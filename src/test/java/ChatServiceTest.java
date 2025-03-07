package org.example.service;

import org.example.exception.CurrencyConversionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private JokeService jokeService;

    @Mock
    private CurrencyConverterService converterService;

    private ChatService chatService;

    @BeforeEach
    void setup() {
        chatService = new ChatService(jokeService, converterService);
    }

    @Test
    void processMessage_shouldHandleGreetings() {
        assertResponse("Hi there!", "Hello! How can I assist you today?");
        assertResponse("Greetings!", "Hello! How can I assist you today?");
    }

    @Test
    void processMessage_shouldHandleJokeRequests() {
        when(jokeService.getRandomJoke()).thenReturn("Test joke");

        assertResponse("Tell me a joke", "Test joke");
        assertResponse("Can you tell me a joke?", "Test joke");
    }

    @Test
    void processMessage_shouldHandleConversions() throws Exception {
        when(converterService.convert(100, "USD", "EUR"))
                .thenReturn(92.0);

        assertResponse("Convert 100 USD to EUR", "100.00 USD is 92.00 EUR");
        assertResponse("How much is 100 USD in EUR?", "100.00 USD is 92.00 EUR");
    }

    @Test
    void processMessage_shouldHandleConversionErrors() {
        // Test both currency error scenarios
        when(converterService.convert(anyDouble(), eq("ABC"), anyString()))
                .thenThrow(new CurrencyConversionException("Invalid currency: ABC"));
        when(converterService.convert(anyDouble(), anyString(), eq("XYZ")))
                .thenThrow(new CurrencyConversionException("Invalid currency: XYZ"));

        // Test invalid from currency
        String response1 = chatService.processMessage("Convert 100 ABC to USD");
        assertTrue(response1.contains("Error: Invalid currency: ABC"));

        // Test invalid to currency
        String response2 = chatService.processMessage("Convert 100 USD to XYZ");
        assertTrue(response2.contains("Error: Invalid currency: XYZ"));
    }

    @Test
    void processMessage_shouldHandleUnknownQuestions() {
        assertResponse("What's the weather?",
                "I'm sorry, I didn't understand your question. Can you please rephrase it?");
    }

    private void assertResponse(String input, String expected) {
        assertEquals(expected, chatService.processMessage(input));
    }
}
