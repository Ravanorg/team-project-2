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
import java.sql.Statement;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        generateDatabase();
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/ManageTeacher.fxml")).load()));
        primaryStage.setTitle("Manage Teachers");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    private void generateDatabase(){
        try {Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            InputStream resourceAsStream = getClass().getResourceAsStream("/schema.sql");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            statement.execute(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to generate tables").showAndWait();
        }

    }
}
