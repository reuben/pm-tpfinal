package view;

import controllers.AppController;
import controllers.ServiceRequestController;
import controllers.TaskTypeController;
import model.Client;
import model.ServiceRequest;
import model.TaskType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

public class CreateServiceRequestDialog extends JDialog {
    private CreateServiceRequestDialog(Client client) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 350);
        setMinimumSize(new Dimension(400, 350));
        setPreferredSize(new Dimension(400, 350));
        setTitle("Nova requisição de serviço");

        this.client = client;
        clientLabel.setText("<html>" +
                client.getName() + "<br>" +
                client.getId() + "<br>" +
                client.getCPF() + "<br>" +
                client.getAddress() + "<br>" +
                client.getPhone() + "<br>" +
                client.getEmail() +
                "</html>");

        Font font = this.editClientBtn.getFont();
        Map attrs = font.getAttributes();
        attrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        this.editClientBtn.setFont(font.deriveFont(attrs));

        this.editClientBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClientDialog.create(client);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                editClientBtn.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                editClientBtn.setBorderPainted(false);
            }
        });

        taskTypesListModel = new DefaultListModel<>();
        for (TaskType taskType : TaskTypeController.getAllTaskTypes()) {
            taskTypesListModel.addElement(new TaskTypeCheckBox(taskType, false));
        }

        taskTypesList.setModel(taskTypesListModel);

        buttonOK.addActionListener(e -> {
            ArrayList<TaskType> taskTypes = new ArrayList<>();
            for (int i = 0; i < taskTypesListModel.size(); ++i) {
                if (taskTypesListModel.get(i).isSelected()) {
                    taskTypes.add(taskTypesListModel.get(i).getTaskType());
                }
            }
            ServiceRequest request = new ServiceRequest(this.client, this.descriptionTextArea.getText());
            ServiceRequestController.saveNewServiceRequest(request);
            ServiceRequestController.updateTaskTypesForServiceRequest(request, taskTypes);
            dispose();
        });

        buttonCancel.addActionListener(e -> onCancel());

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
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void create(Client client) {
        CreateServiceRequestDialog dialog = new CreateServiceRequestDialog(client);
        dialog.pack();
        dialog.setLocationRelativeTo(AppController.getAppFrame());
        dialog.setVisible(true);
    }

    private final Client client;
    private DefaultListModel<TaskTypeCheckBox> taskTypesListModel;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private CheckBoxList taskTypesList;
    private JTextArea descriptionTextArea;
    private JLabel clientLabel;
    private JButton editClientBtn;
}
