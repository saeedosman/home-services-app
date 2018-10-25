package com.example.aaron.groupprojectseg;

public class ValidationHelper {
    public ValidationHelper() {
    }

    public String validateUsername(String s) {
        if (s.length() > 16 || s.length() < 3) return "Username must have 3-16 characters";
        if (!s.matches("[a-zA-Z0-9]*")) return "Username must contain only letters and numbers";
        return null;
    }
    public String validateFirstName(String s) {
        if (!s.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) return "Invalid First Name";
        return null;
    }

    public String validateLastName(String s) {
        if (!s.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) return "Invalid Last Name";
        return null;
    }

    public String validatePassword(String s) {
        if (s.length() < 3) return "Password must have at least 3 characters";
        return null;
    }
    public String validateEmail(String s) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) return "Invalid email address";
        return null;
    }

}