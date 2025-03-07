package org.example.exception;

import org.example.controller.ConversationController;
import org.example.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConversationController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Test
    void handleCurrencyConversionException() throws Exception {
        when(chatService.processMessage("Convert 100 ABC to XYZ"))
                .thenThrow(new CurrencyConversionException("Invalid currency"));

        mockMvc.perform(post("/conversation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Convert 100 ABC to XYZ\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.answer").value("Error: Invalid currency"));
    }

    @Test
    void handleExternalServiceException() throws Exception {
        when(chatService.processMessage("Convert 100 USD to EUR"))
                .thenThrow(new ExternalServiceException("API unavailable"));

        mockMvc.perform(post("/conversation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Convert 100 USD to EUR\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.answer").value("Error: API unavailable"));
    }
}