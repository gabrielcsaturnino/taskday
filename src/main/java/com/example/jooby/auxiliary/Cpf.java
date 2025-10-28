package com.example.jooby.auxiliary;

import java.util.regex.Pattern;

import com.example.jooby.exception.InvalidFormatException;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cpf {
    @Column(name = "cpf", nullable = false, unique = true, length = 11)
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
            throw new InvalidFormatException("CPF cannot be null or empty");
        }
    
        if(!CPF_PATTERN.matcher(value).matches()) {
            throw new InvalidFormatException("Invalid CPF format: " + value);
        }
        
        // Validar CPF usando Caelum Stella
        String cleanCpf = value.replaceAll("[^0-9]", "");
        CPFValidator validator = new CPFValidator();
        try {
            validator.assertValid(cleanCpf);
        } catch (InvalidStateException e) {
            throw new InvalidFormatException("Invalid CPF: " + value);
        }
    }

    public String getValue() {
        return value;
    }
}
