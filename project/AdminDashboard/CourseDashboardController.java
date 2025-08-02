package com.se.philips.AdminDashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;

public class CourseDashboardController {

    @FXML private TextField titleField, categoryField, tagsField, thumbnailField, attachmentField;
    @FXML private TextArea descField;
    @FXML private ComboBox<String> difficultyBox;
    @FXML private ComboBox<String> levelBox;
    @FXML private ListView<String> teacherListView;
    @FXML private TableView<CourseDashboard> courseTable;
    @FXML private TableColumn<CourseDashboard, String> titleCol, categoryCol, difficultyCol,levelCol, actionCol;

    private final ObservableList<CourseDashboard> courseList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set difficulty options
        difficultyBox.setItems(FXCollections.observableArrayList("Beginner", "Intermediate", "Advanced"));
        levelBox.setItems(FXCollections.observableArrayList("freshmen", "sophomore", "junior","senior"));


        // Table columns
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        difficultyCol.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));

        // Optional: style difficulty column
        difficultyCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("beginner", "intermediate", "advanced");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    switch (item) {
                        case "Beginner" -> getStyleClass().add("beginner");
                        case "Intermediate" -> getStyleClass().add("intermediate");
                        case "Advanced" -> getStyleClass().add("advanced");
                    }
                }
            }
        });

        levelCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("freshmen", "sophomore", "junior", "senior");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    switch (item) {
                        case "freshmen" -> getStyleClass().add("freshmen");
                        case "sophomore" -> getStyleClass().add("sophomore");
                        case "junior" -> getStyleClass().add("junior");
                        case "senior" -> getStyleClass().add("senior");
                    }
                }
            }
        });

        // Style ComboBox dropdown items

        // Style selected item in ComboBox
        difficultyBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("beginner", "intermediate", "advanced");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    switch (item) {
                        case "Beginner" -> getStyleClass().add("beginner");
                        case "Intermediate" -> getStyleClass().add("intermediate");
                        case "Advanced" -> getStyleClass().add("advanced");
                    }
                }
            }
        });

        levelBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("freshmen", "sophomore", "junior", "senior");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    switch (item) {
                        case "freshmen" -> getStyleClass().add("freshmen");
                        case "sophomore" -> getStyleClass().add("sophomore");
                        case "junior" -> getStyleClass().add("junior");
                        case "senior" -> getStyleClass().add("senior");
                    }
                }
            }
        });

        // Action buttons in table
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(e -> editCourse(getIndex()));
                editBtn.setStyle("-fx-background-color: #F59E0B;");
                deleteBtn.setOnAction(e -> deleteCourse(getIndex()));
                deleteBtn.setStyle("-fx-background-color: #F08080;");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, editBtn, deleteBtn);
                    setGraphic(box);
                }
            }
        });

        teacherListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadTeachers();
        loadCourses();
    }

    private void loadTeachers() {
        ObservableList<String> teachers = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT staff_id FROM teachers")) {
            while (rs.next()) {
                teachers.add(rs.getString("staff_id"));
            }
            teacherListView.setItems(teachers);
        } catch (SQLException e) {
            showAlert("Failed to load teachers: " + e.getMessage());
        }
    }

    private void loadCourses() {
        courseList.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Courses")) {
            while (rs.next()) {
                courseList.add(new CourseDashboard(
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getString("difficulty"),
                        rs.getString("level")
                ));
            }
            courseTable.setItems(courseList);
        } catch (SQLException e) {
            showAlert("Failed to load courses: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveCourse() {
        String title = titleField.getText();
        String desc = descField.getText();
        String diff = difficultyBox.getValue();
        String lvl = levelBox.getValue();
        String cat = categoryField.getText();
        String tags = tagsField.getText();
        String thumbnail = thumbnailField.getText();
        String attachment = attachmentField.getText();

        if (title.isEmpty() || diff == null) {
            showAlert("Title and Difficulty are required.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String insertSQL = "INSERT INTO Courses (title, description, difficulty, level, category, tags, thumbnail, attachment) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement ps = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, diff);
            ps.setString(4, lvl);
            ps.setString(5, cat);
            ps.setString(6, tags);
            ps.setString(7, thumbnail);
            ps.setString(8, attachment);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int courseId = keys.next() ? keys.getInt(1) : -1;

            for (String teacherName : teacherListView.getSelectionModel().getSelectedItems()) {
                PreparedStatement teacherPS = conn.prepareStatement("SELECT teachers_id FROM teachers WHERE staff_id = ?");
                teacherPS.setString(1, teacherName);
                ResultSet trs = teacherPS.executeQuery();
                if (trs.next()) {
                    int teacherId = trs.getInt("teachers_id");

                    PreparedStatement assignPS = conn.prepareStatement("INSERT INTO course_teacher (course_id, teachers_id) VALUES (?, ?)");
                    assignPS.setInt(1, courseId);
                    assignPS.setInt(2, teacherId);
                    assignPS.executeUpdate();
                }
            }

            showAlert("Course created and assigned successfully!");
            loadCourses();
        } catch (SQLException e) {
            showAlert("Error saving course: " + e.getMessage());
        }
    }

    private void editCourse(int index) {
        CourseDashboard course = courseList.get(index);
        titleField.setText(course.getTitle());
        categoryField.setText(course.getCategory());
        difficultyBox.setValue(course.getDifficulty());
        levelBox.setValue(course.getLevel());
    }

    private void deleteCourse(int index) {
        CourseDashboard course = courseList.get(index);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Courses WHERE title = ?");
            ps.setString(1, course.getTitle());
            ps.executeUpdate();

            showAlert("Course deleted!");
            loadCourses();
        } catch (SQLException e) {
            showAlert("Failed to delete: " + e.getMessage());
        }
    }

    @FXML
    private void browseThumbnail() {
        File file = new FileChooser().showOpenDialog(new Stage());
        if (file != null) {
            thumbnailField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void browseAttachment() {
        File file = new FileChooser().showOpenDialog(new Stage());
        if (file != null) {
            attachmentField.setText(file.getAbsolutePath());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
