package com.se.philips.AdminDashboard.CoursetoTeacher;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;

public class CourseToTeacherController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private RadioButton beginnerRadio;
    @FXML private RadioButton intermediateRadio;
    @FXML private RadioButton advancedRadio;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField tagsField;
    @FXML private ImageView thumbnailPreview;
    @FXML private ToggleGroup difficultyGroup;

    private String selectedThumbnailPath = null;

    @FXML
    public void initialize() {
        // Create and assign the ToggleGroup
        difficultyGroup = new ToggleGroup();
        beginnerRadio.setToggleGroup(difficultyGroup);
        intermediateRadio.setToggleGroup(difficultyGroup);
        advancedRadio.setToggleGroup(difficultyGroup);

        categoryComboBox.getItems().addAll("Math", "Science", "History", "Programming");
    }


    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Thumbnail");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Copy to internal directory
                Path destination = Paths.get("thumbnails", selectedFile.getName());
                Files.createDirectories(destination.getParent());
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                selectedThumbnailPath = destination.toString();
                thumbnailPreview.setImage(new Image(destination.toUri().toString()));

            } catch (IOException e) {
                showAlert("Error uploading image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSaveCourse() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String difficulty = beginnerRadio.isSelected() ? "Beginner" :
                intermediateRadio.isSelected() ? "Intermediate" :
                        advancedRadio.isSelected() ? "Advanced" : "";
        String category = categoryComboBox.getValue();
        String tags = tagsField.getText();

        if (title.isEmpty() || difficulty.isEmpty() || category == null || selectedThumbnailPath == null) {
            showAlert("Please fill all required fields and upload thumbnail.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "INSERT INTO courses(title, description, difficulty, category, tags, thumbnail_path) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, difficulty);
            stmt.setString(4, category);
            stmt.setString(5, tags);
            stmt.setString(6, selectedThumbnailPath);
            stmt.executeUpdate();

            showAlert("Course saved successfully!");
        } catch (SQLException e) {
            showAlert("Database error: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
