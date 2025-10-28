package com.example.jooby.auxiliary;

import java.util.regex.Pattern;

import com.example.jooby.exception.InvalidFormatException;
import com.example.jooby.exception.SaveNullObjectException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email {
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";   
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

   
    public Email() {
    }

    public Email(String email) {
        isValidEmail(email);
        this.email = email;
    }

    public void isValidEmail(String email) {

        if(email == null || email.isEmpty()){
            throw new SaveNullObjectException("Email cannot be null");
        }

        if (EMAIL_PATTERN.matcher(email).matches() == false) {
            throw new InvalidFormatException("Invalid email format");
        }
    }

    public String getEmail() {
        return email;
    }
}
