package view;

import javax.swing.*;

public class FatalErrorDialog {
    public static void die(String error, Exception e) {
        JOptionPane.showMessageDialog(null,
                error,
                "Erro fatal",
                JOptionPane.ERROR_MESSAGE);
        if (e != null) {
            e.printStackTrace();
        }
        System.exit(1);
    }
}
