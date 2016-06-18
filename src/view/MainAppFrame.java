package view;

import javax.swing.*;
import java.awt.*;

public class MainAppFrame extends JFrame {
    private MainAppFrame() {
        setTitle("FazConcertos");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(appPanel);
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            MainAppFrame app = new MainAppFrame();
            app.setVisible(true);
        });
    }

    private JTabbedPane tabbedPane1;
    private JPanel techniciansPanel;
    private JPanel ordersPanel;
    private JList list1;
    private JButton addTechnicianBtn;
    private JTextField technicianSearch;
    private JButton techSearchBtn;
    private JList list2;
    private JButton novaOSButton;
    private JTextArea osSearch;
    private JButton osSearchBtn;
    private JPanel appPanel;
}
