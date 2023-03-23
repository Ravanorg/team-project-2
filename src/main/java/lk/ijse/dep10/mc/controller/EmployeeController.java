package lk.ijse.dep10.mc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.mc.db.DBConnection;

import java.sql.*;

public class EmployeeController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewEmployee;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Employee> tblEmployees;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    private Connection connection;

    public void initialize(){
        connection = DBConnection.getInstance().getConnection();

        btnSave.setDisable(true);
        txtId.setDisable(true);

        tblEmployees.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblEmployees.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblEmployees.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            btnDelete.setDisable(current == null);
            if (tblEmployees.getSelectionModel().getSelectedItem()==null) return;
            txtId.setText(String.valueOf(current.getId()));
            txtName.setText(current.getName());
            txtAddress.setText(current.getAddress());
        });

        String sql= "SELECT * FROM Employee ";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()){
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setAddress(resultSet.getString("address"));
                tblEmployees.getItems().add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Employee selectedItem = tblEmployees.getSelectionModel().getSelectedItem();


        try {
            String sql = String.format("DELETE FROM Employee WHERE id='%d'", selectedItem.getId());
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            tblEmployees.getItems().remove(selectedItem);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        btnSave.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateData()) return;

        try {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Employee(name, address) VALUES (?,?)");
            stm.setString(1,txtName.getText());
            stm.setString(2,txtAddress.getText());
            stm.executeUpdate();


            Employee selectedItem = tblEmployees.getSelectionModel().getSelectedItem();
            if (selectedItem!= null){
                Employee employee = new Employee();
//            employee.setId(txtId.getText());
                employee.setName(txtName.getText());
                employee.setAddress(txtAddress.getText());
                tblEmployees.getItems().remove(tblEmployees.getSelectionModel().getSelectedIndex());
                tblEmployees.getSelectionModel().clearSelection();
                tblEmployees.getItems().add(employee);
                return;
            }

            Employee employee = new Employee();
//            employee.setId(Integer.parseInt(txtId.getText().trim()));
            employee.setName(txtName.getText().trim());
            employee.setAddress(txtAddress.getText().trim());
            tblEmployees.getItems().add(employee);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean validateData() {
        boolean isValidate = true;
        if (txtName.getText().isBlank()){
            isValidate=false;
        }
        if (txtAddress.getText().isBlank()){
            isValidate=false;
        }
        return isValidate;
    }

}
