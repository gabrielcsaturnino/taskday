package com.example.taskday.domain.model.auxiliary;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.taskday.domain.exception.PasswordException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(name = "hash_password", nullable = false)
    private String hashValue;

    private Password(String hashValue) {
        this.hashValue = hashValue;
    }
    
    protected Password() {}

 
    public static Password create(String rawPassword, PasswordEncoder encoder) {
        validate(rawPassword);
        String hashedPassword = encoder.encode(rawPassword);
        return new Password(hashedPassword);
    }
    
    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.hashValue);
    }

    public String getHashValue() {
        return hashValue;
    }

    private static void validate(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new PasswordException("Password cannot be empty.");
        }
        
        StringBuilder err = new StringBuilder();
        if (rawPassword.chars().noneMatch(Character::isUpperCase)) {
            err.append("Password must contain at least one uppercase letter.\n");
        }
        if (rawPassword.chars().noneMatch(ch -> "!@#$%^&*()_+[]{}|;':\",.<>?/".indexOf(ch) >= 0)) {
            err.append("Password must contain at least one special character.\n");
        }
        if (rawPassword.chars().noneMatch(Character::isDigit)) {
            err.append("Password must contain at least one number.\n");
        }
        if (err.length() > 0) {
            throw new PasswordException(err.toString());
        }
    }

    public String getPassword() {
        return hashValue;
    }

    
}