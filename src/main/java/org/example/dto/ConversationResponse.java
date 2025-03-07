package org.example.dto;

import lombok.Data;

@Data
public class ConversationResponse {
    private String answer;

    public ConversationResponse(String answer) {
        this.answer = answer;
    }
}
