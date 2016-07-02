package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import model.TaskType;
import model.Technician;
import model.TechnicianTaskType;
import view.TechnicianDialog;

import java.sql.SQLException;

public final class TechnicianController {
    private TechnicianController() {

    }

    public static Technician[] getAll() {
        Dao<Technician, Long> dao = AppController.getTechnicianDao();
        try {
            return dao.queryForAll().toArray(new Technician[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void addTechnician() {
        view.TechnicianDialog.create(null, TechnicianDialog.DialogMode.READ_WRITE);
    }

    public static void viewTechnician(Technician technician) {
        view.TechnicianDialog.create(technician, TechnicianDialog.DialogMode.READ_ONLY);
    }

    public static void editTechnician(Technician technician) {
        view.TechnicianDialog.create(technician, TechnicianDialog.DialogMode.READ_WRITE);
    }

    public static Technician[] filter(String filter) {
        Dao<Technician, Long> dao = AppController.getTechnicianDao();
        try {
            return dao.queryBuilder().where().like("name", "%" + filter + "%").query().toArray(new Technician[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void saveNewTechnician(Technician technician) {
        try {
            AppController.getTechnicianDao().create(technician);
            AppController.getAppFrame().resetTechnicianView();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void updateTechnician(Technician technician) {
        try {
            AppController.getTechnicianDao().update(technician);
            AppController.getAppFrame().resetTechnicianView();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void removeTechnician(Technician technician) {
        try {
            AppController.getTechnicianDao().delete(technician);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static TaskType[] getTaskTypesForTechnician(Technician technician) {
        Dao<TechnicianTaskType, Void> technicianTaskTypeDao = AppController.getTechnicianTaskTypeDao();
        Dao<TaskType, String> taskTypeDao = AppController.getTaskTypeDao();
        try {
            if (taskTypesForTechnicianQuery == null) {
                QueryBuilder<TechnicianTaskType, Void> innerQuery = technicianTaskTypeDao.queryBuilder();
                innerQuery.selectColumns(TechnicianTaskType.TASKTYPE_ID_FIELD)
                          .where()
                              .eq(TechnicianTaskType.TECHNICIAN_ID_FIELD, new SelectArg());
                taskTypesForTechnicianQuery = taskTypeDao.queryBuilder()
                        .where()
                            .in(TaskType.ID_FIELD, innerQuery)
                        .prepare();
            }

            taskTypesForTechnicianQuery.setArgumentHolderValue(0, technician);
            return taskTypeDao.query(taskTypesForTechnicianQuery).toArray(new TaskType[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static TaskType[] getTaskTypesMinusTechnician(Technician technician) {
        Dao<TechnicianTaskType, Void> technicianTaskTypeDao = AppController.getTechnicianTaskTypeDao();
        Dao<TaskType, String> taskTypeDao = AppController.getTaskTypeDao();
        try {
            if (taskTypesMinusTechnicianQuery == null) {
                QueryBuilder<TechnicianTaskType, Void> innerQuery = technicianTaskTypeDao.queryBuilder();
                innerQuery.selectColumns(TechnicianTaskType.TASKTYPE_ID_FIELD)
                        .where()
                            .eq(TechnicianTaskType.TECHNICIAN_ID_FIELD, new SelectArg());
                taskTypesMinusTechnicianQuery = taskTypeDao.queryBuilder()
                        .where()
                            .notIn(TaskType.ID_FIELD, innerQuery)
                        .prepare();
            }

            taskTypesMinusTechnicianQuery.setArgumentHolderValue(0, technician);
            return taskTypeDao.query(taskTypesMinusTechnicianQuery).toArray(new TaskType[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void updateTaskTypesForTechnician(Technician technician, Iterable<TaskType> taskTypes) {
        try {
            Dao<TechnicianTaskType, Void> dao = AppController.getTechnicianTaskTypeDao();
            DeleteBuilder<TechnicianTaskType, Void> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where()
                    .eq(TechnicianTaskType.TECHNICIAN_ID_FIELD, technician)
                    .and().notIn(TechnicianTaskType.TASKTYPE_ID_FIELD, taskTypes);
            deleteBuilder.delete();
            for (TaskType taskType : taskTypes) {
                TechnicianTaskType joinColumn = new TechnicianTaskType(technician, taskType);
                AppController.getTechnicianTaskTypeDao().create(joinColumn);
            }
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static Technician[] getTechniciansForTaskTypes(Iterable<TaskType> taskTypes) {
        Dao<TechnicianTaskType, Void> technicianTaskTypeDao = AppController.getTechnicianTaskTypeDao();
        Dao<Technician, Long> technicianDao = AppController.getTechnicianDao();
        try {
            QueryBuilder<TechnicianTaskType, Void> innerQuery = technicianTaskTypeDao.queryBuilder();
            innerQuery.selectColumns(TechnicianTaskType.TECHNICIAN_ID_FIELD)
                    .where()
                        .in(TechnicianTaskType.TASKTYPE_ID_FIELD, taskTypes);
            return technicianDao.queryBuilder()
                    .where()
                        .in(Technician.ID_FIELD, innerQuery)
                    .query().toArray(new Technician[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    private static PreparedQuery<TaskType> taskTypesForTechnicianQuery;
    private static PreparedQuery<TaskType> taskTypesMinusTechnicianQuery;
}
