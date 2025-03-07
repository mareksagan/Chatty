package org.example.dto;

import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRates {
    private final String base;
    private final Map<String, Double> rates;
    private final LocalDate date;
}
