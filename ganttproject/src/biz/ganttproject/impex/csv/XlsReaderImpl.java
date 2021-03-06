/*
Copyright 2017 Roman Torkhov, BarD Software s.r.o

This file is part of GanttProject, an opensource project management tool.

GanttProject is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
*/
package biz.ganttproject.impex.csv;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author torkhov
 */
class XlsReaderImpl implements SpreadsheetReader {

  private final Workbook myBook;
  private final Map<String, Integer> myHeaders;

  XlsReaderImpl(InputStream is, List<String> columnHeaders) throws IOException {
    myBook = new HSSFWorkbook(is);
    myHeaders = initializeHeader(columnHeaders);
  }

  @Override
  public void close() throws IOException {
    myBook.close();
  }

  @Override
  public Iterator<SpreadsheetRecord> iterator() {
    return Iterators.transform(myBook.getSheetAt(0).iterator(), (input) -> new XlsRecordImpl(getCellValues(input), myHeaders));
  }

  private List<String> getCellValues(Row row) {
    return Lists.newArrayList(Iterables.transform(row, Cell::getStringCellValue));
  }

  /**
   * This method was taken from {@link org.apache.commons.csv.CSVParser#initializeHeader}
   * Create the name to index mapping if the column headers not {@code null}.
   * @param columnHeaders column headers
   * @return the name to index mapping
   */
  private Map<String, Integer> initializeHeader(List<String> columnHeaders) {
    Map<String, Integer> hdrMap = null;
    List<String> formatHeader = columnHeaders;
    if (formatHeader != null) {
      hdrMap = new LinkedHashMap<>();
      List<String> headerRecord = null;
      if (formatHeader.isEmpty()) {
        // read the header from the first line of the file
        Row row = myBook.getSheetAt(0).getRow(0);
        if (row != null) {
          headerRecord = getCellValues(row);
        }
      } else {
        headerRecord = formatHeader;
      }

      // build the name to index mappings
      if (headerRecord != null) {
        for (int i = 0; i < headerRecord.size(); i++) {
          String header = headerRecord.get(i);
          if (hdrMap.containsKey(header)) {
            throw new IllegalArgumentException("The header contains a duplicate name: \"" + header + "\" in " + headerRecord);
          }
          hdrMap.put(header, i);
        }
      }
    }
    return hdrMap;
  }
}