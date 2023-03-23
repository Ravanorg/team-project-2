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

    public static void main(String[] args) throws SQLException {
        launch(args);
        DBConnection.getInstance().getConnection().close();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        generateTables();
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/DashBoard.fxml")).load()));
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Manage Different Entities");

    }

    private void generateTables() {
        var connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            InputStream is = getClass().getResourceAsStream("/schema.sql");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");

            }
            br.close();
            System.out.println(sb.toString());
            stm.executeUpdate(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to read schema script");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to find schema script");
        }

    }
}
