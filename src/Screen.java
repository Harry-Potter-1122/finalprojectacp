import javax.swing.*;
import java.awt.*;

public class Screen {

    // ---------- Home Screen ----------
    public static JPanel homeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("🏠 Home Screen", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        DefaultListModel<String> homeListModel = new DefaultListModel<>();
        JList<String> homeList = new JList<>(homeListModel);
        JScrollPane scrollPane = new JScrollPane(homeList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addHomeBtn = new JButton("Add Home");
        JButton deleteHomeBtn = new JButton("Delete Home");
        buttonPanel.add(addHomeBtn);
        buttonPanel.add(deleteHomeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ---------- Device Screen ----------
    public static JPanel deviceScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("⚡ Device Screen", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        DefaultListModel<String> deviceListModel = new DefaultListModel<>();
        JList<String> deviceList = new JList<>(deviceListModel);
        JScrollPane scrollPane = new JScrollPane(deviceList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Device Name:"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Power (Watts):"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Hours per Day:"));
        inputPanel.add(new JTextField());

        JPanel buttonPanel = new JPanel();
        JButton addDeviceBtn = new JButton("Add Device");
        JButton deleteDeviceBtn = new JButton("Delete Device");
        buttonPanel.add(addDeviceBtn);
        buttonPanel.add(deleteDeviceBtn);

        bottomPanel.add(inputPanel);
        bottomPanel.add(buttonPanel);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Settings Screen ----------
    public static JPanel settingsScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("⚙️ Settings Screen", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(3, 1, 10, 10));
        content.add(new JLabel("Electricity Rate (Rs. per kWh):", SwingConstants.CENTER));
        content.add(new JTextField("30"));
        JButton saveBtn = new JButton("Save Rate");
        content.add(saveBtn);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }
}
