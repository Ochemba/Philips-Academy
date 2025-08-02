package com.se.philips.AdminDashboard;

public class TeacherCreate {
    private String lastName;
    private String firstName;
    private String ID;
    private String levelAssigned;
    private String courseAssigned;
    private String department;
    private String PhoneNumber;
    private String gender;
    private String password;

    public TeacherCreate(String lastName, String firstName, String ID,
                         String levelAssigned, String courseAssigned, String PhoneNumber, String department, String gender, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.ID = ID;
        this.levelAssigned = levelAssigned;
        this.courseAssigned = courseAssigned;
        this.department = department;
        this.gender = gender;
        this.PhoneNumber = PhoneNumber;
        this.password = password;
    }

    // Getters
    public String getID() { return ID; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getLevelAssigned() { return levelAssigned; }
    public String getCourseAssigned() { return courseAssigned; }
    public String getDepartment() { return department; }
    public String getGender() { return gender; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return PhoneNumber; }

    // Setters
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setID(String ID) { this.ID = ID; }
    public void setLevelAssigned(String levelAssigned) { this.levelAssigned = levelAssigned; }
    public void setCourseAssigned(String courseAssigned) { this.courseAssigned = courseAssigned; }
    public void setDepartment(String department) { this.department = department; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhoneNumber(String phoneNumber) { this.PhoneNumber = phoneNumber; }
    public void setPassword(String password) { this.password = password; }

}
