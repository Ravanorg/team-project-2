package lk.ijse.dep10.mc.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.mc.db.DBConnection;
import lk.ijse.dep10.mc.model.Teacher;

import java.sql.*;

public class ManageTeacherController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewTeacher;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Teacher> tblTeacher;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    int count=0;
    String idTeacher="T"+count;

    @FXML
    private TextField txtName;

    public void initialize() {
        tblTeacher.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblTeacher.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblTeacher.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        loadTeachers();
        txtId.setText(idTeacher);
        txtId.setDisable(true);

    }

    private void loadTeachers(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Teacher");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                tblTeacher.getItems().add(new Teacher(id, name, address));
                if (count<Integer.parseInt(id.substring(1))){
                    count = Integer.parseInt(id.substring(1));
                    System.out.println(count);
                }
            }
            count++;
            idTeacher=String.format("T%03d",count);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to load teachers to the table").showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnNewTeacherOnAction(ActionEvent event) {
        btnDelete.setDisable(true);
        tblTeacher.getSelectionModel().clearSelection();
        txtName.clear();
        txtAddress.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM Teacher WHERE id='%s'";
            sql = String.format(sql, tblTeacher.getSelectionModel().getSelectedItem().getId());
            statement.executeUpdate(sql);

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to add Teacher to databasee").showAndWait();
            throw new RuntimeException(e);
        }

        tblTeacher.getItems().remove(tblTeacher.getSelectionModel().getSelectedItem());

        btnNewTeacher.fire();


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        System.out.println("btv save");
        if (tblTeacher.getSelectionModel()!=null) {
            if (!isValid()) return;
            addToDatabase();
            addTOTable();
            count++;

        }else {

        }
        System.out.println("save"+count);
        idTeacher=String.format("T%03d",count);
        txtId.setText(idTeacher);
        btnNewTeacher.fire();
    }

    private void addToDatabase() {
        System.out.println("add database");
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Teacher(id, name, address) VALUES (?,?,?)");
            System.out.println("add database"+id+" "+name+" "+address);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,address);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to add Teacher to databasee").showAndWait();
            throw new RuntimeException(e);
        }
    }
    private void  addTOTable(){
        ObservableList<Teacher> teachersList = tblTeacher.getItems();
        teachersList.add(new Teacher(txtId.getText(),txtName.getText(),txtAddress.getText()));
        System.out.println("add new teacher to table");
    }

    private boolean isValid() {
        boolean flag = true;
        String name = txtName.getText();
        String address = txtAddress.getText();

        if (name.isEmpty() || !name.matches("^[A-z]+ ?[A-z]*")) {
            flag = false;
            txtName.requestFocus();
            txtName.selectAll();
        }
        if (name.isEmpty() || !address.matches("^[A-z]{5,}")) {
            flag = false;
            txtAddress.requestFocus();
            txtAddress.selectAll();
        }

        System.out.println("isvalid table return: "+flag);
        return flag;
    }

}
