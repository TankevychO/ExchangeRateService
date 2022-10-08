package com.example.testtaskoveronix.service.impl;

import java.util.Optional;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.repository.CurrencyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {
    @InjectMocks
    private CurrencyServiceImpl<Currency> currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void shouldValidFindByCodeCurrency() {
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("US Dollar");
        currency.setCode("USD");
        Optional<Currency> optionalCurrency = Optional.of(currency);
        Mockito.when(currencyRepository.findByCode("USD")).thenReturn(optionalCurrency);
        Currency actual = currencyService.findByCode("USD");
        Assertions.assertEquals(currency, actual);
    }

    @Test
    void shouldNullFindByCodeCurrency() {
        Mockito.when(currencyRepository.findByCode("USD1")).thenReturn(Optional.empty());
        Currency actual = currencyService.findByCode("USD1");
        Assertions.assertNull(actual);
    }
}