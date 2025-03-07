package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesResponse {
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;
}