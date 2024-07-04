package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;
    }
}