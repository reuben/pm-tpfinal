package view;

import controllers.TechnicianController;
import model.TaskType;
import model.Technician;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public class TechnicianDialog extends JDialog {
    public enum DialogMode {
        READ_ONLY,
        READ_WRITE
    }

    private TechnicianDialog(Technician technician, DialogMode mode) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveBtn);
        setTitle("Detalhes do técnico");
        setSize(365, 300);
        setMinimumSize(new Dimension(365, 300));
        setPreferredSize(new Dimension(365, 300));
        setLocationRelativeTo(null);

        this.technician = technician;
        taskTypesListModel = new DefaultListModel<>();

        if (technician != null) {
            nameTextField.setText(technician.getName());
            emailTextField.setText(technician.getEmail());
            phoneTextField.setText(technician.getPhone());
            for (TaskType taskType : TechnicianController.getTaskTypesForTechnician(technician)) {
                taskTypesListModel.addElement(new TaskTypeCheckBox(taskType, true));
            }
            for (TaskType taskType : TechnicianController.getTaskTypesMinusTechnician(technician)) {
                taskTypesListModel.addElement(new TaskTypeCheckBox(taskType, false));
            }
        } else {
            for (TaskType taskType : TechnicianController.getAllTaskTypes()) {
                taskTypesListModel.addElement(new TaskTypeCheckBox(taskType, false));
            }
        }

        taskTypesList.setModel(taskTypesListModel);

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
            Vector<TaskType> taskTypes = new Vector<>();
            for (int i = 0; i < taskTypesListModel.size(); ++i) {
                if (taskTypesListModel.get(i).isSelected()) {
                    taskTypes.add(taskTypesListModel.get(i).getTaskType());
                }
            }
            if (this.technician == null) {
                Technician newTechnician = new Technician(nameTextField.getText(), emailTextField.getText(), phoneTextField.getText());
                TechnicianController.saveNewTechnician(newTechnician);
                TechnicianController.updateTaskTypesForTechnician(newTechnician, taskTypes);
            } else {
                this.technician.setName(nameTextField.getText());
                this.technician.setEmail(emailTextField.getText());
                this.technician.setPhone(phoneTextField.getText());
                TechnicianController.updateTechnician(this.technician);
                TechnicianController.updateTaskTypesForTechnician(this.technician, taskTypes);
            }
        });

        this.editListAction = new EditListAction(() -> {
            TechnicianController.addTaskType(this.taskTypesList.getSelectedValue().getTaskType());
        });

        addTaskTypeBtn.addActionListener(e -> {
            TaskTypeCheckBox newTaskType = new TaskTypeCheckBox(new TaskType(""), true);
            this.taskTypesListModel.addElement(newTaskType);
            this.taskTypesList.setSelectedValue(newTaskType, true);
            this.editListAction.actionPerformed(this.taskTypesList);
        });

        removeTaskTypeBtn.addActionListener(e -> {
            if (this.taskTypesList.isSelectionEmpty()) {
                return;
            }
            TaskTypeCheckBox checkBox = this.taskTypesList.getSelectedValue();
            TaskType taskType = checkBox.getTaskType();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja remover a habilitação '" + taskType.getName() + "'?\n" +
                    "Isso irá remover essa habilitação de todos os técnicos que a possuam.", "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                TechnicianController.removeTaskType(taskType);
                this.taskTypesListModel.removeElement(checkBox);
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

    private Technician technician;

    private EditListAction editListAction;
    private DefaultListModel<TaskTypeCheckBox> taskTypesListModel;

    private JPanel contentPane;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField phoneTextField;
    private CheckBoxList<TaskTypeCheckBox> taskTypesList;
    private JButton addTaskTypeBtn;
    private JButton removeTaskTypeBtn;
    private JPanel addRemovePanel;
}
