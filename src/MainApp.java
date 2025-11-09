import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static JFrame frame;
    public static List<Home> homes = new ArrayList<>();
    public static double ratePerUnit = 30.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Electricity Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            // Main menu panel
            JPanel mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JButton homeBtn = new JButton("Home Screen");
            JButton deviceBtn = new JButton("Device Screen");
            JButton settingsBtn = new JButton("Settings Screen");
            JButton exitBtn = new JButton("Exit");

            gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(homeBtn, gbc);
            gbc.gridy = 1; mainPanel.add(deviceBtn, gbc);
            gbc.gridy = 2; mainPanel.add(settingsBtn, gbc);
            gbc.gridy = 3; mainPanel.add(exitBtn, gbc);

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}
