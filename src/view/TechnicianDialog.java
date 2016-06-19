package view;

import controllers.TechnicianController;
import model.Technician;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

public class TechnicianDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField phoneTextField;
    private CheckBoxList taskTypesList;

    public enum DialogMode {
        READ_ONLY,
        READ_WRITE
    };

    public TechnicianDialog(Technician technician, DialogMode mode) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveBtn);
        setTitle("Editar técnico");
        setLocationRelativeTo(null);

        if (technician != null) {
            nameTextField.setText(technician.getName());
            emailTextField.setText(technician.getEmail());
            phoneTextField.setText(technician.getPhone());
            //TODO: task types binding
        }

        if (mode == DialogMode.READ_ONLY) {
            nameTextField.setEnabled(false);
            emailTextField.setEnabled(false);
            phoneTextField.setEnabled(false);
            taskTypesList.setEnabled(false);
        }

        saveBtn.addActionListener(e -> onOK());
        cancelBtn.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        if (mode == DialogMode.READ_WRITE) {
            nameTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    enableSaveButton();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    enableSaveButton();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    enableSaveButton();
                }

                private void enableSaveButton() {
                    saveBtn.setEnabled(true);
                }
            });
        }

        saveBtn.addActionListener((ActionEvent e) -> {
            if (technician == null) {
                Technician newTechnician = new Technician(nameTextField.getText(), emailTextField.getText(), phoneTextField.getText(), null);
                TechnicianController.saveNewTechnician(newTechnician);
            } else {
                TechnicianController.updateTechnician(technician);
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void create(Technician technician, DialogMode mode) {
        TechnicianDialog dialog = new TechnicianDialog(technician, mode);
        dialog.pack();
        dialog.setVisible(true);
    }
}
