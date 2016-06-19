package view;

import controllers.TechnicianController;
import model.TaskType;
import model.Technician;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class TechnicianDialog extends JDialog {
    public enum DialogMode {
        READ_ONLY,
        READ_WRITE
    }

    private class TaskTypeCheckBox extends JCheckBox {
        public TaskTypeCheckBox(TaskType taskType, boolean enabled) {
            super(taskType.getName(), enabled);
            this.taskType = taskType;
        }

        public TaskType getTaskType() {
            return taskType;
        }

        private TaskType taskType;
    }

    private TechnicianDialog(Technician technician, DialogMode mode) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveBtn);
        setTitle("Detalhes do técnico");
        setLocationRelativeTo(null);

        this.technician = technician;
        this.taskTypeCheckboxes = new Vector<TaskTypeCheckBox>();

        if (technician != null) {
            nameTextField.setText(technician.getName());
            emailTextField.setText(technician.getEmail());
            phoneTextField.setText(technician.getPhone());
            for (TaskType taskType : TechnicianController.getTaskTypesForTechnician(technician)) {
                taskTypeCheckboxes.add(new TaskTypeCheckBox(taskType, true));
            }
            for (TaskType taskType : TechnicianController.getTaskTypesMinusTechnician(technician)) {
                taskTypeCheckboxes.add(new TaskTypeCheckBox(taskType, false));
            }
        } else {
            for (TaskType taskType : TechnicianController.getAllTaskTypes()) {
                taskTypeCheckboxes.add(new TaskTypeCheckBox(taskType, false));
            }
        }
        taskTypesList.setListData(taskTypeCheckboxes);

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
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        if (mode == DialogMode.READ_WRITE) {
            final DocumentListener editListener = new DocumentListener() {
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
            };
            nameTextField.getDocument().addDocumentListener(editListener);
            emailTextField.getDocument().addDocumentListener(editListener);
            phoneTextField.getDocument().addDocumentListener(editListener);
            taskTypesList.addListSelectionListener(e -> saveBtn.setEnabled(true));
        }

        saveBtn.addActionListener(e -> {
            if (this.technician == null) {
                Technician newTechnician = new Technician(nameTextField.getText(), emailTextField.getText(), phoneTextField.getText());
                TechnicianController.saveNewTechnician(newTechnician);
                TechnicianController.updateTaskTypesForTechnician(newTechnician,
                        taskTypesList.getSelectedValuesList().stream().map(TaskTypeCheckBox::getTaskType).collect(Collectors.toList()));
            } else {
                this.technician.setName(nameTextField.getText());
                this.technician.setEmail(emailTextField.getText());
                this.technician.setPhone(phoneTextField.getText());
                TechnicianController.updateTechnician(this.technician);
                TechnicianController.updateTaskTypesForTechnician(this.technician,
                        taskTypesList.getSelectedValuesList().stream().map(TaskTypeCheckBox::getTaskType).collect(Collectors.toList()));
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

    private final Technician technician;
    private final Vector<TaskTypeCheckBox> taskTypeCheckboxes;

    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField phoneTextField;
    private CheckBoxList<TaskTypeCheckBox> taskTypesList;
}
