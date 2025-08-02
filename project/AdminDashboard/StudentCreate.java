package com.se.philips.AdminDashboard;

public class StudentCreate {
    private String lastName;
    private String firstName;
    private String matricId;
    private String level;
    private String department;
    private String gender;
    private String password;

    public StudentCreate(String lastName, String firstName, String matricId,
                         String level, String department, String gender, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.matricId = matricId;
        this.level = level;
        this.department = department;
        this.gender = gender;
        this.password = password;
    }

    // Getters
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMatricId() { return matricId; }
    public String getLevel() { return level; }
    public String getDepartment() { return department; }
    public String getGender() { return gender; }
    public String getPassword() { return password; }

    // Setters
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMatricId(String matricId) { this.matricId = matricId; }
    public void setLevel(String level) { this.level = level; }
    public void setDepartment(String department) { this.department = department; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPassword(String password) { this.password = password; }
}
