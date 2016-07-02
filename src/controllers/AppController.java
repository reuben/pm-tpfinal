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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;

import static java.time.temporal.ChronoField.*;

public final class AppController {
    private AppController() {
    }

    private static void initialize() throws SQLException {
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
            serviceRequestTaskTypeDao = DaoManager.createDao(connectionSource, ServiceRequestTaskType.class);
            paymentDao = DaoManager.createDao(connectionSource, Payment.class);
            serviceEstimateDao = DaoManager.createDao(connectionSource, ServiceEstimate.class);

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
            TableUtils.createTableIfNotExists(connectionSource, ServiceRequestTaskType.class);
            TableUtils.createTableIfNotExists(connectionSource, ServiceEstimate.class);
            TableUtils.createTableIfNotExists(connectionSource, Payment.class);
            TableUtils.createTableIfNotExists(connectionSource, ServiceEstimate.class);

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

    static Dao<Client, String> getClientDao() {
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

    static Dao<TaskType, String> getTaskTypeDao() {
        return taskTypeDao;
    }

    static Dao<TechnicianTaskType, Void> getTechnicianTaskTypeDao() {
        return technicianTaskTypeDao;
    }

    static Dao<ServiceRequestTaskType, Void> getServiceRequestTaskTypeDao() {
        return serviceRequestTaskTypeDao;
    }

    static Dao<ServiceEstimate, Long> getServiceEstimateDao() {
        return serviceEstimateDao;
    }

    static Dao<Payment, Long> getPaymentDao() {
        return paymentDao;
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

    public static final DateTimeFormatter BRAZILIAN_LOCAL_DATE;
    static {
        BRAZILIAN_LOCAL_DATE = new DateTimeFormatterBuilder()
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral('/')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('/')
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .toFormatter();
    }

    public static final DateTimeFormatter BRAZILIAN_LOCAL_DATETIME;
    static {
        BRAZILIAN_LOCAL_DATETIME = new DateTimeFormatterBuilder()
                .append(BRAZILIAN_LOCAL_DATE)
                .appendLiteral(' ')
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .toFormatter();
    }

    private static boolean initialized = false;

    private static final String DB_URL = "jdbc:sqlite:database.sqlite";
    private static JdbcConnectionSource connectionSource;

    private static Dao<Client, String> clientDao;
    private static Dao<Technician, Long> technicianDao;
    private static Dao<ServiceRequest, Long> serviceRequestDao;
    private static Dao<TaskType, String> taskTypeDao;
    private static Dao<TechnicianTaskType, Void> technicianTaskTypeDao;
    private static Dao<ServiceRequestTaskType, Void> serviceRequestTaskTypeDao;
    private static Dao<ServiceEstimate, Long> serviceEstimateDao;
    private static Dao<Payment, Long> paymentDao;

    private static MainAppFrame appFrame;
}
