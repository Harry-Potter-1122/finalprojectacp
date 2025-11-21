import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Database db = new Database(); // may throw Exception
                new Screen(db);               // GUI that uses db
            } catch (Exception e) {
                // show simple error and exit
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Failed to initialize database:\n" + e.getMessage(),
                        "Startup Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
