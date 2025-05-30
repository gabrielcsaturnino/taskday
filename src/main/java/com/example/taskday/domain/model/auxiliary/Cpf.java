package com.example.taskday.domain.model.auxiliary;

import java.util.regex.Pattern;

import jakarta.persistence.Embeddable;

@Embeddable
public class Cpf {
    private String value;
    private final static String CPF_REGEX = "^(?:\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$";
    private final static Pattern CPF_PATTERN = Pattern.compile(CPF_REGEX);
  
    public Cpf() {
    }
    public Cpf(String value) {
        isValid(value);
        this.value = value;
    }
    
    public void isValid(String value) {
        if(value == null || value.isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be null or empty");
        }
    
        if(!CPF_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid CPF format: " + value);
        }
    }

}
