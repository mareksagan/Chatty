package org.example.service;

import org.example.exception.CurrencyConversionException;
import org.example.exception.ExternalServiceException;
import org.example.service.CurrencyConverterService;
import org.example.service.JokeService;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatService {
    private final JokeService jokeService;
    private final CurrencyConverterService currencyConverterService;

    private static final Pattern GREETING_PATTERN = Pattern.compile("(?i)^(hello|hi|hey|greetings).*");
    private static final Pattern JOKE_PATTERN = Pattern.compile("(?i)^(tell me a joke|can you tell me a joke|say a joke).*");
    private static final Pattern CONVERSION_PATTERN1 = Pattern.compile("(?i).*\\bConvert (\\d+(?:\\.\\d+)?) ([A-Za-z]{3}) to ([A-Za-z]{3})\\b.*");
    private static final Pattern CONVERSION_PATTERN2 = Pattern.compile("(?i).*\\bHow much is (\\d+(?:\\.\\d+)?) ([A-Za-z]{3}) in ([A-Za-z]{3})\\b.*");

    public ChatService(JokeService jokeService, CurrencyConverterService currencyConverterService) {
        this.jokeService = jokeService;
        this.currencyConverterService = currencyConverterService;
    }

    public String processMessage(String message) {
        if (isGreeting(message)) {
            return "Hello! How can I assist you today?";
        } else if (isJokeRequest(message)) {
            return jokeService.getRandomJoke();
        } else {
            Matcher matcher1 = CONVERSION_PATTERN1.matcher(message);
            Matcher matcher2 = CONVERSION_PATTERN2.matcher(message);
            if (matcher1.find()) {
                return handleConversion(matcher1);
            } else if (matcher2.find()) {
                return handleConversion(matcher2);
            } else {
                return "I'm sorry, I didn't understand your question. Can you please rephrase it?";
            }
        }
    }

    private boolean isGreeting(String message) {
        return GREETING_PATTERN.matcher(message).matches();
    }

    private boolean isJokeRequest(String message) {
        return JOKE_PATTERN.matcher(message).matches();
    }

    private String handleConversion(Matcher matcher) {
        try {
            double amount = Double.parseDouble(matcher.group(1));
            String from = matcher.group(2).toUpperCase();
            String to = matcher.group(3).toUpperCase();
            double result = currencyConverterService.convert(amount, from, to);
            return String.format("%.2f %s is %.2f %s", amount, from, result, to);
        }
        catch (CurrencyConversionException | ExternalServiceException e) {
            return "Error: " + e.getMessage();
        }
        catch (NumberFormatException e) {
            return "Error: Invalid amount format";
        }
    }
}
