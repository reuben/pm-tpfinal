package view;

import controllers.ServiceRequestController;

import javax.swing.*;
import java.awt.event.*;

public class ClientLoginDialog extends JDialog {
    public ClientLoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(loginButton);

        //loginButton.addActionListener(e -> onOK());
        loginButton.addActionListener(e -> {
            // Check if there is a client with the input CPF
            boolean existingClient = ServiceRequestController.checkIfClientExists(this.cpfTextField.getText());

            if(existingClient){

            } else {

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

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void create() {
        ClientLoginDialog dialog = new ClientLoginDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    private JPanel contentPane;
    private JButton loginButton;
    private JButton buttonCancel;
    private JTextField nameTextField;
    private JTextField cpfTextField;
    private JTextField phoneTextField;
}
