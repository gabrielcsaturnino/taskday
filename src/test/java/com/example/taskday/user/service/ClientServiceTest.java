package com.example.taskday.user.service;

import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.exception.DuplicateFieldException;
import com.example.taskday.exception.NotFoundException;
import com.example.taskday.user.Client;
import com.example.taskday.user.dto.CreateClientRequestDTO;
import com.example.taskday.user.dto.UpdateClientRequestDTO;
import com.example.taskday.user.repository.ClientRepository;
import com.example.taskday.user.repository.ContractorRepository;
import com.example.taskday.address.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ContractorRepository contractorRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private CreateClientRequestDTO createClientDTO;
    private UpdateClientRequestDTO updateClientDTO;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail(new Email("john@example.com"));
        testClient.setPhone(new Phone("(11) 99999-9999"));

        createClientDTO = new CreateClientRequestDTO(
                "John", "Doe", "123456789", "Password123!",
                "(11) 99999-9999", "john@example.com", "11144477735",
                "1990-01-01", new com.example.taskday.address.dto.CreateAddressRequestDTO(
                        "Rua Teste", "123", "Centro", "São Paulo", "SP", "01234567"
                )
        );

        updateClientDTO = new UpdateClientRequestDTO(
                "Jane", "Smith", "(11) 98888-8888", "jane@example.com"
        );
    }

    @Test
    void createClient_ShouldCreateClientSuccessfully() {
        // Given
        when(clientRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(clientRepository.existsByPhone(any(Phone.class))).thenReturn(false);
        when(contractorRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(contractorRepository.existsByPhone(any(Phone.class))).thenReturn(false);
        when(contractorRepository.existsByCpf(any())).thenReturn(false);
        when(contractorRepository.existsByrgDoc(anyString())).thenReturn(false);
        when(clientRepository.existsByCpf(any())).thenReturn(false);
        when(clientRepository.existsByrgDoc(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        clientService.createClient(createClientDTO);

        // Then
        verify(clientRepository).save(any(Client.class));
        verify(addressRepository).save(any());
    }

    @Test
    void createClient_ShouldThrowException_WhenEmailAlreadyExists() {
        // Given
        when(clientRepository.existsByEmail(any(Email.class))).thenReturn(true);

        // When & Then
        assertThrows(DuplicateFieldException.class, () -> {
            clientService.createClient(createClientDTO);
        });
    }

    @Test
    void findById_ShouldReturnClient_WhenClientExists() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        // When
        Client result = clientService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testClient.getId(), result.getId());
        assertEquals(testClient.getFirst_name(), result.getFirst_name());
    }

    @Test
    void findById_ShouldThrowException_WhenClientDoesNotExist() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            clientService.findById(1L);
        });
    }

    @Test
    void updateClient_ShouldUpdateClientSuccessfully() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(contractorRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(clientRepository.existsByPhone(any(Phone.class))).thenReturn(false);
        when(contractorRepository.existsByPhone(any(Phone.class))).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // When
        Client result = clientService.updateClient(1L, updateClientDTO);

        // Then
        assertNotNull(result);
        verify(clientRepository).save(any(Client.class));
    }
}
