package com.example.EmailCategorizer.dto;

import java.util.List;

public record AIBatchRequest(
        List<AIBatchEmail> emails) {
}
