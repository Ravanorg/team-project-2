package lk.ijse.dep10.mc.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.mc.db.DBConnection;
import lk.ijse.dep10.mc.model.Student;

import java.sql.*;

public class StudentViewController {

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<Student> tblStudents;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;

    public void initialize() {
        Platform.runLater(btnNew::fire);
        btnDelete.setDisable(true);
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        loadStudents();

        tblStudents.getSelectionModel().selectedItemProperty().addListener((ov,previous,current)->{
            btnDelete.setDisable(current==null);
            if (current==null)return;
            txtID.setText(current.getId());
            txtName.setText(current.getName());
            txtAddress.setText(current.getAddress());
        });
    }

    private void loadStudents() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Student");
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                Student student = new Student(id,name,address);
                tblStudents.getItems().add(student);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load students").showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Student selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent==null)return;
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM Student WHERE id=?");
            stm.setString(1, selectedStudent.getId());
            stm.executeUpdate();
            tblStudents.getItems().remove(selectedStudent);
            btnNew.fire();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the selected student").showAndWait();
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnNewOnAction(ActionEvent event) {
        txtName.clear();
        txtAddress.clear();
        txtName.requestFocus();
        tblStudents.getSelectionModel().clearSelection();
        generateId();

    }

    private void generateId() {
        if (tblStudents.getItems().size()==0) txtID.setText("S001");
        else {
            int id = Integer.parseInt(tblStudents.getItems().get(tblStudents.getItems().size()-1).getId().substring(1))+1;
            txtID.setText(String.format("S%03d",id));
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!dataValid()) return;
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Student VALUES (?,?,?)");
            stm.setString(1,txtID.getText());
            stm.setString(2,txtName.getText());
            stm.setString(3,txtAddress.getText());
            stm.executeUpdate();
            Student student = new Student(txtID.getText(), txtName.getText(), txtAddress.getText());
            tblStudents.getItems().add(student);
            btnNew.fire();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add the student").showAndWait();
            throw new RuntimeException(e);
        }

    }

    private boolean dataValid() {
        boolean isValid = true;
        if (!txtAddress.getText().matches("[A-z]{3,}")){
            txtAddress.selectAll();
            txtAddress.requestFocus();
            isValid=false;
        }
        if (!txtName.getText().matches("[A-z]{3,}")){
            txtName.selectAll();
            txtName.requestFocus();
            isValid=false;
        }
        return isValid;
    }

}
