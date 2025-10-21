package com.example.taskday;

import com.example.taskday.address.dto.CreateAddressRequestDTO;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.repository.ChatRoomRepository;
import com.example.taskday.chatroom.service.ChatRoomService;
import com.example.taskday.infra.security.service.AuthenticationService;
import com.example.taskday.job.Job;
import com.example.taskday.job.JobApplication;
import com.example.taskday.job.dto.CreateJobRequestDTO;
import com.example.taskday.job.enums.JobApplicationStatusEnum;
import com.example.taskday.job.service.JobApplicationService;
import com.example.taskday.job.service.JobService;
import com.example.taskday.message.Message;
import com.example.taskday.message.dto.MessageDTO;
import com.example.taskday.user.Client;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.dto.CreateClientRequestDTO;
import com.example.taskday.user.dto.CreateContractorRequestDTO;
import com.example.taskday.user.service.ClientService;
import com.example.taskday.user.service.ContractorService;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChatControllerIT { 

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @Autowired private ContractorService contractorService;
    @Autowired private ClientService clientService;
    @Autowired private JobService jobService;
    @Autowired private JobApplicationService jobApplicationService;
    @Autowired private AuthenticationService authenticationService;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired private TransactionTemplate transactionTemplate; 

    // Fila para receber as mensagens do WebSocket de forma síncrona
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();

    // Variáveis para guardar os tokens e a sala de chat para os testes
    private String clientJwt;
    private String contractorJwt;
    private ChatRoom activeChatRoom;

    @BeforeEach
    public void setUp() {
        this.stompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        transactionTemplate.execute(status -> {
           
        
        CreateAddressRequestDTO address1 = new CreateAddressRequestDTO("Main St", "123", "Apt 456", "Downtown", "12345-678","Cityville");
        CreateContractorRequestDTO contractorDTO = new CreateContractorRequestDTO ("John", "Doe", "123456789", "securePassword123#", "62983374358","jhonDoe@gmail.com", "08063359127","1990-01-01", "Contractor description",address1);
        contractorService.createContractor(contractorDTO);

        CreateAddressRequestDTO address2 = new CreateAddressRequestDTO("Second St", "456", "Suite 789", "Uptown", "23456-789","Townsville");
        CreateClientRequestDTO clientDTO = new CreateClientRequestDTO("Client", "User", "987654329", "clientPassword123#", "62998765432", "clientEmail@gmail.com", "02920643223", "1990-01-01", address2);
        clientService.createClient(clientDTO);

        Client client = clientService.findByEmail(new Email("clientEmail@gmail.com"));
        Contractor contractor = contractorService.findContractorByEmail(new Email("jhonDoe@gmail.com"));

        CreateJobRequestDTO createJobDTO = new CreateJobRequestDTO("Software Development", "Develop a new feature", 140, address2);
        jobService.createJob(createJobDTO, client.getId());
        Job job = jobService.findAllByClientId(client.getId()).get(0);

        JobApplication jobApplication = jobApplicationService.createJobApplication(job.getId(), contractor.getId());
        
        jobApplicationService.updateStatus(jobApplication.getId(), JobApplicationStatusEnum.ACCEPTED);
        
        this.activeChatRoom = chatRoomRepository.findAll().stream().filter(chatRoom -> chatRoom.getClient().getId().equals(client.getId()) && chatRoom.getContractor().getId().equals(contractor.getId())).findFirst()
                .orElseThrow(() -> new RuntimeException("ChatRoom not found for the created Job"));
        System.err.println("ChatRoom created with ID: " + activeChatRoom.getChatRoomStatusEnum());
        CustomUserDetails clientDetails = new CustomUserDetails(client, null);
        UsernamePasswordAuthenticationToken clientAuth = new UsernamePasswordAuthenticationToken(clientDetails, null, clientDetails.getAuthorities());
        this.clientJwt = authenticationService.authenticate(clientAuth);

        CustomUserDetails contractorDetails = new CustomUserDetails(null, contractor);
        UsernamePasswordAuthenticationToken contractorAuth = new UsernamePasswordAuthenticationToken(contractorDetails, null, contractorDetails.getAuthorities());
        this.contractorJwt = authenticationService.authenticate(contractorAuth);
        return null;
        });
    }

    @Test
    void sendMessage_whenClientIsParticipant_shouldBroadcastMessage() throws Exception {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer " + clientJwt);
        CompletableFuture<MessageDTO> messageFuture = new CompletableFuture<>();
        StompSession stompSession = stompClient.connectAsync(
                String.format("ws://localhost:%d/ws", port),new WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {}
        ).get(2, TimeUnit.SECONDS);

        stompSession.subscribe("/topic/chat/" + activeChatRoom.getId(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageDTO.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageFuture.complete((MessageDTO) payload);
            }
        });

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setChatRoomId(activeChatRoom.getId());
        messageDTO.setContentMessage("Olá, sou o cliente!");
        messageDTO.setTypeMessage("text");
        messageDTO.setOwner("client_owner");
        stompSession.send("/app/chat.sendMessage", messageDTO);
        



        MessageDTO receivedMessage = messageFuture.get(5, TimeUnit.SECONDS);
        assertNotNull(receivedMessage, "A mensagem não foi recebida do tópico.");
        assertEquals("Olá, sou o cliente!", receivedMessage.getContentMessage());
        assertEquals(activeChatRoom.getId(), receivedMessage.getChatRoomId());
    }

    @Test
    @Transactional
    void sendMessage_whenUserIsNotParticipant_shouldNotBroadcastMessage() throws Exception {
        
        CreateAddressRequestDTO address3 = new CreateAddressRequestDTO("Third St", "789", "zdzddzd", "Suburb", "34567-890","Otherville");
        CreateContractorRequestDTO impostorDTO = new CreateContractorRequestDTO("Impostor", "User", "555555555", "impostorPass123#", "62955555555", "impostor@gmail.com", "08063359127", "1995-05-05",  "Impostor description",address3);
        contractorService.createContractor(impostorDTO);
        Contractor impostor = contractorService.findContractorByEmail(new Email("impostor@gmail.com"));
        
        CustomUserDetails impostorDetails = new CustomUserDetails(null, impostor);
        UsernamePasswordAuthenticationToken impostorAuth = new UsernamePasswordAuthenticationToken(impostorDetails, null, impostorDetails.getAuthorities());
        String impostorJwt = authenticationService.authenticate(impostorAuth);

        WebSocketHttpHeaders connectHeaders = new WebSocketHttpHeaders();
        connectHeaders.add("Authorization", "Bearer " + impostorJwt);

        StompSession stompSession = stompClient.connectAsync(
                String.format("ws://localhost:%d/ws", port), connectHeaders, new StompSessionHandlerAdapter() {}
        ).get(2, TimeUnit.SECONDS);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setChatRoomId(activeChatRoom.getId());
        messageDTO.setContentMessage("Tentando invadir o chat");
        stompSession.send("/app/chat.sendMessage", messageDTO);

        Message receivedMessage = messageQueue.poll(2, TimeUnit.SECONDS);

        assertNull(receivedMessage, "Uma mensagem foi enviada indevidamente por um não participante.");
    }
}
