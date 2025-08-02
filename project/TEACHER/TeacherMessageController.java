package com.se.philips.TEACHER;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherMessageController {

    @FXML private TextField receiverField;
    @FXML private TextArea messageField;
    @FXML private VBox messageList;
    private File attachedFile;

    private final String teacherId = "T001"; // Replace with logged-in teacher ID

    @FXML
    public void initialize() {
        setupAutoComplete();
        loadMessages();
    }

    private void setupAutoComplete() {
        List<String> ids = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT matricId FROM students");
            while (rs.next()) {
                ids.add(rs.getString("matricId"));
            }
            ids.add("admin"); // for admin messaging
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(receiverField, ids); // Requires ControlsFX
    }

    @FXML
    private void handleAttachFile() {
        FileChooser fileChooser = new FileChooser();
        attachedFile = fileChooser.showOpenDialog(receiverField.getScene().getWindow());
    }

    @FXML
    private void handleSendMessage() {
        String receiverId = receiverField.getText();
        String message = messageField.getText();
        String attachmentPath = attachedFile != null ? attachedFile.getAbsolutePath() : null;

        if (receiverId.isEmpty() || message.isEmpty()) {
            showAlert("Validation", "Receiver and message cannot be empty.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "INSERT INTO messages (senderId, receiverId, message, attachmentPath, senderType) VALUES (?, ?, ?, ?, 'teacher')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, teacherId);
            stmt.setString(2, receiverId);
            stmt.setString(3, message);
            stmt.setString(4, attachmentPath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        messageField.clear();
        receiverField.clear();
        attachedFile = null;
        loadMessages();
    }

    private void loadMessages() {
        messageList.getChildren().clear();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "SELECT * FROM messages WHERE senderId = ? OR receiverId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, teacherId);
            stmt.setString(2, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String senderId = rs.getString("senderId");
                String receiverId = rs.getString("receiverId");
                String message = rs.getString("message");
                String attachment = rs.getString("attachmentPath");
                String senderType = rs.getString("senderType");

                String senderName = senderType.equals("teacher") ? findTeacherNameById(senderId)
                        : senderType.equals("student") ? findStudentNameById(senderId)
                        : "Admin";

                Label msgLabel = new Label(senderName + ": " + message);
                msgLabel.setWrapText(true);

                if (attachment != null) {
                    Hyperlink fileLink = new Hyperlink("📎 Download");
                    fileLink.setOnAction(e -> getHostServices().showDocument("file:///" + attachment.replace("\\", "/")));
                    messageList.getChildren().addAll(msgLabel, fileLink);
                } else {
                    messageList.getChildren().add(msgLabel);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String findStudentNameById(String matricId) {
        String fullName = matricId;
        String sql = "SELECT firstname, lastname FROM students WHERE matricId = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, matricId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fullName = rs.getString("firstname") + " " + rs.getString("lastname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }

    private String findTeacherNameById(String teacherId) {
        String fullName = teacherId;
        String sql = "SELECT firstname, lastname FROM teachers WHERE teacherId = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fullName = rs.getString("firstname") + " " + rs.getString("lastname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }

    private javafx.application.HostServices getHostServices() {
        return com.se.philips.Welcome.getHostServices(); // Adjust to match your entry class
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
