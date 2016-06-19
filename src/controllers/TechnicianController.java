package controllers;

import com.j256.ormlite.dao.Dao;
import model.Technician;
import view.MainAppFrame;
import view.TechnicianDialog;

import java.sql.SQLException;
import java.util.List;

public final class TechnicianController {
    private TechnicianController() {

    }

    public static List<Technician> getAll() {
        Dao<Technician, Long> dao = AppController.getTechnicianDao();
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
    }

    public static void addTechnician() {
        view.TechnicianDialog.create(null, TechnicianDialog.DialogMode.READ_WRITE);
    }

    public static void editTechnician(Technician technician) {
        view.TechnicianDialog.create(technician, TechnicianDialog.DialogMode.READ_WRITE);
    }

    public static List<Technician> filter(String filter) {
        return null;
    }

    public static void saveNewTechnician(Technician technician) {
        try {
            technician.setDao(AppController.getTechnicianDao());
            technician.create();
            AppController.getAppFrame().updateTechnicianView();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public static void updateTechnician(Technician technician) {
        try {
            technician.update();
            AppController.getAppFrame().updateTechnicianView();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }
}
