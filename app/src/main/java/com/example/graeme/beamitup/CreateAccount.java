package com.example.graeme.beamitup;

import android.util.Patterns;

import static com.example.graeme.beamitup.Account.moveCursorToNewLine;

class CreateAccount {

    static boolean isValid(String email, String password, String confirmPassword, StringBuilder errors){
        return emailValid(email, errors) && passwordValid(password, confirmPassword, errors);
    }

    static private boolean emailValid(String email, StringBuilder errors) {
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            moveCursorToNewLine(errors);
            errors.append("Enter a valid email.");
            valid = false;
        }

        if (Account.isEmailInUse(email, errors)){
            moveCursorToNewLine(errors);
            errors.append("Email in use - choose another email.");
            valid = false;
        }
        return valid;
    }

    static private boolean passwordValid(String password, String confirmPassword, StringBuilder errors) {
        boolean valid = true;

        if (password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            errors.append("Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.");
            valid = false;
        }

        if (!password.equals(confirmPassword)){
            moveCursorToNewLine(errors);
            errors.append("Password and password confirmation must match.");
            valid = false;
        }

        return valid;
    }

}
