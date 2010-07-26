package net.sourceforge.ganttproject.task;

import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.ganttproject.GanttCalendar;

/**
 * This class is used to describe the hierarchy of the tasks.
 * 
 * @author bbaranne (Benoit Baranne)
 */
public class TaskNode extends DefaultMutableTreeNode {
    /**
     * The reference task
     */
    private final Task task;

    /**
     * Creates an instance of TaskNode with the given task as reference.
     * 
     * @param t
     *            Task of reference.
     */
    public TaskNode(Task t) {
        super(t);
        task = t;
    }

    /**
     * Sets the priority of the task.
     * 
     * @param priority
     *            The priority to be set.
     */
    public void setPriority(Task.Priority priority) {
        task.setPriority(priority);
    }

    /**
     * @return the priority of the task.
     */
    public Task.Priority getPriority() {
        return task.getPriority();
    }

    /**
     * Sets the name of the task.
     * 
     * @param newName
     *            The name to be set.
     */
    public void setName(String newName) {
        task.setName(newName);
    }

    /**
     * @return the name of the task.
     */
    public String getName() {
        return task.getName();
    }

    /**
     * Sets the start date of the task.
     * 
     * @param startDate
     *            The start date of the task to be set.
     */
    public void setStart(GanttCalendar startDate) {
        TaskMutator mutator = task.createMutatorFixingDuration();
        mutator.setStart(startDate);
        mutator.commit();
    }

    /**
     * @return the start date of the task.
     */
    public GanttCalendar getStart() {
        return task.getStart();
    }

    /**
     * Sets the end date of the task.
     * 
     * @param endDate
     *            The end date of the task to be set.
     */
    public void setEnd(GanttCalendar endDate) {
    	TaskMutator mutator = task.createMutator();
        mutator.setEnd(endDate);
        mutator.commit();
    }

    /**
     * @return the end date of the task.
     */
    public GanttCalendar getEnd() {
        return task.getEnd();
    }

    /**
     * Sets the duration of the task.
     * 
     * @param length
     *            The duration to be set.
     */
    public void setDuration(TaskLength length) {
        TaskMutator mutator = task.createMutator();
        mutator.setDuration(length);
        mutator.commit();
    }

    /**
     * @return the duration of the task.
     */
    public int getDuration() {
        return (int) task.getDuration().getValue();
    }

    /**
     * Sets the completion percentage of the task.
     * 
     * @param percentage
     *            The percentage to be set.
     */
    public void setCompletionPercentage(int percentage) {
        task.setCompletionPercentage(percentage);
    }

    /**
     * @return the completion percentage of the task.
     */
    public int getCompletionPercentage() {
        return task.getCompletionPercentage();
    }

    public void setTaskInfo(TaskInfo info) {
        task.setTaskInfo(info);
    }

    public TaskInfo getTaskInfo() {
        return task.getTaskInfo();
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        return task.getName();
    }

    /**
     * @inheritDoc
     */
    public Object getUserObject() {
        return task;
    }

    public void applyThirdDateConstraint() {
        if (task.getThird() != null)
            switch (task.getThirdDateConstraint()) {
            case TaskImpl.EARLIESTBEGIN:
                if (task.getThird().after(getStart())) {
                    task.setStart(task.getThird().newAdd(0));
                }
            }
    }

}
