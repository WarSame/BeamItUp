package com.example.graeme.beamitup;

import android.util.Patterns;

class Account {
    private static final int MINIMUM_PASSWORD_LENGTH = 4;
    private static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private String password;

    Account(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    static boolean isValidLogin(String email, String password, StringBuilder errors){
        return emailValidLogin(email, errors) && passwordValidLogin(password, errors);
    }

    static private boolean emailValidLogin(String email, StringBuilder errors){
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errors.append("Enter a valid email.\n");
            valid = false;
        }
        return valid;
    }

    static private boolean passwordValidLogin(String password, StringBuilder errors){
        boolean valid = true;

        if (password.isEmpty() || password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            errors.append("Enter a password between 4 and 16 characters.\n");
            valid = false;
        }

        return valid;
    }

    static boolean isValidCreateAccount(String email, String password, String confirmPassword, StringBuilder errors){
        return emailValidCreateAccount(email, errors) && passwordValidCreateAccount(password, confirmPassword, errors);
    }

    static private boolean emailValidCreateAccount(String email, StringBuilder errors) {
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errors.append("Enter a valid email.\n");
            valid = false;
        }

        if (emailInUse(email, errors)){
            errors.append("Email in use - choose another email.\n");
            valid = false;
        }
        return valid;
    }

    static private boolean emailInUse(String email, StringBuilder errors) {
        //Dummy - check DB for email when creating account
        return false;
    }

    static private boolean passwordValidCreateAccount(String password, String confirmPassword, StringBuilder errors) {
        boolean valid = true;

        if (password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            valid = false;
        }

        if (!password.equals(confirmPassword)){
            errors.append("Password and confirm password must match.\n");
            valid = false;
        }

        return valid;
    }
}
