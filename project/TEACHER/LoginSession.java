package com.se.philips.TEACHER;

public class LoginSession {
    private static String teacherId;

    public static void setTeacherId(String id) {
        teacherId = id;
    }

    public static String getTeacherId() {
        return teacherId;
    }

    public static void clear() {
        teacherId = null;
    }
}
