package com.example.taskday;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.taskday.auxiliary.Password;
import com.example.taskday.exception.PasswordException;

import static org.junit.jupiter.api.Assertions.*;


class PasswordValidationTest {
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void shouldAcceptValidPassword() {
        assertDoesNotThrow(() -> Password.create("Valid123!", passwordEncoder));
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() {
        Exception ex = assertThrows(PasswordException.class, () -> Password.create("senhasegura123!", passwordEncoder));
        assertTrue(ex.getMessage().toLowerCase().contains("uppercase"));
    }

    @Test
    void shouldRejectPasswordWithoutEspecialCharacter() {
        Exception ex = assertThrows (PasswordException.class, () -> Password.create("SenhaSegura123", passwordEncoder));
        assertTrue(ex.getMessage().toLowerCase().contains("special character"));
    }

    @Test
    void shouldRejectPasswordWithoutNumber() {
        Exception ex = assertThrows(PasswordException.class, () -> Password.create("SenhaSegura!", passwordEncoder));
        assertTrue(ex.getMessage().toLowerCase().contains("number"));
    }


}
