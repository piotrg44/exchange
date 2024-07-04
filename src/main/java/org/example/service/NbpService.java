package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.configuration.ConfigProperties;
import org.example.dto.ExchangeRateResponse;
import org.example.exception.NoSuchInformationFromNbpException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class NbpService {

    private final RestTemplate restTemplate;
    private final ConfigProperties configProperties;

    public BigDecimal getExchangeRates(String currencyCode) {
        var url = String.format("%s/exchangerates/rates/a/%s", configProperties.getAddress(), currencyCode);
        var exchangeRateResponse = restTemplate.getForObject(url, ExchangeRateResponse.class);
        if (exchangeRateResponse == null || exchangeRateResponse.getRates() == null || exchangeRateResponse.getRates().isEmpty()) {
            throw new NoSuchInformationFromNbpException("Data from NBP API is not correct");
        }
        return exchangeRateResponse.getRates().get(0).getMid();
    }

    public String exchangeCurrency(String currency, String amount, String toCurrency) {
        if (currency.equals("PLN")) {
            return new BigDecimal(amount).divide(getExchangeRates(toCurrency), 2, RoundingMode.HALF_UP).toString();
        } else if (toCurrency.equals("PLN")) {
            return getExchangeRates(currency).multiply(new BigDecimal(amount)).toString();
        } else {
            var exchangedAmount = getExchangeRates(currency).multiply(new BigDecimal(amount));
            return exchangedAmount.divide(getExchangeRates(toCurrency), 2, RoundingMode.HALF_UP).toString();
        }
    }
}