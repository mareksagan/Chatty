package org.example.exception;

import org.example.dto.ConversationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyConversionException.class)
    public ResponseEntity<ConversationResponse> handleCurrencyConversionException(CurrencyConversionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ConversationResponse("Error: " + ex.getMessage()));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ConversationResponse> handleExternalServiceException(ExternalServiceException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ConversationResponse("Error: " + ex.getMessage()));
    }
}
