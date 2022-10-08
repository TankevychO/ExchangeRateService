package com.example.testtaskoveronix.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import com.example.testtaskoveronix.repository.CurrencyRepository;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.converter.Converter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class CurrencyServiceImpl<K> implements CurrencyService<K> {
    private final Converter<Currency, K, CurrencyResponse> currencyConverter;
    private final CurrencyRepository currencyRepository;

    @Override
    public Set<CurrencyResponse> findAllCurrency() {
        return currencyRepository.findAll()
                .parallelStream().map(currencyConverter::toDto).collect(Collectors.toSet());
    }

    @Override
    public Currency findByCode(String code) {
        Optional<Currency> optionalCurrency = currencyRepository.findByCode(code.toUpperCase());
        if (optionalCurrency.isEmpty()) log.warn("Currency with code: " + code + " not found");
        return optionalCurrency.isEmpty() ? null : optionalCurrency.get();
    }

    @Override
    public void saveAll(Set<Currency> currencies) {
        currencyRepository.saveAll(currencies);
    }
}
