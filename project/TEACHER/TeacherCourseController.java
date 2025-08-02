package com.se.philips.TEACHER;

import com.se.philips.DBConnect.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.sqlite.core.DB;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;


public class TeacherCourseController {

    @FXML
    private Accordion courseAccordion;

    private String teacherId;

    public void setTeacherId(String id) {
        this.teacherId = id;

        if (id == null || id.isEmpty()) {
            showAlert("Teacher ID not set. Please login first.");
            return;
        }

//        // Load courses now that the ID is available
//        loadCoursesByLevel("Freshmen");
//        loadCoursesByLevel("Sophomore");
//        loadCoursesByLevel("Junior");
//        loadCoursesByLevel("Senior");
    }

    public void initialize() {
        // Don't load courses here — wait until teacherId is set
        String teacherId = LoginSession.getTeacherId();
        if (teacherId != null) {
            displayCoursesForTeacher(teacherId);
        } else {
            System.out.println("No teacher ID found in session. Please log in.");
        }

    }

    private void loadCoursesByLevel(String level) {
        TitledPane pane = new TitledPane();
        pane.setText(level);

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10;");

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = """
    SELECT c.*
    FROM courses c
    JOIN course_teacher ct ON c.course_id = ct.course_id
    JOIN teachers t ON ct.teachers_id = t.teachers_id
    WHERE c.difficulty = ? AND t.staff_id = ?
""";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, level);
            pstmt.setString(2, "%" + teacherId + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String tags = rs.getString("tags");
                String thumbnailPath = rs.getString("thumbnail_path");
                String attachmentPath = rs.getString("attachment_path");

                Label titleLabel = new Label("Title: " + title);
                Label descriptionLabel = new Label("Description: " + description);
                Label categoryLabel = new Label("Category: " + category);
                Label tagsLabel = new Label("Tags: " + tags);

                ImageView thumbnailView = new ImageView();
                if (thumbnailPath != null && !thumbnailPath.isEmpty()) {
                    try {
                        Image image = new Image(new FileInputStream(thumbnailPath));
                        thumbnailView.setImage(image);
                        thumbnailView.setFitWidth(150);
                        thumbnailView.setFitHeight(100);
                        thumbnailView.setPreserveRatio(true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                VBox courseBox = new VBox(5);
                courseBox.getChildren().addAll(titleLabel, descriptionLabel, categoryLabel, tagsLabel, thumbnailView);
                courseBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
                vbox.getChildren().add(courseBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        pane.setContent(vbox);
        courseAccordion.getPanes().add(pane);
    }

    public void displayCoursesForTeacher(String staffId) {
        String sql = """
        SELECT c.*
        FROM courses c
        JOIN course_teacher ct ON c.course_id = ct.course_id
        JOIN teachers t ON ct.teachers_id = t.teachers_id
        WHERE t.staff_id = ?
    """;

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staffId);
            ResultSet rs = stmt.executeQuery();

            Map<String, List<VBox>> groupedCourses = new HashMap<>();

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String tags = rs.getString("tags");
                String thumbnail = rs.getString("thumbnail");
                String attachment = rs.getString("attachment");
                String difficulty = rs.getString("difficulty");
                String level = rs.getString("level");

                Label titleLabel = new Label("Title: " + title);
                Label descriptionLabel = new Label("Description: " + description);
                Label categoryLabel = new Label("Category: " + category);
                Label tagsLabel = new Label("Tags: " + tags);
                Label difficultyLabel = new Label("Difficulty: " + difficulty);

                ImageView thumbnailView = new ImageView();
                if (thumbnail != null && !thumbnail.isEmpty()) {
                    try {
                        Image image = new Image(new FileInputStream(thumbnail));
                        thumbnailView.setImage(image);
                        thumbnailView.setFitWidth(150);
                        thumbnailView.setPreserveRatio(true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Button openAttachmentBtn = new Button("Open Attachment");
                if (attachment != null && !attachment.isEmpty()) {
                    openAttachmentBtn.setOnAction(e -> {
                        try {
                            Desktop.getDesktop().open(new File(attachment));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                } else {
                    openAttachmentBtn.setDisable(true);
                    openAttachmentBtn.setText("No Attachment");
                }

                VBox courseBox = new VBox(5);
                courseBox.getChildren().addAll(
                        titleLabel,
                        descriptionLabel,
                        categoryLabel,
                        tagsLabel,
                        difficultyLabel,
                        thumbnailView,
                        openAttachmentBtn
                );
                courseBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

                groupedCourses.computeIfAbsent(level, k -> new ArrayList<>()).add(courseBox);
            }

            courseAccordion.getPanes().clear();

            for (Map.Entry<String, List<VBox>> entry : groupedCourses.entrySet()) {
                String level = entry.getKey();
                List<VBox> courseBoxes = entry.getValue();

                VBox levelBox = new VBox(10);
                levelBox.getChildren().addAll(courseBoxes);

                TitledPane levelPane = new TitledPane("Level: " + level, levelBox);
                courseAccordion.getPanes().add(levelPane);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
