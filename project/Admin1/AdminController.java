package com.se.philips.Admin1;

import com.se.philips.User;
import com.se.philips.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminController {
    private UserService userService = new UserService();
    private User selectedUser;

    @FXML private TableView<User> usersTable;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;

    @FXML
    public void initialize() {
        // Initialize role combo box
        roleComboBox.getItems().addAll("ADMIN", "USER", "EDITOR");

        // Set up table selection listener
        usersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedUser = newSelection;
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                });

        // Load initial data
        refreshTable();
    }

    private void refreshTable() {
        ObservableList<User> users = FXCollections.observableArrayList(
                userService.getAllUsers()
        );
        usersTable.setItems(users);
    }

    private void populateForm(User user) {
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        roleComboBox.setValue(user.getRole());
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        usersTable.getSelectionModel().clearSelection();
        selectedUser = null;
    }

    private User getFormData() {
        User user = new User();
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());
        user.setRole(roleComboBox.getValue());
        return user;
    }

    @FXML
    private void handleRefresh() {
        refreshTable();
        clearForm();
    }

    @FXML
    private void handleAdd() {
        User newUser = getFormData();
        if (newUser.getName() == null || newUser.getName().isEmpty() ||
                newUser.getEmail() == null || newUser.getEmail().isEmpty() ||
                newUser.getRole() == null) {
            showAlert("Error", "All fields are required!");
            return;
        }

        userService.createUser(newUser);
        refreshTable();
        clearForm();
        showAlert("Success", "User added successfully!");
    }

    @FXML
    private void handleUpdate() {
        if (selectedUser == null) {
            showAlert("Error", "No user selected!");
            return;
        }

        User updatedUser = getFormData();
        updatedUser.setId(selectedUser.getId());

        if (userService.updateUser(updatedUser)) {
            refreshTable();
            clearForm();
            showAlert("Success", "User updated successfully!");
        } else {
            showAlert("Error", "Failed to update user!");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedUser == null) {
            showAlert("Error", "No user selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete " + selectedUser.getName() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (userService.deleteUser(selectedUser.getId())) {
                refreshTable();
                clearForm();
                showAlert("Success", "User deleted successfully!");
            } else {
                showAlert("Error", "Failed to delete user!");
            }
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}