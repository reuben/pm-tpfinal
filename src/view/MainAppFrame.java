package view;

import controllers.AppController;
import controllers.ServiceRequestController;
import controllers.TechnicianController;
import model.ServiceRequest;
import model.Technician;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainAppFrame extends JFrame {
    public MainAppFrame() {
        setTitle("FazConcertos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setContentPane(appPanel);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        initTechnicianView();
        initServiceOrderView();
    }

    private void onCancel() {
        AppController.quit();
        dispose();
    }

    private void initTechnicianView() {
        addBtn.addActionListener(e -> TechnicianController.addTechnician());

        editBtn.addActionListener(e -> TechnicianController.editTechnician(technicianList.getSelectedValue()));

        removeBtn.addActionListener(e -> {
            Technician technician = technicianList.getSelectedValue();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja apagar o cadastro do técnico '" + technician.getName() + "'?",
                    "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                TechnicianController.removeTechnician(technician);
                resetTechnicianView();
            }
        });

        ActionListener searchListener = e -> technicianList.setListData(TechnicianController.filter(technicianSearch.getText()));
        technicianSearchBtn.addActionListener(searchListener);
        technicianSearch.addActionListener(searchListener);

        technicianList.addListSelectionListener(e -> {
            boolean isSelectionEmpty = technicianList.isSelectionEmpty();
            editBtn.setEnabled(!isSelectionEmpty);
            removeBtn.setEnabled(!isSelectionEmpty);
        });

        resetTechnicianView();
    }

    public void resetTechnicianView() {
        technicianList.setListData(TechnicianController.getAll());
    }

    private void initServiceOrderView() {
        createServiceRequestBtn.addActionListener(e -> ServiceRequestController.addServiceRequest());

        viewServiceRequestBtn.addActionListener(e -> ServiceRequestController.editServiceRequest(serviceRequestList.getSelectedValue()));

        ActionListener searchListener = e -> serviceRequestList.setListData(ServiceRequestController.filter(serviceRequestSearch.getText()));
        technicianSearchBtn.addActionListener(searchListener);
        technicianSearch.addActionListener(searchListener);

        serviceRequestList.addListSelectionListener(e -> viewServiceRequestBtn.setEnabled(!serviceRequestList.isSelectionEmpty()));

        resetServiceOrderView();
    }

    public void resetServiceOrderView() {
        serviceRequestList.setListData(ServiceRequestController.getAll());
    }

    private JPanel appPanel;

    private JList<Technician> technicianList;
    private JButton addBtn;
    private JButton editBtn;
    private JButton removeBtn;
    private JTextField technicianSearch;

    private JButton technicianSearchBtn;
    private JList<ServiceRequest> serviceRequestList;
    private JButton createServiceRequestBtn;
    private JTextField serviceRequestSearch;
    private JButton serviceRequestSearchBtn;
    private JButton viewServiceRequestBtn;
}
