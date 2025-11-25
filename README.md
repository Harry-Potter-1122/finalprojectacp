**Overview**

This project is a basic Java Swing application designed to approximate the monthly electricity bill of a home based on its devices. Users can add multiple homes, enter each home individually, add devices with their wattage and daily usage, and calculate the monthly electricity cost.

**Description**

This project is a basic Java Swing application that allows users to add homes, add devices inside those homes, and view a simple monthly electricity bill approximation. The interface is built using beginner-level layouts such as FlowLayout, GridLayout, and BorderLayout without any advanced UI frameworks.

Each home and device appears as a fixed-size button, and all lists are scrollable when items exceed the available screen space. The application saves all homes and devices locally in a JSON file so that the data remains available even after the program is closed and reopened. No external database tools are required.

This project demonstrates simple GUI structure, basic event handling, data persistence using JSON, and a straightforward workflow: main screen → add home → enter home → add device → calculate monthly bill.

**Features**

- Add and view homes
- Add and view devices inside each home
- Automatic monthly bill calculation
- Scrollable lists for homes and devices
- SQLite database for saving data between sessions
- Basic and beginner-friendly UI structure

**Technologies Used**

- Java 
- Swing (FlowLayout, BorderLayout, GridLayout)
- SQLite (via JDBC)

**Project Structure**

- MainApp.java – Entry point of the application
- Screen.java – Handles main UI screens (Home Screen, Device Screen, Settings)
- Home.java – Model for a home
- Device.java – Model for a device
- Database.java – Handles SQLite setup, saving, and loading

**How to Run**

- Add sqlite-jdbc.jar to project dependencies
- Build and run MainApp.java
- The database file will be created automatically in the project folder

**Data Storing**

- Homes and devices are stored using SQLite
- All entries load automatically when the application restarts
