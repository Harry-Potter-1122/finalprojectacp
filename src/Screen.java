import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Screen extends JFrame {

    private final Database db;
    private JPanel mainContent;
    private double ratePerUnit = 30.0;

    // runtime list of homes loaded from DB
    private java.util.List<Home> homes;

    public Screen(Database db) {
        this.db = db;

        setTitle("Monthly Bill Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // NORTH title
        JLabel title = new JLabel("Monthly Bill Calculator", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
        north.add(title);
        add(north, BorderLayout.NORTH);

        // CENTER content panel
        mainContent = new JPanel(new BorderLayout());
        add(mainContent, BorderLayout.CENTER);

        // SOUTH footer
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Exit application?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) System.exit(0);
        });
        south.add(exitBtn);
        add(south, BorderLayout.SOUTH);

        // load homes from DB and show home list
        loadHomesAndShow();
        setVisible(true);
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
        JPanel menu = new JPanel(new GridLayout(2, 1, 20, 20));
        menu.setBorder(BorderFactory.createEmptyBorder(80, 200, 80, 200));
        JButton manageHomes = new JButton("Manage Homes");
        JButton settings = new JButton("Settings");
        menu.add(wrapCenter(manageHomes));
        menu.add(wrapCenter(settings));
        manageHomes.addActionListener(e -> showHomeList());
        settings.addActionListener(e -> showSettings());
        mainContent.add(menu, BorderLayout.CENTER);
        refreshUI();
    }

    // HOME LIST screen: scrollable buttons for homes; fixed-size buttons
    private void showHomeList() {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Homes", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        // container for buttons (vertical)
        JPanel listContainer = new JPanel(new GridLayout(0, 1, 8, 8));
        JScrollPane scroll = new JScrollPane(listContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(740, 360));

        // populate with homes from memory (loaded from DB)
        listContainer.removeAll();
        for (Home h : homes) {
            JButton btn = new JButton(h.getName());
            btn.setPreferredSize(new Dimension(700, 44)); // fixed size
            btn.addActionListener(ev -> showHomeDetail(h));
            listContainer.add(wrapLeftButton(btn));
        }

        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addHome = new JButton("Add Home");
        JButton back = new JButton("Back");
        bottom.add(addHome);
        bottom.add(back);
        panel.add(bottom, BorderLayout.SOUTH);

        addHome.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter home name:");
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

        back.addActionListener(e -> showMainMenu());

        mainContent.add(panel, BorderLayout.CENTER);
        refreshUI();
    }

    // HOME DETAIL screen: shows devices for a given home
    private void showHomeDetail(Home home) {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Home: " + home.getName(), SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        // devices container
        JPanel deviceList = new JPanel(new GridLayout(0, 1, 8, 8));
        JScrollPane scroll = new JScrollPane(deviceList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(740, 320));

        // load devices from DB for this home
        try {
            java.util.List<Device> devs = db.getDevicesByHomeId(home.getId());
            home.setDevices(devs);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load devices:\n" + ex.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
        }

        deviceList.removeAll();
        for (Device d : home.getDevices()) {
            JButton dbtn = new JButton(d.toString());
            dbtn.setPreferredSize(new Dimension(700, 42));
            deviceList.add(wrapLeftButton(dbtn));
        }

        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addDevice = new JButton("Add Device");
        JButton calc = new JButton("Calculate Monthly Bill");
        JButton back = new JButton("Back");
        bottom.add(addDevice);
        bottom.add(calc);
        bottom.add(back);
        panel.add(bottom, BorderLayout.SOUTH);

        addDevice.addActionListener(e -> {
            JTextField nameF = new JTextField();
            JTextField wattsF = new JTextField();
            JTextField hoursF = new JTextField();
            Object[] form = {
                    "Device name:", nameF,
                    "Watts:", wattsF,
                    "Hours per day:", hoursF
            };
            int res = JOptionPane.showConfirmDialog(this, form, "Add Device", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                String dname = nameF.getText().trim();
                String wattsS = wattsF.getText().trim();
                String hoursS = hoursF.getText().trim();
                if (dname.isEmpty() || wattsS.isEmpty() || hoursS.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double watts, hours;
                try {
                    watts = Double.parseDouble(wattsS);
                    hours = Double.parseDouble(hoursS);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Numeric fields invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int newId = db.insertDevice(home.getId(), dname, watts, hours);
                    if (newId > 0) {
                        // reload devices and refresh view
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

        calc.addActionListener(e -> {
            double totalKwh = 0.0;
            for (Device d : home.getDevices()) totalKwh += d.monthlyKwh();
            double bill = totalKwh * ratePerUnit;
            String msg = String.format("Estimated monthly consumption: %.2f kWh\nEstimated monthly bill: Rs. %.2f",
                    totalKwh, bill);
            JOptionPane.showMessageDialog(this, msg, "Monthly Bill", JOptionPane.INFORMATION_MESSAGE);
        });

        back.addActionListener(e -> showHomeList());

        mainContent.add(panel, BorderLayout.CENTER);
        refreshUI();
    }

    // Settings view: edit rate per unit
    private void showSettings() {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel("Settings", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        center.add(new JLabel("Rate per kWh (Rs):"));
        JTextField rateField = new JTextField(String.valueOf(ratePerUnit), 8);
        center.add(rateField);
        panel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        JButton back = new JButton("Back");
        bottom.add(save);
        bottom.add(back);
        panel.add(bottom, BorderLayout.SOUTH);

        save.addActionListener(e -> {
            String t = rateField.getText().trim();
            try {
                double r = Double.parseDouble(t);
                if (r <= 0) {
                    JOptionPane.showMessageDialog(this, "Rate must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ratePerUnit = r;
                JOptionPane.showMessageDialog(this, "Rate updated.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        back.addActionListener(e -> showHomeList());

        mainContent.add(panel, BorderLayout.CENTER);
        refreshUI();
    }

    // helpers
    private JPanel wrapCenter(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.add(comp);
        return p;
    }

    private JPanel wrapLeftButton(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(btn);
        return p;
    }

    private void refreshUI() {
        mainContent.revalidate();
        mainContent.repaint();
    }
}
