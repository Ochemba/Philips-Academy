package com.se.philips.AdminDashboard;

import java.time.LocalDateTime;

public class MessageDashboard {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
    private String attachmentPath;

    public MessageDashboard(String sender, String receiver, String content, LocalDateTime timestamp, String attachmentPath) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.attachmentPath = attachmentPath;
    }

    // Getters
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getAttachmentPath() { return attachmentPath; }
}
