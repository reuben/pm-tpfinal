package controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import model.Technician;
import view.TechnicianDialog;

import java.sql.SQLException;
import java.util.List;

public final class TechnicianController {
    private TechnicianController() {

    }

    public static Technician[] getAll() {
        Dao<Technician, Long> dao = AppController.getTechnicianDao();
        try {
            return (Technician[])dao.queryForAll().toArray();
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

    public static Technician[] filter(String filter) {
        Dao<Technician, Long> dao = AppController.getTechnicianDao();
        try {
            PreparedQuery<Technician> query = dao.queryBuilder().where().like("name", "%" + filter + "%").prepare();
            return (Technician[])dao.query(query).toArray();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
        return null; // unreachable
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

    public static void removeTechnician(Technician technician) {
        try {
            technician.delete();
        } catch (SQLException e) {
            view.FatalErrorDialog.die("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }
}
