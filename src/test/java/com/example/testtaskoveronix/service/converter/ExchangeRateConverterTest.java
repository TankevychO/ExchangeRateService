package com.example.testtaskoveronix.service.converter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.SourceService;
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
class ExchangeRateConverterTest {
    @InjectMocks
    private ExchangeRateConverter exchangeRateConverter;
    private static ExchangeRate exchangeRate;
    private static ExchangeCurrentApiDto exchangeCurrentApiDto;
    private static Source source;
    private static Currency baseCurrency;
    private static Currency currency;

    @BeforeAll
    public static void setup() {
        baseCurrency = new Currency();
        currency = new Currency();
        exchangeRate = new ExchangeRate();
        source = new Source("freecurrencyapi", "https://api.currencyapi.com/v3/currencies",
                "https://api.currencyapi.com/v3/latest", "https://api.currencyapi.com/v3/historical");
        exchangeCurrentApiDto = new ExchangeCurrentApiDto();
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

        exchangeCurrentApiDto.setBaseCurrentCode("USD");
        exchangeCurrentApiDto.setCode("EUR");
        exchangeCurrentApiDto.setValue(new BigDecimal("1.0123"));
        exchangeCurrentApiDto.setSource("freecurrencyapi");
        exchangeCurrentApiDto.setIsActual(true);
        exchangeCurrentApiDto.setDate("2020-10-15T22:33:12");
    }

    @Mock
    private CurrencyService<CurrencyDtoCurrencyApi> currencyService;
    @Mock
    private SourceService sourceService;

    @Test
    void shouldValidConvertExchangeRateToDto() {
        ExchangeCurrentApiDto actual = exchangeRateConverter.toDto(exchangeRate);
        Assertions.assertEquals(exchangeCurrentApiDto, actual);
    }

    @Test
    void shouldValidConvertDtoToExchangeRate() {
        Mockito.when(sourceService.findByName("freecurrencyapi")).thenReturn(source);
        Mockito.when(currencyService.findByCode("USD")).thenReturn(baseCurrency);
        Mockito.when(currencyService.findByCode("EUR")).thenReturn(currency);
        ExchangeRate actual = exchangeRateConverter.toEntity(exchangeCurrentApiDto);
        Assertions.assertEquals(exchangeRate, actual);
    }

    @Test
    void shouldNullConvertDtoToExchangeRate() {
        ExchangeRate actual = exchangeRateConverter.toEntity(null);
        Assertions.assertNull(actual);
    }

    @Test
    void shouldNullConvertExchangeRateToDto() {
        ExchangeCurrentApiDto actual = exchangeRateConverter.toDto(null);
        Assertions.assertNull(actual);
    }

    @Test
    void shouldValidConvertExchangeRateWithNullFieldsToDto() {
        exchangeRate.setId(null);
        exchangeRate.setBaseCurrency(null);
        exchangeRate.setCurrency(null);
        exchangeRate.setRate(null);
        exchangeRate.setSource(null);
        exchangeRate.setDate(null);
        exchangeRate.setIsActual(null);

        exchangeCurrentApiDto.setBaseCurrentCode(null);
        exchangeCurrentApiDto.setCode(null);
        exchangeCurrentApiDto.setValue(null);
        exchangeCurrentApiDto.setSource(null);
        exchangeCurrentApiDto.setIsActual(null);
        exchangeCurrentApiDto.setDate(null);
        ExchangeCurrentApiDto actual = exchangeRateConverter.toDto(exchangeRate);
        Assertions.assertEquals(exchangeCurrentApiDto, actual);
    }

    @Test
    void shouldValidConvertDtoWithNullFieldsToExchangeRate() {
        exchangeRate.setId(null);
        exchangeRate.setBaseCurrency(null);
        exchangeRate.setCurrency(null);
        exchangeRate.setRate(null);
        exchangeRate.setSource(null);
        exchangeRate.setDate(null);
        exchangeRate.setIsActual(null);

        exchangeCurrentApiDto.setBaseCurrentCode(null);
        exchangeCurrentApiDto.setCode(null);
        exchangeCurrentApiDto.setValue(null);
        exchangeCurrentApiDto.setSource(null);
        exchangeCurrentApiDto.setIsActual(null);
        exchangeCurrentApiDto.setDate(null);
        ExchangeRate actual = exchangeRateConverter.toEntity(exchangeCurrentApiDto);
        Assertions.assertEquals(exchangeRate, actual);
    }
}