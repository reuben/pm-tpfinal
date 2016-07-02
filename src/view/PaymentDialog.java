package view;

import controllers.AppController;
import model.Payment;
import model.ServiceRequest;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PaymentDialog extends JDialog {
    private PaymentDialog(ServiceRequest request) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Confirmar pagamento");

        this.request = request;

        for (Payment.PaymentType type : Payment.PaymentType.values()) {
            paymentTypeCombobox.addItem(type);
        }

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

    private Payment getPayment() {
        return payment;
    }

    private void onOK() {
        payment = new Payment();
        payment.setRequest(request);
        payment.setReceiptNumber(receiptNumberTextField.getText());
        payment.setType((Payment.PaymentType)paymentTypeCombobox.getSelectedItem());
        payment.setBankName(bankNameTextField.getText());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static Payment create(ServiceRequest request) {
        PaymentDialog dialog = new PaymentDialog(request);
        dialog.pack();
        dialog.setLocationRelativeTo(AppController.getAppFrame());
        dialog.setVisible(true);
        return dialog.getPayment();
    }

    private ServiceRequest request;
    private Payment payment;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField receiptNumberTextField;
    private JComboBox<Payment.PaymentType> paymentTypeCombobox;
    private JTextField bankNameTextField;
}
