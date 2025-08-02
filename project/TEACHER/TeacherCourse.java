package com.se.philips.TEACHER;

public class TeacherCourse {
    private int courseId;
    private String title;
    private String description;
    private String category;
    private String tags;
    private String difficulty;
    private String level;
    private String thumbnailPath;
    private String attachmentPath;

    public TeacherCourse(int courseId, String title, String description, String category,
                         String tags, String difficulty, String thumbnailPath, String level,String attachmentPath) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.difficulty = difficulty;
        this.level = level;
        this.thumbnailPath = thumbnailPath;
        this.attachmentPath = attachmentPath;
    }

    // Getters
    public int getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getLevel(){ return level; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getTags() { return tags; }
    public String getDifficulty() { return difficulty; }
    public String getThumbnailPath() { return thumbnailPath; }
    public String getAttachmentPath() { return attachmentPath; }

    // Setters
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setTitle(String title) { this.title = title; }
    public void setLevel(String level) { this.level = level; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setTags(String tags) { this.tags = tags; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }
}
