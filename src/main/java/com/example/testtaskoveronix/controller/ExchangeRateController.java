package com.example.testtaskoveronix.controller;

import java.time.LocalDate;
import java.util.Set;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.dto.response.BestRateResponse;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import com.example.testtaskoveronix.model.dto.response.ExchangeRateResponse;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-task-over-onix")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final CurrencyService<CurrencyDtoCurrencyApi> currencyService;
    private final ExchangeRateService<ExchangeCurrentApiDto> exchangeRateService;

    @GetMapping("/currency")
    public ResponseEntity<Set<CurrencyResponse>> getAllCurrency() {
        Set<CurrencyResponse> currencies = currencyService.findAllCurrency();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/exchange-rate")
    public ResponseEntity<ExchangeRateResponse> getLatelyExchangeRates() {
        ExchangeRateResponse response = exchangeRateService.findExchangeRate();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<ExchangeRateResponse> getHistoryExchangeRates(@RequestParam(defaultValue = "1") Long idSource,
                                                                        @RequestParam LocalDate date,
                                                                        @RequestParam(required = false) String currency) {
        ExchangeRateResponse response = exchangeRateService
                .getHistoryExchangeRates(date, idSource, currency);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/best-rate/{currency}")
    public ResponseEntity<BestRateResponse> getBestExchangeRates(@PathVariable(name = "currency") String currencyName) {
        getLatelyExchangeRates();
        BestRateResponse response = exchangeRateService.getBestExchangeRate(currencyName);
        return ResponseEntity.ok(response);
    }
}
