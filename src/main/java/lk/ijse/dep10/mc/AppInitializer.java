package lk.ijse.dep10.mc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.dep10.mc.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        generateAllTables();
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Employee.fxml"))));
        primaryStage.setTitle("Manage Employee");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void generateAllTables() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/schema.sql")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ( (line = bufferedReader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            Statement statement = connection.createStatement();
            statement.execute(stringBuilder.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
