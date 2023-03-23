package lk.ijse.dep10.mc.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static final DBConnection dbconnection = new DBConnection();
    private final Connection  connection;

    private DBConnection() {
        try {
            File file = new File("application.properties");
            var fileReader = new FileReader(file);
            Properties properties = new Properties();
            properties.load(fileReader);
            fileReader.close();

            var localhost = properties.getProperty("mysql.host", "localhost");
            var port = properties.getProperty("mysql.port", "3306");
            var database = properties.getProperty("mysql.database", "dep10_git");
            var username = properties.getProperty("mysql.username", "root");
            var password = properties.getProperty("mysql.password", "rasiya");


            String url = "jdbc:mysql://" + localhost + ":" + port + "/" + database + "?createDatabaseIfNotExist=true&allowMultiQueries=true";
            connection = DriverManager.getConnection(url, username, password);
            var statement = connection.createStatement();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to create a Database Connection").showAndWait();
            System.exit(1);
            throw new RuntimeException(e);
        }

    }

    public static DBConnection getInstance() {
        return dbconnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
