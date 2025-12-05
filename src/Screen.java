import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class Screen extends JFrame {

    private final Database db;
    private JPanel mainContent;
    private double ratePerUnit = 30.0;

    // runtime list of homes loaded from DB
    private java.util.List<Home> homes;

    public Screen(Database db) {
        // Set the Look and Feel to Nimbus for a more modern appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to the default
            System.err.println("Nimbus L&F not found. Using default.");
        }

        this.db = db;

        setTitle("⚡ Monthly Bill Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Slightly increased height for better spacing
        setLocationRelativeTo(null);
        // Use a more generous layout for the main frame
        setLayout(new BorderLayout(15, 15));

        // Apply a subtle background color
        Color primaryBg = new Color(240, 248, 255); // Alice Blue
        getContentPane().setBackground(primaryBg);

        // NORTH title (Refined Styling)
        JLabel title = new JLabel("Monthly Bill Calculator", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28)); // Larger, more prominent font
        Color primaryColor = new Color(0, 102, 204); // Deep Blue
        title.setForeground(primaryColor);

        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
        north.setBackground(primaryBg);
        north.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding for the title
        north.add(title);
        add(north, BorderLayout.NORTH);

        // CENTER content panel
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE); // White background for content area
        mainContent.setBorder(new EmptyBorder(10, 10, 10, 10)); // Inner padding
        add(mainContent, BorderLayout.CENTER);

        // SOUTH footer (Refined Styling)
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        south.setBackground(primaryBg);
        JButton exitBtn = new JButton("Exit Application");
        styleButton(exitBtn, new Color(204, 51, 0)); // Red-orange for exit

        exitBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Exit application?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (c == JOptionPane.YES_OPTION) System.exit(0);
        });
        south.add(exitBtn);
        add(south, BorderLayout.SOUTH);

        // load homes from DB and show home list
        loadHomesAndShow();
        setVisible(true);
    }

    // Helper to apply professional style to buttons
    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(8, 15, 8, 15) // Padding
        ));
    }

    // load homes from DB (with try/catch here) and show Home List view
    private void loadHomesAndShow() {
        try {
            homes = db.getAllHomes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load homes:\n" + e.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
            homes = new java.util.ArrayList<>();
        }
        showHomeList();
    }

    // show main menu (if needed) - not used, but could be added
    private void showMainMenu() {
        mainContent.removeAll();
        JPanel menu = new JPanel(new GridLayout(2, 1, 30, 30));
        menu.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        menu.setBackground(Color.WHITE);

        JButton manageHomes = new JButton("🏠 Manage Homes");
        styleButton(manageHomes, new Color(51, 153, 51)); // Green

        JButton settings = new JButton("⚙ Settings");
        styleButton(settings, new Color(255, 153, 0)); // Orange

        menu.add(wrapCenter(manageHomes));
        menu.add(wrapCenter(settings));

        manageHomes.addActionListener(e -> loadHomesAndShow());
        settings.addActionListener(e -> showSettings());

        mainContent.add(menu, BorderLayout.CENTER);
        refreshUI();
    }

    // HOME LIST screen: scrollable buttons for homes; fixed-size buttons
    private void showHomeList() {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // Section Title
        JLabel lbl = new JLabel("🏡 Your Homes", SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(lbl, BorderLayout.NORTH);

        // Container for buttons (vertical)
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS)); // Use BoxLayout for better vertical stacking
        listContainer.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY.brighter()));

        // Populate with homes from memory (loaded from DB)
        listContainer.removeAll();
        if (homes.isEmpty()) {
            listContainer.add(wrapCenter(new JLabel("No homes added yet. Use the 'Add Home' button below.")));
            listContainer.add(Box.createVerticalStrut(200)); // Add space to fill container
        } else {
            for (Home h : homes) {
                JButton btn = new JButton("<html><div style='text-align:left; padding: 5px 10px; font-size:14px;'>" + h.getName() + "</div></html>");
                btn.setAlignmentX(Component.LEFT_ALIGNMENT);
                btn.setMaximumSize(new Dimension(700, 50)); // Set max size to control height
                btn.setPreferredSize(new Dimension(700, 50));

                // Styling home buttons
                btn.setBackground(new Color(230, 240, 250)); // Light blue
                btn.setForeground(Color.BLACK);
                btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(153, 204, 255), 1),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                btn.addActionListener(ev -> showHomeDetail(h));

                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
                wrapper.setBackground(Color.WHITE);
                wrapper.add(btn);
                wrapper.setMaximumSize(new Dimension(800, 60)); // Control wrapper size
                listContainer.add(wrapper);
                listContainer.add(Box.createVerticalStrut(8)); // Spacing between buttons
            }
        }

        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(Color.WHITE);
        JButton addHome = new JButton("➕ Add New Home");
        styleButton(addHome, new Color(51, 153, 51)); // Green for primary action

        JButton back = new JButton("Back to Menu");
        styleButton(back, new Color(102, 102, 102)); // Gray for secondary action

        bottom.add(addHome);
        // Using showMainMenu instead of back to make sure it's linked correctly
        // bottom.add(back);

        panel.add(bottom, BorderLayout.SOUTH);

        addHome.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter a descriptive name for the new home:", "Add Home", JOptionPane.PLAIN_MESSAGE);
            if (name == null) return;
            name = name.trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Home name required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // insert into DB and reload list
            try {
                int newId = db.insertHome(name); // may throw
                if (newId > 0) {
                    Home newHome = new Home(newId, name);
                    homes.add(newHome);
                    showHomeList(); // refresh UI
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to add home:\n" + ex.getMessage(),
                        "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // back.addActionListener(e -> showMainMenu()); // Uncomment if you use the main menu
        // For now, back is removed as main menu isn't the primary flow

        mainContent.add(panel, BorderLayout.CENTER);
        refreshUI();
    }

    // HOME DETAIL screen: shows devices for a given home
    private void showHomeDetail(Home home) {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // Section Title
        JLabel lbl = new JLabel("Home: " + home.getName() + " Devices", SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(lbl, BorderLayout.NORTH);

        // devices container
        JPanel deviceList = new JPanel();
        deviceList.setLayout(new BoxLayout(deviceList, BoxLayout.Y_AXIS));
        deviceList.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(deviceList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY.brighter()));

        // load devices from DB for this home
        try {
            java.util.List<Device> devs = db.getDevicesByHomeId(home.getId());
            home.setDevices(devs);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load devices:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }

        deviceList.removeAll();
        if (home.getDevices().isEmpty()) {
            deviceList.add(wrapCenter(new JLabel("No devices added to this home.")));
            deviceList.add(Box.createVerticalStrut(200));
        } else {
            for (Device d : home.getDevices()) {
                // Formatting device display to be cleaner
                String deviceText = String.format("<html><div style='padding: 5px 10px;'><b>%s</b> &nbsp;|&nbsp; Power: %.0f W &nbsp;|&nbsp; Usage: %.1f h/day &nbsp;|&nbsp; Monthly kWh: <b>%.2f</b></div></html>",
                        d.getName(), d.getWatts(), d.getHoursPerDay(), d.monthlyKwh());
                JButton dbtn = new JButton(deviceText);
                dbtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                dbtn.setMaximumSize(new Dimension(700, 50));
                dbtn.setPreferredSize(new Dimension(700, 50));

                dbtn.setBackground(new Color(250, 250, 250)); // Off-white
                dbtn.setForeground(new Color(51, 51, 51));
                dbtn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                        new EmptyBorder(5, 10, 5, 10)
                ));

                // No action for device button currently, but one could be added

                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
                wrapper.setBackground(Color.WHITE);
                wrapper.add(dbtn);
                wrapper.setMaximumSize(new Dimension(800, 60));
                deviceList.add(wrapper);
                deviceList.add(Box.createVerticalStrut(8));
            }
        }

        panel.add(scroll, BorderLayout.CENTER);

        // Calculation summary section
        double totalKwh = home.getDevices().stream().mapToDouble(Device::monthlyKwh).sum();
        double bill = totalKwh * ratePerUnit;

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        summaryPanel.setBackground(new Color(245, 255, 245)); // Very light green
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(153, 204, 153), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        JLabel summaryLabel = new JLabel(String.format("Total Monthly Consumption: %.2f kWh  |  Estimated Monthly Bill (@Rs. %.2f): 💸 Rs. %.2f", totalKwh, ratePerUnit, bill));
        summaryLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        summaryLabel.setForeground(new Color(0, 102, 0)); // Dark green text
        summaryPanel.add(summaryLabel);

        panel.add(summaryPanel, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBackground(Color.WHITE);
        JButton addDevice = new JButton("➕ Add Device");
        styleButton(addDevice, new Color(0, 102, 204)); // Blue

        JButton calc = new JButton("Calculate Bill"); // Redundant now, but kept the logic in the action listener
        styleButton(calc, new Color(255, 153, 0)); // Orange

        JButton back = new JButton("🔙 Back to Homes");
        styleButton(back, new Color(102, 102, 102));

        // Use a box layout to align buttons better
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(addDevice);
        // buttonRow.add(calc); // Remove Calc button since summary is visible
        buttonRow.add(back);

        panel.add(buttonRow, BorderLayout.SOUTH); // Moved to a separate row for better spacing

        addDevice.addActionListener(e -> {
            JTextField nameF = new JTextField(15);
            JTextField wattsF = new JTextField(15);
            JTextField hoursF = new JTextField(15);

            // Use Box layout for a cleaner form dialog
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.add(new JLabel("Device name:"));
            formPanel.add(nameF);
            formPanel.add(Box.createVerticalStrut(10)); // Spacer
            formPanel.add(new JLabel("Power (Watts):"));
            formPanel.add(wattsF);
            formPanel.add(Box.createVerticalStrut(10));
            formPanel.add(new JLabel("Hours used per day:"));
            formPanel.add(hoursF);

            int res = JOptionPane.showConfirmDialog(this, formPanel, "Add New Device", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                String dname = nameF.getText().trim();
                String wattsS = wattsF.getText().trim();
                String hoursS = hoursF.getText().trim();
                if (dname.isEmpty() || wattsS.isEmpty() || hoursS.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double watts, hours;
                try {
                    watts = Double.parseDouble(wattsS);
                    hours = Double.parseDouble(hoursS);
                    if (watts <= 0 || hours < 0 || hours > 24) {
                        JOptionPane.showMessageDialog(this, "Watts must be positive. Hours must be between 0 and 24.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Numeric fields invalid.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int newId = db.insertDevice(home.getId(), dname, watts, hours);
                    if (newId > 0) {
                        // Reload devices and refresh view
                        java.util.List<Device> devs = db.getDevicesByHomeId(home.getId());
                        home.setDevices(devs);
                        showHomeDetail(home);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to add device:\n" + ex.getMessage(),
                            "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Simplified calculation action: just shows the same message as the summary is already on screen
        calc.addActionListener(e -> {
            // Already calculated, just show the message
            String msg = String.format("Estimated monthly consumption: %.2f kWh\nEstimated monthly bill: Rs. %.2f",
                    totalKwh, bill);
            JOptionPane.showMessageDialog(this, msg, "Monthly Bill Summary", JOptionPane.INFORMATION_MESSAGE);
        });


        back.addActionListener(e -> showHomeList());

        mainContent.add(panel, BorderLayout.CENTER);
        refreshUI();
    }

    // Settings view: edit rate per unit
    private void showSettings() {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("⚙ Settings", SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(lbl, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        center.setBackground(Color.WHITE);

        center.add(new JLabel("<html><div style='font-size:14px;'>Current Rate per kWh (Rs.):</div></html>"));
        JTextField rateField = new JTextField(String.valueOf(ratePerUnit), 10);
        rateField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        center.add(rateField);
        panel.add(wrapCenter(center), BorderLayout.CENTER); // Wrap center to center the setting input

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.setBackground(Color.WHITE);
        JButton save = new JButton("💾 Save Rate");
        styleButton(save, new Color(51, 153, 51)); // Green for save

        JButton back = new JButton("🔙 Back to Homes");
        styleButton(back, new Color(102, 102, 102)); // Gray

        bottom.add(save);
        bottom.add(back);
        panel.add(bottom, BorderLayout.SOUTH);

        save.addActionListener(e -> {
            String t = rateField.getText().trim();
            try {
                double r = Double.parseDouble(t);
                if (r <= 0) {
                    JOptionPane.showMessageDialog(this, "Rate must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ratePerUnit = r;
                JOptionPane.showMessageDialog(this, "Rate per kWh updated successfully to Rs. " + String.format("%.2f", r), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        back.addActionListener(e -> showHomeList());

        mainContent.add(panel, BorderLayout.CENTER);
        refreshUI();
    }

    // helpers
    private JPanel wrapCenter(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(Color.WHITE);
        p.add(comp);
        return p;
    }

    // This helper is no longer strictly needed with the new button alignment strategy, but kept for completeness
    private JPanel wrapLeftButton(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.WHITE);
        p.add(btn);
        return p;
    }

    private void refreshUI() {
        mainContent.revalidate();
        mainContent.repaint();
    }
}