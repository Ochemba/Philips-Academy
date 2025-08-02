package com.se.philips.AdminDashboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;

public class AdminProfileController {

    @FXML private TextField nameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> genderBox;
    @FXML private DatePicker dobPicker;
    @FXML private ImageView profileImageView;

    private File selectedImageFile;

    @FXML
    public void initialize() {
        genderBox.getItems().addAll("Male", "Female", "Other");
        loadProfileFromFile();

        // Make profile image circular
        profileImageView.setPreserveRatio(true);
        profileImageView.setFitWidth(220);
        profileImageView.setFitHeight(220);

        Circle clip = new Circle(60, 60, 60); // centerX, centerY, radius
        profileImageView.setClip(clip);
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            profileImageView.setImage(image);
        }
    }

    @FXML
    private void handleSaveChanges() {
        String url = "jdbc:sqlite:MainDB.db";
        String sql = "INSERT INTO Profile (nameField, passwordField, phoneField, genderBox, dobpicker, icon_location) VALUES (?, ?, ?, ?, ?, ?)";
        String name = nameField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();
        String gender = genderBox.getValue();
        LocalDate dob = dobPicker.getValue();
        String location = selectedImageFile.toURI().toString();

        if (name.isEmpty() || password.isEmpty() || phone.isEmpty() || gender == null || dob == null) {
            showStyledAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, phone);
            pstmt.setString(4, gender);
            pstmt.setString(5, dob.toString());
            pstmt.setString(6, location);

            pstmt.executeUpdate();
            System.out.println("Profile Changed!");
            nameField.clear();
            passwordField.clear();
            phoneField.clear();
            loadProfileFromFile();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


        showStyledAlert(Alert.AlertType.INFORMATION, "Changes saved successfully!");
    }



    private void saveToFile(AdminProfile profile) {
        try (PrintWriter writer = new PrintWriter("admin_profile.txt")) {
            writer.println(profile.getName());
            writer.println(profile.getPassword());
            writer.println(profile.getPhone());
            writer.println(profile.getGender());
            writer.println(profile.getDob());
            writer.println(profile.getImagePath() != null ? profile.getImagePath() : "none");
        } catch (IOException e) {
            showStyledAlert(Alert.AlertType.ERROR, "Failed to save profile");
        }
    }

    private void loadProfileFromFile() {
        String url = "jdbc:sqlite:MainDB.db";
        String sql = "SELECT * FROM Profile";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("nameField");
                String password = rs.getString("passwordField");
                String phone = rs.getString("phoneField");
                String gender = rs.getString("genderBox");
                String dob = rs.getString("dobpicker");
                String icon_location = rs.getString("icon_location");
                nameField.setText(name);
                passwordField.setText(password);
                phoneField.setText(phone);
                genderBox.setValue(gender);
                dobPicker.setValue(LocalDate.parse(dob));
                String imagePath = icon_location;

                if (!"none".equals(imagePath)) {
                    selectedImageFile = new File(imagePath);
                    if (selectedImageFile.exists()) {
                        profileImageView.setImage(new Image(selectedImageFile.toURI().toString()));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    private void showStyledAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Profile Update");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/se/philips/css/dialog-styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }
}
