package com.example.testtaskoveronix.service.downloader.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.downloader.ExchangeRateResultDtoExchangeRateApi;
import com.example.testtaskoveronix.service.converter.Converter;
import com.example.testtaskoveronix.service.downloader.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeDownloadExchangeRateApiImplTest {
    @InjectMocks
    private CurrencyExchangeDownloadExchangeRateApiImpl exchangeRateApiDownload;
    private static Source source;
    private static ExchangeRate exchangeRate;
    private static Currency baseCurrency;
    private static Currency currency;
    private static ExchangeCurrentApiDto exchangeCurrentApiDto;
    private static Map<String, BigDecimal> rate;
    private static ExchangeRateResultDtoExchangeRateApi exchangeRateResultDtoExchangeRateApi;

    @BeforeAll
    public static void setup() {
        baseCurrency = new Currency();
        currency = new Currency();
        exchangeRate = new ExchangeRate();
        exchangeCurrentApiDto = new ExchangeCurrentApiDto();
        exchangeRateResultDtoExchangeRateApi = new ExchangeRateResultDtoExchangeRateApi();
        source = new Source("freecurrencyapi", "https://api.currencyapi.com/v3/currencies",
                "https://api.currencyapi.com/v3/latest", "https://api.currencyapi.com/v3/historical");
        rate = new HashMap<>();
    }

    @BeforeEach
    void init() {
        baseCurrency.setId(1L);
        baseCurrency.setName("US Dollar");
        baseCurrency.setCode("USD");

        currency.setId(2L);
        currency.setName("Euro");
        currency.setCode("EUR");

        exchangeRate.setId(1L);
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setCurrency(currency);
        exchangeRate.setRate(new BigDecimal("1.0123"));
        exchangeRate.setSource(source);
        exchangeRate.setDate(LocalDateTime.of(2020, 10, 15, 22, 33, 12));
        exchangeRate.setIsActual(true);

        exchangeCurrentApiDto.setBaseCurrentCode(baseCurrency.getCode());
        exchangeCurrentApiDto.setCode(currency.getCode());
        exchangeCurrentApiDto.setValue(new BigDecimal("1.0123"));
        exchangeCurrentApiDto.setSource(source.getName());
        exchangeCurrentApiDto.setIsActual(true);
        exchangeCurrentApiDto.setDate("2020-10-15T22:33:12");

        rate.put(currency.getCode(), new BigDecimal("1.0123"));

        exchangeRateResultDtoExchangeRateApi.setRates(rate);
        exchangeRateResultDtoExchangeRateApi.setDate(LocalDate.of(2020, 10, 15).toString());
        exchangeRateResultDtoExchangeRateApi.setBase(baseCurrency.getCode());
        exchangeRateResultDtoExchangeRateApi.setTimestamp(1602801192);
    }

    @AfterEach
    void clean() {
        rate.clear();
    }

    @Mock
    private HttpClient httpClient;
    @Mock
    private Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter;

    @Test
    void shouldValidDownload() {
        Mockito.when(httpClient.get(source.getUrlExchangeRate(), ExchangeRateResultDtoExchangeRateApi.class, "gvgjhshahnf"))
                .thenReturn(exchangeRateResultDtoExchangeRateApi);
        Mockito.when(converter.toEntity(exchangeCurrentApiDto)).thenReturn(exchangeRate);
        Set<ExchangeRate> actual = exchangeRateApiDownload.download(source.getUrlExchangeRate(), ExchangeRateResultDtoExchangeRateApi.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(Set.of(exchangeRate), actual);
    }

    @Test
    void shouldEmptyDownloadWithNullUrl() {
        Set<ExchangeRate> actual = exchangeRateApiDownload.download(null, ExchangeRateResultDtoExchangeRateApi.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(new HashSet<>(), actual);
    }

    @Test
    void shouldValidDownloadWithNullFieldsDto() {
        exchangeRate.setBaseCurrency(null);
        exchangeRate.setCurrency(null);
        exchangeRate.setRate(null);
        exchangeRate.setSource(null);
        exchangeRate.setDate(null);
        exchangeRate.setIsActual(null);

        exchangeRateResultDtoExchangeRateApi.setRates(null);
        exchangeRateResultDtoExchangeRateApi.setDate(null);
        exchangeRateResultDtoExchangeRateApi.setBase(null);
        exchangeRateResultDtoExchangeRateApi.setTimestamp(null);

        exchangeCurrentApiDto.setBaseCurrentCode(null);
        exchangeCurrentApiDto.setCode(null);
        exchangeCurrentApiDto.setValue(null);
        exchangeCurrentApiDto.setSource(null);
        exchangeCurrentApiDto.setIsActual(null);
        exchangeCurrentApiDto.setDate(null);

        Mockito.when(httpClient.get(source.getUrlExchangeRate(), ExchangeRateResultDtoExchangeRateApi.class, "gvgjhshahnf"))
                .thenReturn(exchangeRateResultDtoExchangeRateApi);

        Set<ExchangeRate> actual = exchangeRateApiDownload.download(source.getUrlExchangeRate(), ExchangeRateResultDtoExchangeRateApi.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(new HashSet<>(), actual);
    }
}