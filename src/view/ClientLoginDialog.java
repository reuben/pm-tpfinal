package view;

import controllers.AppController;
import controllers.ServiceRequestController;
import model.Client;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientLoginDialog extends JDialog {
    private ClientLoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(loginButton);
        setTitle("Login do cliente");

        this.clientName = "";
        this.clientCPF = "";
        this.clientPhone = "";

        loginButton.addActionListener(e -> {
            // Check if there is a client with the input CPF
            this.client = ServiceRequestController.getClientById(this.cpfTextField.getText());
            this.clientName = this.nameTextField.getText();
            this.clientCPF = this.cpfTextField.getText();
            this.clientPhone = this.phoneTextField.getText();
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

    private void onCancel() {
        dispose();
    }

    public Client getClient() {
        return client;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientCPF() {
        return clientCPF;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public static ClientLoginDialog create() {
        ClientLoginDialog dialog = new ClientLoginDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(AppController.getAppFrame());
        dialog.setVisible(true);
        return dialog;
    }

    private Client client;

    private String clientName;
    private String clientCPF;
    private String clientPhone;

    private JPanel contentPane;
    private JButton loginButton;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JTextField cpfTextField;
    private JTextField phoneTextField;

}
