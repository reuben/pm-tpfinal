package view;

import model.TaskType;

import javax.swing.*;

class TaskTypeCheckBox extends JCheckBox {
    public TaskTypeCheckBox(TaskType taskType, boolean enabled) {
        super(taskType.getName(), enabled);
        this.taskType = taskType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    private TaskType taskType;
}
