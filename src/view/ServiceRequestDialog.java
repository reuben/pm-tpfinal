package view;

import model.ServiceRequest;

import javax.swing.*;
import java.awt.event.*;

public class ServiceRequestDialog extends JDialog {
    public ServiceRequestDialog(ServiceRequest serviceRequest) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.serviceRequest = serviceRequest;

        buttonOK.addActionListener(e -> onOK());
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

    public static void create(ServiceRequest serviceRequest) {
        ServiceRequestDialog dialog = new ServiceRequestDialog(serviceRequest);
        dialog.pack();
        dialog.setVisible(true);
    }

    private ServiceRequest serviceRequest;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
}
