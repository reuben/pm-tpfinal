package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import model.TaskType;
import model.TechnicianTaskType;

import java.sql.SQLException;

public final class TaskTypeController {
    public static void addTaskType(TaskType taskType) {
        try {
            Dao<TaskType, String> dao = AppController.getTaskTypeDao();
            dao.create(taskType);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void removeTaskType(TaskType taskType) {
        try {
            Dao<TechnicianTaskType, Void> dao = AppController.getTechnicianTaskTypeDao();
            DeleteBuilder<TechnicianTaskType, Void> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(TechnicianTaskType.TASKTYPE_ID_FIELD, taskType);
            deleteBuilder.delete();
            AppController.getTaskTypeDao().delete(taskType);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static TaskType[] getAllTaskTypes() {
        try {
            return AppController.getTaskTypeDao().queryForAll().toArray(new TaskType[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }
}
