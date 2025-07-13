package com.example.taskday;

import org.junit.jupiter.api.Test;

import com.example.taskday.domain.exception.PasswordException;
import com.example.taskday.domain.model.auxiliary.Password;

import static org.junit.jupiter.api.Assertions.*;


class PasswordValidationTest {

    @Test
    void shouldAcceptValidPassword() {
        assertDoesNotThrow(() -> new Password("Valid123!"));
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() {
        Exception ex = assertThrows(PasswordException.class, () -> new Password("senhasegura123!"));
        assertTrue(ex.getMessage().toLowerCase().contains("uppercase"));
    }

    @Test
    void shouldRejectPasswordWithoutEspecialCharacter() {
        Exception ex = assertThrows (PasswordException.class, () -> new Password("senhaSegura123"));
        assertTrue(ex.getMessage().toLowerCase().contains("special character"));
    }

    @Test
    void shouldRejectPasswordWithoutNumber() {
        Exception ex = assertThrows(PasswordException.class, () -> new Password("SenhaSegura!"));
        assertTrue(ex.getMessage().toLowerCase().contains("number"));
    }


}
