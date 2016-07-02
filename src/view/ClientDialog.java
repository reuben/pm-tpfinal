package view;

import controllers.AppController;
import controllers.ServiceRequestController;
import model.Client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.time.LocalDate;

public class ClientDialog extends JDialog {
    private void createUIComponents() {
        try {
            this.birthDateTextField = new JFormattedTextField(new MaskFormatter("##/##/####"));
        } catch (ParseException e) {
            assert(false); // unreachable
        }
    }

    private ClientDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Cadastro do cliente");

        this.cpfTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableRegisterButton(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableRegisterButton(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableRegisterButton(e);
            }

            private void enableRegisterButton(DocumentEvent e) {
                buttonOK.setEnabled(e.getDocument().getLength() > 0);
            }
        });

        buttonOK.addActionListener(e -> {
            if (this.client == null) {
                this.client = new Client(this.nameTextField.getText(),
                        this.idTextField.getText(),
                        this.cpfTextField.getText(),
                        this.addressTextField.getText(),
                        LocalDate.parse(this.birthDateTextField.getText(), AppController.BRAZILIAN_LOCAL_DATE),
                        this.emailTextField.getText(),
                        this.phoneTextField.getText());
                ServiceRequestController.saveNewClient(this.client);
                dispose();
            } else {
                client.setName(this.nameTextField.getText());
                client.setId(this.idTextField.getText());
                client.setCPF(this.cpfTextField.getText());
                client.setAddress(this.addressTextField.getText());
                client.setBirthdate(LocalDate.parse(this.birthDateTextField.getText(), AppController.BRAZILIAN_LOCAL_DATE));
                client.setEmail(this.emailTextField.getText());
                client.setPhone(this.phoneTextField.getText());
                ServiceRequestController.updateClient(client);
            }
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

    private ClientDialog(Client client) {
        this();
        this.nameTextField.setText(client.getName());
        this.idTextField.setText(client.getId());
        this.cpfTextField.setText(client.getCPF());
        this.addressTextField.setText(client.getAddress());
        this.birthDateTextField.setText(client.getBirthdate().format(AppController.BRAZILIAN_LOCAL_DATE));
        this.emailTextField.setText(client.getEmail());
        this.phoneTextField.setText(client.getPhone());
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    private void onCancel() {
        dispose();
    }

    public static Client create(Client client) {
        ClientDialog dialog = new ClientDialog(client);
        dialog.pack();
        dialog.setLocationRelativeTo(AppController.getAppFrame());
        dialog.setVisible(true);
        return dialog.getClient();
    }

    public static Client createAfterFailedLogin(String clientCPF, String clientName, String clientPhone) {
        ClientDialog dialog = new ClientDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(AppController.getAppFrame());
        dialog.setDataFromFailedLogin(clientCPF, clientName, clientPhone);
        dialog.setVisible(true);
        return dialog.getClient();
    }

    private void setDataFromFailedLogin(String clientCPF, String clientName, String clientPhone) {
        this.cpfTextField.setText(clientCPF);
        this.nameTextField.setText(clientName);
        this.phoneTextField.setText(clientPhone);
    }

    private Client client;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JTextField cpfTextField;
    private JTextField phoneTextField;
    private JTextField idTextField;
    private JTextField addressTextField;
    private JFormattedTextField birthDateTextField;
    private JTextField emailTextField;
}
