package view;

import controllers.AppController;
import controllers.TechnicianController;
import model.Technician;

import javax.swing.*;
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

        addTechnicianBtn.addActionListener(e -> TechnicianController.addTechnician());

        editBtn.addActionListener(e -> TechnicianController.editTechnician(technicianList.getSelectedValue()));

        removeBtn.addActionListener(e -> {
            Technician technician = technicianList.getSelectedValue();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja apagar o cadastro do técnico '" + technician.getName() + "'?", "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                TechnicianController.removeTechnician(technician);
                updateTechnicianView();
            }
        });

        techSearchBtn.addActionListener(e -> technicianList.setListData(TechnicianController.filter(technicianSearch.getText())));

        technicianList.addListSelectionListener(e -> {
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
        technicianList.setListData(TechnicianController.getAll());
    }

    public void updateOSView() {

    }

    private JList<Technician> technicianList;
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
