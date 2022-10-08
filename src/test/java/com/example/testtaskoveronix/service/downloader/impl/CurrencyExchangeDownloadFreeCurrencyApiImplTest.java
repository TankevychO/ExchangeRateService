package com.example.testtaskoveronix.service.downloader.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.DateExchangeRate;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.downloader.ExchangeRateCurrentApiResultDto;
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
class CurrencyExchangeDownloadFreeCurrencyApiImplTest {
    @InjectMocks
    private CurrencyExchangeDownloadFreeCurrencyApiImpl currencyExchangeDownloadFreeCurrencyApi;
    private static Source source;
    private static ExchangeRate exchangeRate;
    private static Currency baseCurrency;
    private static Currency currency;
    private static ExchangeCurrentApiDto exchangeCurrentApiDto;
    private static Map<String, ExchangeCurrentApiDto> data;
    private static DateExchangeRate dateExchangeRate;
    private static ExchangeRateCurrentApiResultDto exchangeRateCurrentApiResultDto;

    @BeforeAll
    public static void setup() {
        exchangeRate = new ExchangeRate();
        baseCurrency = new Currency();
        currency = new Currency();
        dateExchangeRate = new DateExchangeRate();
        exchangeCurrentApiDto = new ExchangeCurrentApiDto();
        exchangeRateCurrentApiResultDto = new ExchangeRateCurrentApiResultDto();
        data = new HashMap<>();
        source = new Source("exchangeratesapi", "https://api.apilayer.com/exchangerates_data/symbols",
                "https://api.apilayer.com/exchangerates_data/latest&base=usd", "https://api.apilayer.com/exchangerates_data/");
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

        dateExchangeRate.setDateUpdate("2020-10-15T22:33:12");

        data.put(currency.getCode(), exchangeCurrentApiDto);

        exchangeRateCurrentApiResultDto.setMeta(dateExchangeRate);
        exchangeRateCurrentApiResultDto.setData(data);
    }

    @AfterEach
    void clean() {
        data.clear();
    }

    @Mock
    private HttpClient httpClient;
    @Mock
    private Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter;

    @Test
    void shouldValidDownload() {
        Mockito.when(httpClient.get(source.getUrlExchangeRate(), ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf"))
                .thenReturn(exchangeRateCurrentApiResultDto);
        Mockito.when(converter.toEntity(exchangeCurrentApiDto)).thenReturn(exchangeRate);
        Set<ExchangeRate> actual = currencyExchangeDownloadFreeCurrencyApi.download(source.getUrlExchangeRate(), ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(Set.of(exchangeRate), actual);
    }

    @Test
    void shouldEmptyDownloadWithNullUrl() {
        Set<ExchangeRate> actual = currencyExchangeDownloadFreeCurrencyApi.download(null, ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(new HashSet<>(), actual);
    }

    @Test
    void shouldValidDownloadWithNullResultDto() {

        exchangeRateCurrentApiResultDto.setMeta(null);
        exchangeRateCurrentApiResultDto.setData(null);

        Mockito.when(httpClient.get(source.getUrlExchangeRate(), ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf"))
                .thenReturn(exchangeRateCurrentApiResultDto);

        Set<ExchangeRate> actual = currencyExchangeDownloadFreeCurrencyApi.download(source.getUrlExchangeRate(), ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(new HashSet<>(), actual);
    }

    @Test
    void shouldValidDownloadWithNullFieldsResultDto() {
        exchangeCurrentApiDto.setBaseCurrentCode(null);
        exchangeCurrentApiDto.setCode(null);
        exchangeCurrentApiDto.setValue(null);
        exchangeCurrentApiDto.setSource(null);
        exchangeCurrentApiDto.setIsActual(null);
        exchangeCurrentApiDto.setDate(null);

        dateExchangeRate.setDateUpdate(null);

        data.clear();
        data.put(null, exchangeCurrentApiDto);

        exchangeRateCurrentApiResultDto.setMeta(dateExchangeRate);
        exchangeRateCurrentApiResultDto.setData(data);

        Mockito.when(httpClient.get(source.getUrlExchangeRate(), ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf"))
                .thenReturn(exchangeRateCurrentApiResultDto);

        Set<ExchangeRate> actual = currencyExchangeDownloadFreeCurrencyApi.download(source.getUrlExchangeRate(), ExchangeRateCurrentApiResultDto.class, "gvgjhshahnf", source, null);
        Assertions.assertEquals(new HashSet<>(), actual);
    }
}