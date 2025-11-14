import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Screen {

    // ---------- MAIN HOME SCREEN ----------
    public static class HomeScreen extends JFrame {

        ArrayList<Home> homes = new ArrayList<>();
        JPanel listPanel;

        public HomeScreen() {
            setTitle("Homes");
            setSize(350, 400);
            setLayout(new BorderLayout());

            listPanel = new JPanel();
            listPanel.setLayout(new GridLayout(0, 1));

            JButton addHomeBtn = new JButton("Add Home");
            addHomeBtn.addActionListener(e -> new AddHomeScreen(this));

            add(addHomeBtn, BorderLayout.NORTH);
            add(new JScrollPane(listPanel), BorderLayout.CENTER);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

        public void refreshList() {
            listPanel.removeAll();

            for (Home home : homes) {
                JButton btn = new JButton(home.getName());
                btn.addActionListener(e -> new HomeDetailScreen(home));
                listPanel.add(btn);
            }

            listPanel.revalidate();
            listPanel.repaint();
        }
    }

    // ---------- ADD HOME SCREEN ----------
    public static class AddHomeScreen extends JFrame {
        public AddHomeScreen(HomeScreen parent) {
            setTitle("Add Home");
            setSize(300, 150);
            setLayout(new FlowLayout());

            JLabel label = new JLabel("Home Name:");
            JTextField text = new JTextField(15);
            JButton save = new JButton("Save");

            save.addActionListener(e -> {
                parent.homes.add(new Home(text.getText()));
                parent.refreshList();
                dispose();
            });

            add(label);
            add(text);
            add(save);

            setVisible(true);
        }
    }

    // ---------- HOME DETAIL SCREEN ----------
    public static class HomeDetailScreen extends JFrame {

        ArrayList<Device> devices = new ArrayList<>();
        JPanel devicePanel;
        Home home;

        public HomeDetailScreen(Home home) {
            this.home = home;

            setTitle("Home: " + home.getName());
            setSize(350, 400);
            setLayout(new BorderLayout());

            JButton addDeviceBtn = new JButton("Add Device");
            addDeviceBtn.addActionListener(e -> new AddDeviceScreen(this));

            JButton calcBtn = new JButton("Calculate Monthly Bill");
            calcBtn.addActionListener(e -> showBill());

            JPanel topPanel = new JPanel(new FlowLayout());
            topPanel.add(addDeviceBtn);
            topPanel.add(calcBtn);

            devicePanel = new JPanel();
            devicePanel.setLayout(new GridLayout(0, 1));

            add(topPanel, BorderLayout.NORTH);
            add(new JScrollPane(devicePanel), BorderLayout.CENTER);

            setVisible(true);
        }

        public void refreshDevices() {
            devicePanel.removeAll();
            for (Device d : devices) {
                devicePanel.add(new JLabel(d.getName()));
            }
            devicePanel.revalidate();
            devicePanel.repaint();
        }

        public void showBill() {
            double total = 0;
            for (Device d : devices) total += d.getMonthlyBill();

            JOptionPane.showMessageDialog(this,
                    "Total Monthly Bill: " + total + " PKR",
                    "Bill",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------- ADD DEVICE SCREEN ----------
    public static class AddDeviceScreen extends JFrame {
        public AddDeviceScreen(HomeDetailScreen parent) {
            setTitle("Add Device");
            setSize(300, 200);
            setLayout(new GridLayout(4, 2));

            JLabel nameL = new JLabel("Device Name:");
            JTextField name = new JTextField();

            JLabel wattL = new JLabel("Watts:");
            JTextField watt = new JTextField();

            JLabel hourL = new JLabel("Hours/day:");
            JTextField hours = new JTextField();

            JButton save = new JButton("Save");
            save.addActionListener(e -> {
                parent.devices.add(new Device(
                        name.getText(),
                        Double.parseDouble(watt.getText()),
                        Double.parseDouble(hours.getText())
                ));
                parent.refreshDevices();
                dispose();
            });

            add(nameL); add(name);
            add(wattL); add(watt);
            add(hourL); add(hours);
            add(new JLabel()); add(save);

            setVisible(true);
        }
    }
}
