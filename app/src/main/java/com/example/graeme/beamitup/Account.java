package com.example.graeme.beamitup;

class Account {
    static final int MINIMUM_PASSWORD_LENGTH = 4;
    static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private String passwordHash;

    Account(String email, String passwordHash){
        this.email = email;
        this.passwordHash = passwordHash;
    }

    String getEmail() {
        return email;
    }

   String getPasswordHash() {
        return passwordHash;
    }

    static void startNewLine(StringBuilder errors) {
        //Prepend text we are going to add with a new line if it is not the first text in the string builder
        if (errors.length() != 0) {
            errors.append("\n");
        }
    }
}
