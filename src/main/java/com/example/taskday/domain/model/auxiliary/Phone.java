package com.example.taskday.domain.model.auxiliary;

import java.util.regex.Pattern;

import jakarta.persistence.Embeddable;

@Embeddable
public class Phone {
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


    /**
     * Validates the phone number format.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */

    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }




    

    
}
