package com.example.testtaskoveronix.service;

import java.util.Set;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;

public interface CurrencyService<D> {
    Set<CurrencyResponse> findAllCurrency();

    Currency findByCode(String code);

    void saveAll(Set<Currency> currencies);
}
