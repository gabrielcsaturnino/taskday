package com.example.jooby;

import com.example.jooby.address.dto.CreateAddressRequestDTO;
import com.example.jooby.user.Client;
import com.example.jooby.user.dto.CreateClientRequestDTO;
import com.example.jooby.user.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class ControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    private MockMvc mockMvc;
    private Client testClient;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        // Criar um cliente de teste
        CreateAddressRequestDTO addressDTO = new CreateAddressRequestDTO(
                "Rua Teste", "123", "Centro", "São Paulo", "SP", "01234-567"
        );

        CreateClientRequestDTO clientDTO = new CreateClientRequestDTO(
                "João", "Silva", "1234567890", "Teste123!", "11999999999",
                "joao.teste@example.com", "123.456.789-09", "1990-01-01", addressDTO
        );

        // Criar cliente manualmente usando o construtor correto
        testClient = new Client(
            "João", 
            "Silva", 
            new com.example.jooby.auxiliary.Phone("11999999999"),
            new com.example.jooby.auxiliary.Email("joao.teste@example.com"),
            new com.example.jooby.auxiliary.Cpf("12345678909"),
            com.example.jooby.auxiliary.Password.create("Teste123!", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()),
            "1234567890",
            new com.example.jooby.auxiliary.DateOfBirthday("1990-01-01")
        );
        
        testClient = clientRepository.save(testClient);
    }

    @Test
    public void testGetProfile_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/clients/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetProfile_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/clients/profile")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateClient_ShouldReturnCreated() throws Exception {
        CreateAddressRequestDTO addressDTO = new CreateAddressRequestDTO(
                "Rua Nova", "456", "Bairro Novo", "Rio de Janeiro", "RJ", "20000-000"
        );

        CreateClientRequestDTO clientDTO = new CreateClientRequestDTO(
                "Maria", "Santos", "0987654321", "Teste123!", "11888888888",
                "maria.teste@example.com", "987.654.321-00", "1985-05-15", addressDTO
        );

        mockMvc.perform(post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Maria"))
                .andExpect(jsonPath("$.lastName").value("Santos"))
                .andExpect(jsonPath("$.email").value("maria.teste@example.com"));
    }

    @Test
    public void testGetProfile_WithInvalidUser_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/clients/profile")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetJobs_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/jobs/active"))
                .andExpect(status().isOk());
    }
}