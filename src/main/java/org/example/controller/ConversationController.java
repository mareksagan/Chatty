package org.example.controller;

import org.example.dto.ConversationRequest;
import org.example.dto.ConversationResponse;
import org.example.dto.QuestionsResponse;
import org.example.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConversationController {

    private final ChatService chatService;

    public ConversationController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/questions")
    public ResponseEntity<QuestionsResponse> getQuestions() {
        List<String> questions = Arrays.asList(
                "Can you tell me a joke?",
                "Tell me a joke",
                "Convert <amount> <from> to <to>",
                "How much is <amount> <from> in <to>",
                "Hello",
                "Hi"
        );
        return ResponseEntity.ok(new QuestionsResponse(questions));
    }

    @PostMapping("/conversation")
    public ResponseEntity<ConversationResponse> handleConversation(@RequestBody ConversationRequest request) {
        String answer = chatService.processMessage(request.getMessage());
        return ResponseEntity.ok(new ConversationResponse(answer));
    }
}
