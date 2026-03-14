package com.cts.eventsphere.TicketModuleTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Objects;

import com.cts.eventsphere.controller.TicketController;
import com.cts.eventsphere.exception.GlobalExceptionHandler;
import com.cts.eventsphere.exception.ticket.TicketAlreadyExistsException;
import com.cts.eventsphere.exception.ticket.TicketNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.dto.ticket.CreateTicketRequest;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.dto.ticket.TicketResponseDTO;
import com.cts.eventsphere.model.data.TicketStatus;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Faker faker = new Faker();

    private String eventId;
    private String ticketId;

    @BeforeEach
    void setUp() {
        eventId = faker.internet().uuid();
        ticketId = faker.internet().uuid();

        mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().isAssignableFrom(UserPrincipal.class);
                    }
                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        UserPrincipal mockPrincipal = mock(UserPrincipal.class);
                        lenient().when(mockPrincipal.userId()).thenReturn("mock-user-id");
                        return mockPrincipal;
                    }
                })
                .build();
    }

    @Test
    void createTicket_ReturnsOk() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest("VIP", 150.0, TicketStatus.active);
        GenericResponse expectedResponse = new GenericResponse("Ticket created successfully");

        when(ticketService.createTicket(anyString(), anyString(), anyDouble(), any(TicketStatus.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/events/{eventId}/tickets", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ticket created successfully"));
    }

    @Test
    void getTicketsByEventId_ReturnsList() throws Exception {
        TicketResponseDTO ticketDto = new TicketResponseDTO(ticketId, eventId, "VIP", 150.0, TicketStatus.active);
        TicketListResponseDTO expectedResponse = new TicketListResponseDTO(List.of(ticketDto), 0, 10, 1, 1);

        when(ticketService.getTicketsByEventId(anyString(), anyInt(), anyInt())).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/events/{eventId}/tickets", eventId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets[0].ticketId").value(ticketId))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateTicket_ReturnsOk() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest("General", 50.0, TicketStatus.inactive);

        when(ticketService.updateTicket(anyString(), anyString(), anyDouble(), any(TicketStatus.class)))
                .thenReturn(new GenericResponse("Ticket updated successfully"));

        mockMvc.perform(put("/api/v1/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ticket updated successfully"));
    }

    @Test
    void deleteTicket_ReturnsOk() throws Exception {
        when(ticketService.deleteTicket(ticketId))
                .thenReturn(new GenericResponse("Ticket deleted successfully"));

        mockMvc.perform(delete("/api/v1/tickets/{ticketId}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ticket deleted successfully"));
    }

    @Test
    void createTicket_ReturnsBadRequest_WhenTypeMissing() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(null, 150.0, TicketStatus.active);

        mockMvc.perform(post("/api/v1/events/{eventId}/tickets", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicket_ThrowsTicketAlreadyExistsException() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest("VIP", 150.0, TicketStatus.active);
        when(ticketService.createTicket(anyString(), anyString(), anyDouble(), any(TicketStatus.class)))
                .thenThrow(new TicketAlreadyExistsException("Ticket type already exists for this event"));

        mockMvc.perform(post("/api/v1/events/{eventId}/tickets", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertInstanceOf(TicketAlreadyExistsException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Ticket type already exists for this event", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void updateTicket_ThrowsTicketNotFoundException() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest("General", 50.0, TicketStatus.inactive);
        when(ticketService.updateTicket(anyString(), anyString(), anyDouble(), any(TicketStatus.class)))
                .thenThrow(new TicketNotFoundException("Ticket not found"));

        mockMvc.perform(put("/api/v1/tickets/{ticketId}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertInstanceOf(TicketNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Ticket not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void deleteTicket_ThrowsTicketNotFoundException() throws Exception {
        when(ticketService.deleteTicket(ticketId))
                .thenThrow(new TicketNotFoundException("Ticket not found"));

        mockMvc.perform(delete("/api/v1/tickets/{ticketId}", ticketId))
                .andExpect(result -> assertInstanceOf(TicketNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Ticket not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}