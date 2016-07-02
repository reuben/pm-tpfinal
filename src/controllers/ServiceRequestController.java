package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import model.*;
import view.ClientDialog;
import view.ClientLoginDialog;
import view.CreateServiceRequestDialog;
import view.ServiceRequestDialog;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

public final class ServiceRequestController {
    private ServiceRequestController() {
    }

    public static ServiceRequest[] getAll() {
        Dao<ServiceRequest, Long> dao = AppController.getServiceRequestDao();
        try {
            return dao.queryBuilder().where()
                    .not()
                        .in(ServiceRequest.STATUS_FIELD, Arrays.asList(ServiceRequest.Status.Closed, ServiceRequest.Status.Canceled))
                    .query().toArray(new ServiceRequest[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static Client getClientById(String id) {
        Dao<Client, String> dao = AppController.getClientDao();
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void saveNewClient(Client client) {
        Dao<Client, String> dao = AppController.getClientDao();
        try {
            dao.create(client);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void updateClient(Client client) {
        Dao<Client, String> dao = AppController.getClientDao();
        try {
            dao.update(client);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void saveNewEstimate(ServiceEstimate estimate) {
        Dao<ServiceEstimate, Long> dao = AppController.getServiceEstimateDao();
        try {
            dao.create(estimate);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void saveNewPayment(Payment payment) {
        Dao<Payment, Long> dao = AppController.getPaymentDao();
        try {
            dao.create(payment);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void saveNewServiceRequest(ServiceRequest request) {
        Dao<ServiceRequest, Long> dao = AppController.getServiceRequestDao();
        try {
            dao.create(request);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void updateServiceRequest(ServiceRequest request) {
        Dao<ServiceRequest, Long> dao = AppController.getServiceRequestDao();
        try {
            dao.update(request);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static TaskType[] getTaskTypesForServiceRequest(ServiceRequest request) {
        Dao<ServiceRequestTaskType, Void> serviceRequestTaskTypeDao = AppController.getServiceRequestTaskTypeDao();
        Dao<TaskType, String> taskTypeDao = AppController.getTaskTypeDao();
        try {
            if (taskTypesForServiceRequestQuery == null) {
                QueryBuilder<ServiceRequestTaskType, Void> innerQuery = serviceRequestTaskTypeDao.queryBuilder();
                innerQuery.selectColumns(ServiceRequestTaskType.TASKTYPE_ID_FIELD)
                        .where()
                            .eq(ServiceRequestTaskType.SERVICEREQUEST_ID_FIELD, new SelectArg());
                taskTypesForServiceRequestQuery = taskTypeDao.queryBuilder()
                        .where()
                            .in(TaskType.ID_FIELD, innerQuery)
                        .prepare();
            }

            taskTypesForServiceRequestQuery.setArgumentHolderValue(0, request);
            return taskTypeDao.query(taskTypesForServiceRequestQuery).toArray(new TaskType[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void updateTaskTypesForServiceRequest(ServiceRequest request, Collection<TaskType> taskTypes) {
        try {
            Dao<ServiceRequestTaskType, Void> dao = AppController.getServiceRequestTaskTypeDao();
            DeleteBuilder<ServiceRequestTaskType, Void> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where()
                    .eq(ServiceRequestTaskType.SERVICEREQUEST_ID_FIELD, request)
                    .and().notIn(ServiceRequestTaskType.TASKTYPE_ID_FIELD, taskTypes);
            deleteBuilder.delete();
            for (TaskType taskType : taskTypes) {
                ServiceRequestTaskType joinColumn = new ServiceRequestTaskType(request, taskType);
                AppController.getServiceRequestTaskTypeDao().create(joinColumn);
            }
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void addServiceRequest() {
        ClientLoginDialog dialog = ClientLoginDialog.create();
        if (dialog.getClient() != null) {
            CreateServiceRequestDialog.create(dialog.getClient());
        } else {
            Client client = ClientDialog.createAfterFailedLogin(dialog.getClientCPF(), dialog.getClientName(), dialog.getClientPhone());
            if (client != null) {
                CreateServiceRequestDialog.create(client);
            }
        }

        AppController.getAppFrame().resetServiceOrderView();
    }

    public static void editServiceRequest(ServiceRequest serviceRequest) {
        ServiceRequestDialog.create(serviceRequest);
        AppController.getAppFrame().resetServiceOrderView();
    }

    public static ServiceRequest[] filter(String search) {
        return getAll();
    }

    private static PreparedQuery<TaskType> taskTypesForServiceRequestQuery;
}
