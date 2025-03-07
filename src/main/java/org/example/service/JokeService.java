package org.example.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class JokeService {
    static final List<String> JOKES = List.of(
            "Why did the scarecrow win an award? Because he was outstanding in his field!",
            "What do you call fake spaghetti? An impasta!",
            "Why don't skeletons fight each other? They don't have the guts!",
            "What do you get when you cross a snowman and a vampire? Frostbite!",
            "Why did the math book look sad? Because it had too many problems."
    );

    private final Random random = new Random();

    public String getRandomJoke() {
        return JOKES.get(random.nextInt(JOKES.size()));
    }
}
