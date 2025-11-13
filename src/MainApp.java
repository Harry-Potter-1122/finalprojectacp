import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static JFrame frame;
    public static JPanel cardPanel;
    public static CardLayout cardLayout;

    public static List<Home> homes = new ArrayList<>();
    public static double ratePerUnit = 30.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Electricity Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        // Use CardLayout to switch screens
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create all panels
        JPanel menuPanel = createMenuPanel();
        JPanel homePanel = Screen.homeScreen();
        JPanel devicePanel = Screen.deviceScreen();
        JPanel settingsPanel = Screen.settingsScreen();

        // Add them to the card panel
        cardPanel.add(menuPanel, "MENU");
        cardPanel.add(homePanel, "HOME");
        cardPanel.add(devicePanel, "DEVICE");
        cardPanel.add(settingsPanel, "SETTINGS");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    private static JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton homeBtn = new JButton(" Home Screen");
        JButton deviceBtn = new JButton(" Device Screen");
        JButton settingsBtn = new JButton(" Settings Screen");
        JButton exitBtn = new JButton(" Exit");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(homeBtn, gbc);
        gbc.gridy = 1; panel.add(deviceBtn, gbc);
        gbc.gridy = 2; panel.add(settingsBtn, gbc);
        gbc.gridy = 3; panel.add(exitBtn, gbc);

        // ---------- Action Listeners ----------
        homeBtn.addActionListener(e -> cardLayout.show(cardPanel, "HOME"));
        deviceBtn.addActionListener(e -> cardLayout.show(cardPanel, "DEVICE"));
        settingsBtn.addActionListener(e -> cardLayout.show(cardPanel, "SETTINGS"));
        exitBtn.addActionListener(e -> System.exit(0));

        return panel;
    }
}
