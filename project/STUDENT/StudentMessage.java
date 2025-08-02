package com.se.philips.STUDENT;

public class StudentMessage {
    private int id;
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;
    private String attachmentPath;

    public StudentMessage(int id, String sender, String receiver, String content, String timestamp, String attachmentPath) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.attachmentPath = attachmentPath;
    }

    // Getters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
    public String getAttachmentPath() { return attachmentPath; }
}
