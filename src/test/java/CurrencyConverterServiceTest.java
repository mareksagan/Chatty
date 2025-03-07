package com.example.service;

import org.example.dto.CurrencyRatesResponse;
import org.example.exception.CurrencyConversionException;
import org.example.exception.ExternalServiceException;
import org.example.service.CurrencyConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CurrencyConverterService converterService;

    @BeforeEach
    void setup() {
        converterService = new CurrencyConverterService(restTemplate);
    }

    @Test
    void convert_shouldHandleBaseCurrency() {
        mockApiResponse("EUR", Map.of("USD", 1.08, "GBP", 0.85));

        double result = converterService.convert(100, "EUR", "USD");
        assertEquals(108.0, result, 0.001);

        result = converterService.convert(100, "USD", "EUR");
        assertEquals(92.59, result, 0.01);
    }

    @Test
    void convert_shouldHandleCrossCurrency() {
        mockApiResponse("EUR", Map.of("USD", 1.08, "GBP", 0.85));

        double result = converterService.convert(100, "USD", "GBP");
        assertEquals((100 / 1.08) * 0.85, result, 0.01);
    }

    @Test
    void convert_shouldThrowForInvalidCurrency() {
        mockApiResponse("EUR", Map.of("USD", 1.08));

        assertThrows(CurrencyConversionException.class,
                () -> converterService.convert(100, "EUR", "XYZ"));

        assertThrows(CurrencyConversionException.class,
                () -> converterService.convert(100, "XYZ", "EUR"));
    }

    @Test
    void convert_shouldHandleApiFailure() {
        when(restTemplate.getForObject(anyString(), eq(CurrencyRatesResponse.class)))
                .thenReturn(null);

        assertThrows(ExternalServiceException.class,
                () -> converterService.convert(100, "EUR", "USD"));
    }

    private void mockApiResponse(String base, Map<String, Double> rates) {
        CurrencyRatesResponse response = new CurrencyRatesResponse(base, LocalDate.now(), rates);

        when(restTemplate.getForObject(anyString(), eq(CurrencyRatesResponse.class)))
                .thenReturn(response);
    }
}