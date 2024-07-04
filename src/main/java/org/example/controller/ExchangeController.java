package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.NbpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExchangeController {

    private final NbpService nbpService;

    @GetMapping("/exchange/{currency}/{amount}/{toCurrency}")
    public ResponseEntity<String> getPersonDetails(@PathVariable String currency,
                                                   @PathVariable String amount,
                                                   @PathVariable String toCurrency) {
        return ResponseEntity.ok(nbpService.exchangeCurrency(currency, amount, toCurrency));
    }
}