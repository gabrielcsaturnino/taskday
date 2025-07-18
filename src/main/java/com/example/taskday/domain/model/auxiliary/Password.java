package com.example.taskday.domain.model.auxiliary;


import com.example.taskday.domain.exception.PasswordException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(name = "hash_password", nullable = false)
    private String password;

    public Password() {
    }

    public Password(String password){
        validate(password);
        this.password = password;
    }


    public boolean validate(String password){
        StringBuilder err = new StringBuilder();

        if(!containsUpperCase(password)){
            err.append("Password must contain at least one uppercase letter.\n");
        }

        if (!containsEspecialCharacter(password)) {
            err.append("Password must contain at least one special character.\n");
        }

        if (!containsNumber(password)) {
            err.append("Password must contain at least one number.\n");
        }

        if(err.length() > 0){
            throw new PasswordException(err.toString());
        }

        return true;

    }

    public boolean containsUpperCase(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    public boolean containsEspecialCharacter(String password) {
        return password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;':\",.<>?/".indexOf(ch) >= 0);
    }

    public boolean containsNumber(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validate(password);
        this.password = password;
    }
    
}
