package com.example.taskday.domain.model.auxiliary;

import java.util.regex.Pattern;

import jakarta.persistence.Embeddable;

@Embeddable
public class Email {
    
    private String email;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";   
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

   
    public Email() {
    }

    public Email(String email) {
        if(isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.email = email;
    }

    public boolean isValidEmail(String email) {

        if(email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
