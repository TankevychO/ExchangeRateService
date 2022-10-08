package com.example.testtaskoveronix.service.converter;

import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurrencyConverterTest {
    private static CurrencyConverter currencyConverter;
    private static Currency currency;
    private static CurrencyResponse currencyResponse;
    private static CurrencyDtoCurrencyApi currencyDtoCurrencyApi;

    @BeforeAll
    public static void setup() {
        currencyConverter = new CurrencyConverter();
        currency = new Currency();
        currencyResponse = new CurrencyResponse();
        currencyDtoCurrencyApi = new CurrencyDtoCurrencyApi();
    }

    @BeforeEach
    void init() {
        currency.setId(2L);
        currency.setName("Euro");
        currency.setCode("EUR");

        currencyResponse.setName("Euro");
        currencyResponse.setCode("EUR");

        currencyDtoCurrencyApi.setName("Euro");
        currencyDtoCurrencyApi.setCode("EUR");
    }

    @Test
    void shouldValidConvertCurrencyToDto() {
        CurrencyResponse actual = currencyConverter.toDto(currency);
        Assertions.assertEquals(currencyResponse, actual);
    }

    @Test
    void shouldValidConvertDtoToCurrency() {
        Currency actual = currencyConverter.toEntity(currencyDtoCurrencyApi);
        Assertions.assertEquals(currency, actual);
    }

    @Test
    void shouldNullConvertDtoNullToCurrency() {
        Currency actual = currencyConverter.toEntity(null);
        Assertions.assertNull(actual);
    }

    @Test
    void shouldNullConvertCurrencyNullToDto() {
        CurrencyResponse actual = currencyConverter.toDto(null);
        Assertions.assertNull(actual);
    }

    @Test
    void shouldValidConvertCurrencyNullFieldsToDto() {
        currency.setName(null);
        currency.setCode(null);

        currencyResponse.setName(null);
        currencyResponse.setCode(null);
        CurrencyResponse actual = currencyConverter.toDto(currency);
        Assertions.assertEquals(currencyResponse, actual);
    }

    @Test
    void shouldValidConvertDtoWithNullFieldsToCurrency() {
        currency.setName(null);
        currency.setCode(null);

        currencyDtoCurrencyApi.setName(null);
        currencyDtoCurrencyApi.setCode(null);

        Currency actual = currencyConverter.toEntity(currencyDtoCurrencyApi);
        Assertions.assertEquals(currency, actual);
    }
}