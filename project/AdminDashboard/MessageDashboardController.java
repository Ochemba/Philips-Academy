package com.se.philips.AdminDashboard;

import com.se.philips.AdminDashboard.MessageDashboard;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;

public class MessageDashboardController {

    @FXML private TextField receiverField;
    @FXML private TextArea messageField;
    @FXML private VBox messageList;

    private File attachmentFile;

    @FXML
    public void initialize() {
        loadMessagesFromFile();
    }

    @FXML
    private void handleAttachFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose File");
        attachmentFile = chooser.showOpenDialog(null);
    }

    @FXML
    private void handleSendMessage() {
        String url = "jdbc:sqlite:MainDB.db";
        String sql = "INSERT INTO Messages (receiver, content, sender, timestamp, attachmentPath) VALUES (?, ?, ?, ?, ?)";

        String receiver = receiverField.getText();
        String content = messageField.getText();
        String sender = "Admin";
        LocalDateTime timestamp = LocalDateTime.now();
        String attachmentPath = attachmentFile != null ? attachmentFile.getAbsolutePath() : "";

        if (receiver.isEmpty() || content.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, receiver);
            pstmt.setString(2, content);
            pstmt.setString(3, sender);
            pstmt.setString(4, timestamp.toString());
            pstmt.setString(5, attachmentPath);

            pstmt.executeUpdate();
            System.out.println("Message sent!");
            receiverField.clear();
            messageField.clear();
            attachmentFile = null;
            loadMessagesFromFile();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }}


        private void loadMessagesFromFile() {
            String url = "jdbc:sqlite:MainDB.db";
            String sql = "SELECT sender, receiver, content, timestamp, attachmentPath FROM Messages";
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String receiver = rs.getString("receiver");
                    String content = rs.getString("content");
                    String timestampStr = rs.getString("timestamp");
                    String attachment = rs.getString("attachmentPath");
                    LocalDateTime timestamp = LocalDateTime.parse(timestampStr);
                    MessageDashboard message = new MessageDashboard(sender, receiver, content, timestamp, attachment);
                    displayMessage(message, false);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    private void displayMessage(MessageDashboard message, boolean isNew) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-spacing: 5; -fx-border-color: #bdc3c7;");

        Label from = new Label("From: " + message.getSender());
        Label to = new Label("To: " + message.getReceiver());
        Label content = new Label("Message: " + message.getContent());
        Label time = new Label("Sent: " + message.getTimestamp().toString());

        Button replyButton = new Button("Reply");
        replyButton.setOnAction(e -> receiverField.setText(message.getSender()));

        Button openAttachment = new Button("Open Attachment");
        openAttachment.setVisible(!message.getAttachmentPath().isEmpty());
        openAttachment.setOnAction(e -> {
            try {
                new ProcessBuilder("explorer", message.getAttachmentPath()).start();
            } catch (IOException ex) {
                showAlert("Cannot open file.");
            }
        });

        box.getChildren().addAll(from, to, content, time, replyButton, openAttachment);
        messageList.getChildren().add(0, box);
    }

    private void saveMessageToFile(MessageDashboard message) {


    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
}
