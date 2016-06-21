package controllers;

import com.j256.ormlite.dao.Dao;
import model.ServiceRequest;
import view.ServiceRequestDialog;

import java.sql.SQLException;

public final class ServiceRequestController {
    private ServiceRequestController() {
    }


    public static ServiceRequest[] getAll() {
        Dao<ServiceRequest, Long> dao = AppController.getServiceRequestDao();
        try {
            return dao.queryForAll().toArray(new ServiceRequest[0]);
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void addServiceRequest() {
        //TODO: implement GUI flow:
        // ClientLoginDialog ->
        //     if client exists
        //         ServiceRequestDialog
        //     else
        //         ClientRegisterDialog -> ServiceRequestDialog
        AppController.getAppFrame().resetServiceOrderView();
    }

    //TODO: all the Client related logic needed by addServiceRequest flow above

    public static void editServiceRequest(ServiceRequest serviceRequest) {
        ServiceRequestDialog.create(serviceRequest);
    }

    public static ServiceRequest[] filter(String search) {
        //TODO: filter service requests. implement single search for all searchable columns first, then we can think about better UI
        return null;
    }
}
