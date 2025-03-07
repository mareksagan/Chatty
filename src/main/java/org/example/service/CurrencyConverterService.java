package org.example.service;

import org.example.dto.CurrencyRatesResponse;
import org.example.exception.CurrencyConversionException;
import org.example.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Service
public class CurrencyConverterService {
    private static final String API_URL = "https://api.frankfurter.app/latest";
    private final RestTemplate restTemplate;
    private CurrencyRates cachedRates;
    private LocalDate lastFetchDate;

    @Autowired
    public CurrencyConverterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private synchronized void fetchRatesIfNeeded() {
        if (lastFetchDate == null || lastFetchDate.isBefore(LocalDate.now())) {
            CurrencyRatesResponse response = restTemplate.getForObject(API_URL, CurrencyRatesResponse.class);
            if (response != null) {
                cachedRates = new CurrencyRates(response.getBase(), response.getRates(), response.getDate());
                lastFetchDate = response.getDate();
            } else {
                throw new ExternalServiceException("Failed to fetch currency rates");
            }
        }
    }

    public double convert(double amount, String from, String to) {
        fetchRatesIfNeeded();
        from = from.toUpperCase();
        to = to.toUpperCase();

        if (from.equals(cachedRates.getBase())) {
            return convertFromBase(amount, to);
        } else if (to.equals(cachedRates.getBase())) {
            return convertToBase(amount, from);
        } else {
            return convertBetweenNonBase(amount, from, to);
        }
    }

    private double convertFromBase(double amount, String to) {
        Double rate = cachedRates.getRates().get(to);
        if (rate == null) {
            throw new CurrencyConversionException("Currency not supported: " + to);
        }
        return amount * rate;
    }

    private double convertToBase(double amount, String from) {
        Double rate = cachedRates.getRates().get(from);
        if (rate == null) {
            throw new CurrencyConversionException("Currency not supported: " + from);
        }
        return amount / rate;
    }

    private double convertBetweenNonBase(double amount, String from, String to) {
        Double fromRate = cachedRates.getRates().get(from);
        Double toRate = cachedRates.getRates().get(to);
        if (fromRate == null) {
            throw new CurrencyConversionException("Currency not supported: " + from);
        }
        if (toRate == null) {
            throw new CurrencyConversionException("Currency not supported: " + to);
        }
        double baseAmount = amount / fromRate;
        return baseAmount * toRate;
    }

    private static class CurrencyRates {
        private final String base;
        private final Map<String, Double> rates;
        private final LocalDate date;

        public CurrencyRates(String base, Map<String, Double> rates, LocalDate date) {
            this.base = base;
            this.rates = rates;
            this.date = date;
        }

        public String getBase() { return base; }
        public Map<String, Double> getRates() { return rates; }
        public LocalDate getDate() { return date; }
    }
}
