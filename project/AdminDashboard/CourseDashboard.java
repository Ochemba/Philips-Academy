package com.se.philips.AdminDashboard;

public class CourseDashboard {
    private String title;
    private String category;
    private String difficulty;
    private String level;

    public CourseDashboard(String title, String category, String difficulty, String level) {
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getLevel() {
        return level;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
