package com.example.testtaskoveronix.service.downloader.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.CurrencyResultsDto;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import com.example.testtaskoveronix.service.CurrencyService;
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
class CurrencyDownloadImplTest {
    @InjectMocks
    private CurrencyDownloadImpl currencyDownload;
    private static Currency currency;
    private static CurrencyResultsDto currencyResultsDto;
    private static Map<String, CurrencyDtoCurrencyApi> data;
    private static CurrencyDtoCurrencyApi currencyDtoCurrencyApi;
    private static Source source;

    @BeforeAll
    public static void setup() {
        currency = new Currency();
        currencyResultsDto = new CurrencyResultsDto();
        currencyDtoCurrencyApi = new CurrencyDtoCurrencyApi();
        data = new HashMap<>();
        source = new Source("freecurrencyapi", "https://api.currencyapi.com/v3/currencies",
                "https://api.currencyapi.com/v3/latest", "https://api.currencyapi.com/v3/historical");
    }

    @BeforeEach
    void init() {
        currency.setId(2L);
        currency.setName("Euro");
        currency.setCode("EUR");
        currencyDtoCurrencyApi.setName("Euro");
        currencyDtoCurrencyApi.setCode("EUR");
        data.put(currency.getCode(), currencyDtoCurrencyApi);
        currencyResultsDto.setData(data);
    }

    @AfterEach
    void clean() {
        data.clear();
    }

    @Mock
    private HttpClient httpClient;
    @Mock
    private CurrencyService<Currency> currencyService;
    @Mock
    private Converter<Currency, CurrencyDtoCurrencyApi, CurrencyResponse> converter;

    @Test
    void shouldValidDownload() {
        Mockito.when(httpClient.get(source.getUrlExchangeRate(), CurrencyResultsDto.class, "gvgjhshahnf"))
                .thenReturn(currencyResultsDto);
        Mockito.when(converter.toEntity(currencyDtoCurrencyApi)).thenReturn(currency);
        Set<Currency> actual = currencyDownload.download(source.getUrlExchangeRate(), CurrencyResultsDto.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(Set.of(currency), actual);
    }

    @Test
    void shouldEmptyDownloadWithNullUrl() {
        Set<Currency> actual = currencyDownload.download(null, CurrencyResultsDto.class, "gvgjhshahnf", source, true);
        Assertions.assertEquals(new HashSet<>(), actual);
    }
}