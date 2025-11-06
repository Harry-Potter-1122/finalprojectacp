import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MainApp {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static List<Home> homes = new ArrayList<>();
    private static double ratePerUnit = 30.0;
    private static final String DATA_FILE = "data.txt";

    public static void main(String[] args) {
        loadData();
        SwingUtilities.invokeLater(MainApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Electricity Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton homesBtn = new JButton("Manage Homes");
        JButton devicesBtn = new JButton("Manage Devices");
        JButton settingsBtn = new JButton("Settings");
        JButton saveExitBtn = new JButton("Save & Exit");

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(homesBtn, gbc);
        gbc.gridy = 1;
        mainPanel.add(devicesBtn, gbc);
        gbc.gridy = 2;
        mainPanel.add(settingsBtn, gbc);
        gbc.gridy = 3;
        mainPanel.add(saveExitBtn, gbc);

        homesBtn.addActionListener(e -> Screen.showHomeScreen(frame, homes));
        devicesBtn.addActionListener(e -> Screen.showDeviceScreen(frame, homes, ratePerUnit));
        settingsBtn.addActionListener(e -> ratePerUnit = Screen.showSettingsScreen(frame, ratePerUnit));
        saveExitBtn.addActionListener(e -> {
            saveData();
            JOptionPane.showMessageDialog(frame, "Data saved successfully. Exiting...");
            frame.dispose();
            System.exit(0);
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // ---------------- File Handling ----------------
    private static void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println(ratePerUnit);
            for (Home home : homes) {
                writer.println("HOME:" + home.getName());
                for (Device d : home.getDevices()) {
                    writer.println("DEVICE:" + d.getName() + "," + d.getWatts() + "," + d.getHours());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage());
        }
    }

    private static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) ratePerUnit = Double.parseDouble(line);

            Home currentHome = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("HOME:")) {
                    currentHome = new Home(line.substring(5));
                    homes.add(currentHome);
                } else if (line.startsWith("DEVICE:") && currentHome != null) {
                    String[] parts = line.substring(7).split(",");
                    currentHome.addDevice(new Device(parts[0],
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2])));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    // ---------------- Getters for rate and homes ----------------
    public static double getRatePerUnit() { return ratePerUnit; }
    public static void setRatePerUnit(double rate) { ratePerUnit = rate; }
    public static List<Home> getHomes() { return homes; }
}
