package com.cts.eventsphere.RegistrationModuleTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Objects;

import com.cts.eventsphere.controller.RegistrationController;
import com.cts.eventsphere.exception.GlobalExceptionHandler;
import com.cts.eventsphere.exception.registration.RegistrationAlreadyExistsException;
import com.cts.eventsphere.exception.registration.RegistrationNotFoundException;
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

import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.dto.registration.RegistrationListResponseDTO;
import com.cts.eventsphere.dto.registration.RegistrationRequestDTO;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Faker faker = new Faker();

    private String eventId;
    private String userId;
    private String ticketId;
    private String registrationId;

    @BeforeEach
    void setUp() {
        eventId = faker.internet().uuid();
        userId = faker.internet().uuid();
        ticketId = faker.internet().uuid();
        registrationId = faker.internet().uuid();

        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
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
                        lenient().when(mockPrincipal.userId()).thenReturn(userId);
                        return mockPrincipal;
                    }
                })

                .build();
    }

    @Test
    void createRegistration_ReturnsOk() throws Exception {
        RegistrationRequestDTO request = new RegistrationRequestDTO(ticketId);
        GenericResponse expectedResponse = new GenericResponse("Registration successful");

        when(registrationService.registerForEvent(userId, eventId, ticketId)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/events/{eventId}/registrations", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful"));
    }

    @Test
    void getAllRegistrationsByEvent_ReturnsList() throws Exception {
        RegistrationDTO regDto = new RegistrationDTO(registrationId, eventId, ticketId, userId, "pending");
        RegistrationListResponseDTO expectedResponse = new RegistrationListResponseDTO(List.of(regDto), 0, 10, 1, 1);

        when(registrationService.getRegistrationsByEventId(anyString(), anyInt(), anyInt())).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/events/{eventId}/registrations", eventId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrations[0].registrationId").value(registrationId))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void approveRegistration_ReturnsOk() throws Exception {
        when(registrationService.approveRegistration(registrationId))
                .thenReturn(new GenericResponse("Registration approved successfully"));

        mockMvc.perform(patch("/api/v1/registrations/{registrationId}/approve", registrationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration approved successfully"));
    }

    @Test
    void cancelRegistration_ReturnsOk() throws Exception {
       lenient().when(registrationService.cancelRegistration(registrationId))
                .thenReturn(new GenericResponse("Registration cancelled successfully"));

        mockMvc.perform(patch("/api/v1/registrations/{registrationId}/cancel", registrationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration cancelled successfully"));
    }

    @Test
    void createRegistration_ReturnsBadRequest_WhenTicketIdMissing() throws Exception {
        RegistrationRequestDTO request = new RegistrationRequestDTO(null);

        mockMvc.perform(post("/api/v1/events/{eventId}/registrations", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRegistration_ThrowsRegistrationAlreadyExistsException() throws Exception {
        RegistrationRequestDTO request = new RegistrationRequestDTO(ticketId);
        lenient().when(registrationService.registerForEvent(userId, eventId, ticketId))
                .thenThrow(new RegistrationAlreadyExistsException("User is already registered for this event"));

        mockMvc.perform(post("/api/v1/events/{eventId}/registrations", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertInstanceOf(RegistrationAlreadyExistsException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("User is already registered for this event", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void approveRegistration_ThrowsRegistrationNotFoundException() throws Exception {
        lenient().when(registrationService.approveRegistration(registrationId))
                .thenThrow(new RegistrationNotFoundException("Registration not found"));

        mockMvc.perform(patch("/api/v1/registrations/{registrationId}/approve", registrationId))
                .andExpect(result -> assertInstanceOf(RegistrationNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Registration not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}