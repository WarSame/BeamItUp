package com.example.graeme.beamitup;

import android.util.Patterns;

import static com.example.graeme.beamitup.Account.moveCursorToNewLine;

public class LoginAccount {
    static boolean isValid(String email, String password, StringBuilder errors){
        return emailValid(email, errors) && passwordValid(password, errors);
    }

    static private boolean emailValid(String email, StringBuilder errors){
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            moveCursorToNewLine(errors);
            errors.append("Enter a valid email.");
            valid = false;
        }

        if (!Account.isEmailInUse(email, errors)){
            moveCursorToNewLine(errors);
            errors.append("Email not in use.");
            valid = false;
        }
        return valid;
    }

    static private boolean passwordValid(String password, StringBuilder errors){
        boolean valid = true;

        if (password.isEmpty() || password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            moveCursorToNewLine(errors);
            errors.append("Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.");
            valid = false;
        }

        return valid;
    }

    static boolean isAuthentic(String email, String password) {
        //Implement authentication logic - dummy for now
        boolean authentic = false;
        if (email.equals("foo@t.ca") && password.equals("barr")){
            authentic = true;
        }
        return authentic;
    }
}
