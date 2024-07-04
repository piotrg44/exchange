package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDetailsResponse {
    private String currency;
    private String amount;
}