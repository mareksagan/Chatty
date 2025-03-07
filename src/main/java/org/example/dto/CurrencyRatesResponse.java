package org.example.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class CurrencyRatesResponse {
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;
}