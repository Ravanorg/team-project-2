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
import lk.ijse.dep10.mc.model.Customer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageCustomersController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewCustomer;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize() {
        txtName.getStyleClass().remove("invalid");
        txtAddress.getStyleClass().remove("invalid");

        Platform.runLater(btnNewCustomer::fire);
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        loadAllCustomers();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, t1) -> {
            if (t1 == null) return;
            btnDelete.setDisable(false);
            txtId.setText(t1.getId());
            txtName.setText(t1.getName());
            txtAddress.setText(t1.getAddress());

        });

    }

    private void loadAllCustomers() {
        var connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from Customer");
            var resultSet = stm.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");

                var customer = new Customer(id, name, address);
                tblCustomers.getItems().add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load the Customers");
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        var connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("DELETE  from Customer where id=?");
            stm.setString(1, txtId.getText());
            stm.executeUpdate();

            tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
            btnNewCustomer.fire();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "failed too delete Customer");
        }


    }

    @FXML
    void btnNewCustomerOnAction(ActionEvent event) {
        txtName.getStyleClass().remove("invalid");
        txtAddress.getStyleClass().remove("invalid");
        btnDelete.setDisable(true);
        txtName.clear();
        txtAddress.clear();
        tblCustomers.getSelectionModel().clearSelection();
        generateId();

    }

    private void generateId() {
        if (tblCustomers.getItems().size() == 0) txtId.setText("C001");
        else {
            var id = Integer.parseInt(tblCustomers.getItems().get(tblCustomers.getItems().size() - 1).getId().substring(1)) + 1;
            txtId.setText(String.format("C%03d", id));
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;
        var connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Customer(id, name, address) values (?,?,?)");
            stm.setString(1, txtId.getText());
            stm.setString(2, txtName.getText());
            stm.setString(3, txtAddress.getText());
            stm.executeUpdate();

            var customer = new Customer(txtId.getText(), txtName.getText(), txtAddress.getText());
            tblCustomers.getItems().add(customer);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to add data to database");
        }
        btnNewCustomer.fire();

    }

    private boolean isDataValid() {
        boolean dataValid = true;
        if (!txtAddress.getText().matches("[A-za-z0-9 ,-]+")) {
            txtAddress.requestFocus();
            txtAddress.selectAll();
            txtAddress.getStyleClass().add("invalid");
            dataValid = false;
        }
        if (!txtName.getText().matches("[A-Za-z ]+")) {
            txtName.requestFocus();
            txtName.selectAll();
            txtName.getStyleClass().add("invalid");
            dataValid = false;
        }
        return dataValid;
    }

}
