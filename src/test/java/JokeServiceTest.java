package org.example.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JokeServiceTest {

    @Test
    void getRandomJoke_shouldReturnValidJoke() {
        JokeService jokeService = new JokeService();
        List<String> jokes = JokeService.JOKES;

        for (int i = 0; i < 10; i++) {
            String joke = jokeService.getRandomJoke();
            assertTrue(jokes.contains(joke),
                    "Returned joke should be from predefined list");
        }
    }
}