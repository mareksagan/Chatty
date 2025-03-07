package org.example.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionsResponse {
    private List<String> questions;

    public QuestionsResponse(List<String> questions) {
        this.questions = questions;
    }
}