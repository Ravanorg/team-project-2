package lk.ijse.dep10.mc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.dep10.mc.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        generateTables();
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/StudentView.fxml")).load()));
        primaryStage.setTitle("Manage Students");
        primaryStage.show();
        primaryStage.centerOnScreen();

    }

    private void generateTables() {
        Connection connection = DBConnection.getDbConnection().getConnection();
        try {
            Statement stm = connection.createStatement();
            InputStream is = getClass().getResourceAsStream("/schema.sql");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuilder dbScript = new StringBuilder();
            while ((line=br.readLine())!=null){
                dbScript.append(line).append("\n");
            }
            br.close();
            stm.execute(dbScript.toString());

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to create database").showAndWait();
            throw new RuntimeException(e);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to read the schema.sql file").showAndWait();
            throw new RuntimeException(e);
        }
    }
}
