package org.example.service;

import org.example.configuration.ConfigProperties;
import org.example.dto.ExchangeRateResponse;
import org.example.exception.NoSuchInformationFromNbpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NbpServiceTest {

    private static final String NBP_MOCK_URL = "http://mockedurl.com/exchangerates/rates/a";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ConfigProperties configProperties;

    @InjectMocks
    private NbpService nbpService;

    @BeforeEach
    void setUp() {
        when(configProperties.getAddress()).thenReturn("http://mockedurl.com");
    }

    @Test
    void testExchangeCurrencyPLNtoUSD() {
        // given
        when(restTemplate.getForObject(NBP_MOCK_URL + "/USD", ExchangeRateResponse.class))
                .thenReturn(createMockExchangeRateResponse(new BigDecimal("4.00")));

        // when
        String result = nbpService.exchangeCurrency("PLN", "100", "USD");

        // then
        assertEquals("25.00", result);
    }

    @Test
    void testExchangeCurrencyUSDtoPLN() {
        // given
        when(restTemplate.getForObject(NBP_MOCK_URL + "/USD", ExchangeRateResponse.class))
                .thenReturn(createMockExchangeRateResponse(new BigDecimal("4.00")));

        // when
        String result = nbpService.exchangeCurrency("USD", "25", "PLN");

        // then
        assertEquals("100.00", result);
    }

    @Test
    void testExchangeCurrencyEURtoUSD() {
        // given
        when(restTemplate.getForObject(NBP_MOCK_URL + "/USD", ExchangeRateResponse.class))
                .thenReturn(createMockExchangeRateResponse(new BigDecimal("4")));
        when(restTemplate.getForObject(NBP_MOCK_URL + "/EUR", ExchangeRateResponse.class))
                .thenReturn(createMockExchangeRateResponse(new BigDecimal("4.3")));

        // when
        String result = nbpService.exchangeCurrency("EUR", "50", "USD");

        // then
        assertEquals("53.75", result);
    }

    @Test
    void testExchangeCurrencyUSDtoEUR() {
        // given
        when(restTemplate.getForObject(NBP_MOCK_URL + "/USD", ExchangeRateResponse.class))
                .thenReturn(createMockExchangeRateResponse(new BigDecimal("4.00")));
        when(restTemplate.getForObject(NBP_MOCK_URL + "/EUR", ExchangeRateResponse.class))
                .thenReturn(createMockExchangeRateResponse(new BigDecimal("4.3")));

        // when
        String result = nbpService.exchangeCurrency("USD", "60", "EUR");

        // then
        assertEquals("55.81", result);
    }

    @Test
    void testShouldThrowNoSuchInformationFromNbpException() {
        // given
        when(restTemplate.getForObject(NBP_MOCK_URL + "/USD", ExchangeRateResponse.class))
                .thenReturn(null);

        // when
        assertThrows(NoSuchInformationFromNbpException.class, () ->
                nbpService.exchangeCurrency("PLN", "100", "USD"));
    }

    private ExchangeRateResponse createMockExchangeRateResponse(BigDecimal rate) {
        ExchangeRateResponse response = new ExchangeRateResponse();
        ExchangeRateResponse.Rate rateObj = new ExchangeRateResponse.Rate();
        rateObj.setMid(rate);
        response.setRates(Collections.singletonList(rateObj));
        return response;
    }
}