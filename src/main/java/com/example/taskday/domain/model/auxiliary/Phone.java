package com.example.taskday.domain.model.auxiliary;

import java.util.regex.Pattern;

import com.example.taskday.domain.exception.InvalidFormatException;
import com.example.taskday.domain.exception.SaveNullObjectException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Phone {
    @Column(name = "phone", nullable = false, unique = true)
    private String phoneNumber;
private static final String PHONE_REGEX =
            "^(?:\\+55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(9[0-9]{4}[-\\s]?[0-9]{4})$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);



    public Phone() {
    }

    public Phone(String phoneNumber) {
        isValidPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        isValidPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }




    public void isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new InvalidFormatException("Number cannot be null");
        }

        if (PHONE_PATTERN.matcher(phoneNumber).matches() == false) {
            throw new InvalidFormatException("Invalid phone number format: " + phoneNumber);
        }

    }




    

    
}
