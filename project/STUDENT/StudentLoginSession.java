package com.se.philips.STUDENT;

public class StudentLoginSession {
    private static String matricId;
    private static String firstName;
    private static String level;
    private static String department;

    public static void setSession(String matricId, String firstName, String level, String department) {
        StudentLoginSession.matricId = matricId;
        StudentLoginSession.firstName = firstName;
        StudentLoginSession.level = level;
        StudentLoginSession.department = department;
    }

    public static String getMatricId() {
        return matricId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLevel() {
        return level;
    }

    public static String getDepartment() {
        return department;
    }

    public static void clearSession() {
        matricId = null;
        firstName = null;
        level = null;
        department = null;
    }
}
