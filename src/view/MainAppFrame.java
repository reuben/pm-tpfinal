package view;

import controllers.AppController;
import controllers.TechnicianController;
import model.Technician;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainAppFrame extends JFrame {
    public MainAppFrame() {
        setTitle("FazConcertos");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setContentPane(appPanel);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        addTechnicianBtn.addActionListener(e -> {
            TechnicianController.addTechnician();
        });

        editBtn.addActionListener(e -> {
            TechnicianController.editTechnician((Technician)technicianList.getSelectedValue());
        });

        removeBtn.addActionListener(e -> {
            //TODO: JOptionPane.showConfirmDialog then remove
        });

        techSearchBtn.addActionListener(e -> {
            TechnicianController.filter(technicianSearch.getText());
        });

        technicianList.addListSelectionListener((ListSelectionEvent e) -> {
            editBtn.setEnabled(!technicianList.isSelectionEmpty());
            removeBtn.setEnabled(!technicianList.isSelectionEmpty());
        });

        updateTechnicianView();
        updateOSView();
    }

    private void onCancel() {
        AppController.quit();
        dispose();
    }

    public void updateTechnicianView() {
        technicianList.setListData(TechnicianController.getAll().toArray());
    }

    public void updateOSView() {

    }

    private JList technicianList;
    private JButton addTechnicianBtn;
    private JTextField technicianSearch;
    private JButton techSearchBtn;
    private JList osList;
    private JButton novaOSButton;
    private JTextArea osSearch;
    private JButton osSearchBtn;
    private JPanel appPanel;
    private JButton editBtn;
    private JButton removeBtn;
}
