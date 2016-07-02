package view;

import model.TaskType;

import javax.swing.*;

class TaskTypeCheckBox extends JCheckBox {
    public TaskTypeCheckBox(TaskType taskType, boolean selected, boolean editable) {
        super(taskType.getName(), selected);
        this.taskType = taskType;
        this.editable = editable;
    }

    public TaskTypeCheckBox(TaskType taskType, boolean selected) {
        this(taskType, selected, true);
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.editable) {
            super.setSelected(selected);
        }
    }

    public TaskType getTaskType() {
        return taskType;
    }

    private final TaskType taskType;
    private final boolean editable;
}
