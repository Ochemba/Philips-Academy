package com.se.philips.STUDENT;

import com.se.philips.STUDENT.StudentMessage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentMessageController {

    @FXML private TextField receiverField;
    @FXML private TextArea messageField;
    @FXML private VBox messageList;
    private File selectedAttachment;

    private final String loggedInStudentId = StudentLoginSession.getMatricId(); // Dynamic

    public void initialize() {
        loadMessages();
    }

    private String getFullIdentity(String userId) {
        String fullIdentity = userId; // fallback if name not found

        String[] queries = {
                "SELECT firstname, lastname FROM students WHERE matricId = ?",
                "SELECT firstname, lastname FROM teachers WHERE teacherId = ?",
                "SELECT name FROM admins WHERE adminId = ?"
        };

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            for (String query : queries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, userId);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String first = rs.getString("firstname");
                        String last = rs.getString("lastname");
                        if (first != null && last != null)
                            return userId + " - " + first + " " + last;
                        else if (rs.getString("name") != null)
                            return userId + " - " + rs.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fullIdentity;
    }

    @FXML
    private void handleAttachFile() {
        FileChooser fileChooser = new FileChooser();
        selectedAttachment = fileChooser.showOpenDialog(null);
    }

    private boolean isTeacher(String receiverId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "SELECT 1 FROM teachers WHERE staff_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, receiverId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @FXML
    private void handleSendMessage() {
        String receiver = receiverField.getText().trim();
        String content = messageField.getText().trim();
        String timestamp = LocalDateTime.now().toString();

        if (receiver.isEmpty() || content.isEmpty()) return;

        // Restrict: only send to teachers
        if (!isTeacher(receiver)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You can only send messages to teachers.");
            alert.show();
            return;
        }

        String attachmentPath = selectedAttachment != null ? selectedAttachment.getAbsolutePath() : null;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "INSERT INTO Messages (sender, receiver, content, timestamp, attachmentPath) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, loggedInStudentId);
            stmt.setString(2, receiver);
            stmt.setString(3, content);
            stmt.setString(4, timestamp);
            stmt.setString(5, attachmentPath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        messageField.clear();
        receiverField.clear();
        selectedAttachment = null;
        loadMessages();
    }


    private void loadMessages() {
        messageList.getChildren().clear();
        List<StudentMessage> messages = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "SELECT * FROM Messages WHERE receiver = ? OR sender = ? ORDER BY timestamp DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, loggedInStudentId);
            stmt.setString(2, loggedInStudentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new StudentMessage(
                        rs.getInt("id"),
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("content"),
                        rs.getString("timestamp"),
                        rs.getString("attachmentPath")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (StudentMessage msg : messages) {
            VBox bubble = new VBox();
            bubble.setStyle("-fx-background-color: #dff9fb; -fx-padding: 10; -fx-background-radius: 8;");
            bubble.setSpacing(5);
            String receiverIdentity = getFullIdentity(msg.getReceiver());
            bubble.getChildren().add(new Label("To: " + receiverIdentity));

            String senderIdentity = getFullIdentity(msg.getSender());
            bubble.getChildren().add(new Label("From: " + senderIdentity));
            ;
            bubble.getChildren().add(new Label(msg.getContent()));
            bubble.getChildren().add(new Label("At: " + msg.getTimestamp()));

            if (msg.getAttachmentPath() != null && !msg.getAttachmentPath().isEmpty()) {
                Hyperlink link = new Hyperlink("📎 Attachment");
                link.setOnAction(e -> getHostServices().showDocument("file:///" + msg.getAttachmentPath()));
                bubble.getChildren().add(link);
            }

            Button replyBtn = new Button("Reply");
            replyBtn.setOnAction(e -> {
                receiverField.setText(msg.getSender());
                messageField.requestFocus();
            });
            bubble.getChildren().add(replyBtn);

            messageList.getChildren().add(bubble);
        }
    }
        private javafx.application.HostServices getHostServices() {
        return com.se.philips.Welcome.getAppHostServices();  // ✅ This is correct
    }

}
