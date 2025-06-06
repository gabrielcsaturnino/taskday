package com.example.taskday.domain.model.auxiliary;

import java.util.regex.Pattern;

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
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }
    }




    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }




    

    
}
