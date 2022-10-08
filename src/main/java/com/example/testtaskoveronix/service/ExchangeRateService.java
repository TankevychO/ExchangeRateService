package com.example.testtaskoveronix.service;

import java.time.LocalDate;
import java.util.Set;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.dto.response.BestRateResponse;
import com.example.testtaskoveronix.model.dto.response.ExchangeRateResponse;

public interface ExchangeRateService<T> {
    ExchangeRateResponse findExchangeRate();

    ExchangeRateResponse getHistoryExchangeRates(LocalDate date, Long idSource, String currencyName);

    void deleteOldCurrencyExchange(Source source);

    void saveAll(Set<ExchangeRate> currencyExchanges);

    BestRateResponse getBestExchangeRate(String currencyName);
}
