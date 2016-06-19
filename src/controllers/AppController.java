package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.*;
import view.FatalErrorDialog;
import view.MainAppFrame;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public final class AppController {
    private AppController() {

    }

    public static void initialize() throws SQLException {
        if (!initialized) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                view.FatalErrorDialog.die("Erro fatal ao estabelecer conexão com o banco de dados: " + e.getMessage(), e);
            }
            connectionSource = new JdbcConnectionSource(DB_URL);

            clientDao = DaoManager.createDao(connectionSource, Client.class);
            technicianDao = DaoManager.createDao(connectionSource, Technician.class);
            serviceRequestDao = DaoManager.createDao(connectionSource, ServiceRequest.class);
            taskTypeDao = DaoManager.createDao(connectionSource, TaskType.class);
            technicianTaskTypeDao = DaoManager.createDao(connectionSource, TechnicianTaskType.class);

            TableUtils.createTableIfNotExists(connectionSource, Client.class);
            TableUtils.createTableIfNotExists(connectionSource, Technician.class);
            TableUtils.createTableIfNotExists(connectionSource, ServiceRequest.class);
            if (!taskTypeDao.isTableExists()) {
                TableUtils.createTable(connectionSource, TaskType.class);
                TaskType[] defaults = new TaskType[]{
                        new TaskType("Eletricista"),
                        new TaskType("Bombeiro(a)"),
                        new TaskType("Pedreiro(a)"),
                        new TaskType("Fachineiro(a)")
                };
                for (TaskType taskType : defaults) {
                    taskTypeDao.create(taskType);
                }
            }
            TableUtils.createTableIfNotExists(connectionSource, TechnicianTaskType.class);

            initialized = true;
        }
    }

    public static void quit() {
        try {
            connectionSource.close();
        } catch (IOException e) {
            view.FatalErrorDialog.die("Erro fatal ao fechar a conexão com o banco de dados: " + e.getMessage(), e);
        }
    }

    static Dao<Client, Void> getClientDao() {
        assert(initialized);
        return clientDao;
    }

    static Dao<Technician, Long> getTechnicianDao() {
        assert(initialized);
        return technicianDao;
    }

    static Dao<ServiceRequest, Long> getServiceRequestDao() {
        assert(initialized);
        return serviceRequestDao;
    }

    public static Dao<TaskType, String> getTaskTypeDao() {
        return taskTypeDao;
    }

    public static Dao<TechnicianTaskType, Long> getTechnicianTaskTypeDao() {
        return technicianTaskTypeDao;
    }

    public static MainAppFrame getAppFrame() {
        return appFrame;
    }

    public static void main(String args[]) {
        // Make sure we see the Quit event on OS X as a normal window close
        System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");

        try {
            AppController.initialize();
        } catch (SQLException|IllegalArgumentException e) {
            FatalErrorDialog.die("Erro ao inicializar o banco de dados: " + e.getMessage(), e);
        }

        EventQueue.invokeLater(() -> {
            appFrame = new MainAppFrame();
            appFrame.setVisible(true);
        });
    }

    private static boolean initialized = false;

    private static final String DB_URL = "jdbc:sqlite:database.sqlite";
    private static JdbcConnectionSource connectionSource;

    private static Dao<Client, Void> clientDao;
    private static Dao<Technician, Long> technicianDao;
    private static Dao<ServiceRequest, Long> serviceRequestDao;
    private static Dao<TaskType, String> taskTypeDao;
    private static Dao<TechnicianTaskType, Long> technicianTaskTypeDao;

    private static MainAppFrame appFrame;
}
