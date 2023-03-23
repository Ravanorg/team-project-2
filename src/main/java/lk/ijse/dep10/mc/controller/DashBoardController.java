package lk.ijse.dep10.mc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardController {

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnEmployees;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btnTeachers;

    @FXML
    void btnCustomersOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/ManageCustomers.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
        stage.setTitle("Manage Customers");
    }

    @FXML
    void btnEmployeesOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/ManageEmployees.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
        stage.setTitle("Manage Employees");

    }

    @FXML
    void btnStudentsOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/StudentView.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
        stage.setTitle("Manage Students");

    }

    @FXML
    void btnTeachersOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/ManageTeachers.fxml")).load()));
        stage.show();
        stage.centerOnScreen();
        stage.setTitle("Manage teachers");

    }

}
