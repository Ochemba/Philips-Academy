package com.se.philips.AdminDashboard;

import java.time.LocalDate;

public class AdminProfile {
    private String name;
    private String password;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String imagePath;

    public AdminProfile(String name, String password, String phone, String gender, LocalDate dob, String imagePath) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public LocalDate getDob() { return dob; }
    public String getImagePath() { return imagePath; }
}
