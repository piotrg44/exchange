package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private String message;
}