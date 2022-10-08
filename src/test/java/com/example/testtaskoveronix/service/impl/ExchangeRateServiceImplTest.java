package com.example.testtaskoveronix.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.dto.response.BestRateResponse;
import com.example.testtaskoveronix.model.dto.response.ExchangeRateResponse;
import com.example.testtaskoveronix.repository.ExchangeRateRepository;
import com.example.testtaskoveronix.service.DownloaderMapper;
import com.example.testtaskoveronix.service.converter.Converter;
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
class ExchangeRateServiceImplTest {
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;
    private static Source source;
    private static ExchangeRate exchangeRate;
    private static Currency baseCurrency;
    private static Currency currency;
    private static ExchangeRateResponse exchangeRateResponse;
    private static ExchangeCurrentApiDto exchangeCurrentApiDto;

    @BeforeAll
    public static void setup() {
        baseCurrency = new Currency();
        currency = new Currency();
        exchangeRate = new ExchangeRate();
        exchangeCurrentApiDto = new ExchangeCurrentApiDto();
        exchangeRateResponse = new ExchangeRateResponse();
        source = new Source("freecurrencyapi", "https://api.currencyapi.com/v3/currencies",
                "https://api.currencyapi.com/v3/latest", "https://api.currencyapi.com/v3/historical");
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

    @AfterEach
    void clean() {
        exchangeRateResponse.getErrors().clear();
        exchangeRateResponse.getExchangeCurrentApiDtos().clear();
    }

    @Mock
    private SourceServiceImpl sourceService;
    @Mock
    private CurrencyServiceImpl<CurrencyDtoCurrencyApi> currencyService;
    @Mock
    private ExchangeRateRepository currencyExchangeRepository;
    @Mock
    private DownloaderMapper downloaderMapper;
    @Mock
    private Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter;

    @Test
    void shouldValidFindExchangeRate() {
        exchangeRateResponse.getExchangeCurrentApiDtos().add(exchangeCurrentApiDto);
        Mockito.when(sourceService.findAll()).thenReturn(List.of(source));
        Mockito.when(currencyExchangeRepository.findAllByIsActual(true)).thenReturn(Set.of(exchangeRate));
        Mockito.when(converter.toDto(exchangeRate)).thenReturn(exchangeCurrentApiDto);
        Mockito.when(downloaderMapper.loadExchangeRate(source.getName(), source.getUrlExchangeRate(),
                new HashMap<>(), source, true)).thenReturn(Set.of(exchangeRate));
        ExchangeRateResponse actual = exchangeRateService.findExchangeRate();
        Assertions.assertEquals(exchangeRateResponse, actual);
    }

    @Test
    void shouldNotFoundErrorSourceFindExchangeRateWithSourcesNull() {
        exchangeRateResponse.getErrors().add("Not found sources");
        Mockito.when(sourceService.findAll()).thenReturn(null);
        ExchangeRateResponse actual = exchangeRateService.findExchangeRate();
        Assertions.assertEquals(0, actual.getExchangeCurrentApiDtos().size());
        Assertions.assertEquals(exchangeRateResponse, actual);
    }

    @Test
    void shouldValidDeleteOldCurrencyExchange() {
        Mockito.when(currencyExchangeRepository.getCurrencyExchangesByIsActualAndSource(true, source))
                .thenReturn(List.of(exchangeRate));
        exchangeRateService.deleteOldCurrencyExchange(source);
        Boolean actual = exchangeRate.getIsActual();
        Assertions.assertFalse(actual);
    }

    @Test
    void shouldValidGetBestCurrencyRate() {
        ExchangeRate exchangeRateMax = new ExchangeRate();
        exchangeRateMax.setId(2L);
        exchangeRateMax.setBaseCurrency(baseCurrency);
        exchangeRateMax.setCurrency(currency);
        exchangeRateMax.setRate(new BigDecimal("3.0123"));
        exchangeRateMax.setSource(source);
        exchangeRateMax.setDate(LocalDateTime.of(2020, 10, 15, 22, 33, 12));
        exchangeRateMax.setIsActual(true);

        ExchangeCurrentApiDto exchangeCurrentMaxApiDto = new ExchangeCurrentApiDto();
        exchangeCurrentMaxApiDto.setBaseCurrentCode("USD");
        exchangeCurrentMaxApiDto.setCode("EUR");
        exchangeCurrentMaxApiDto.setValue(new BigDecimal("3.0123"));
        exchangeCurrentMaxApiDto.setSource("freecurrencyapi");
        exchangeCurrentMaxApiDto.setIsActual(true);
        exchangeCurrentMaxApiDto.setDate("2020-10-15T22:33:12");

        ExchangeRate exchangeRateMedian = new ExchangeRate();
        exchangeRateMedian.setId(2L);
        exchangeRateMedian.setBaseCurrency(baseCurrency);
        exchangeRateMedian.setCurrency(currency);
        exchangeRateMedian.setRate(new BigDecimal("2.0123"));
        exchangeRateMedian.setSource(source);
        exchangeRateMedian.setDate(LocalDateTime.of(2020, 10, 15, 22, 33, 12));
        exchangeRateMedian.setIsActual(true);

        ExchangeCurrentApiDto exchangeCurrentMedianApiDto = new ExchangeCurrentApiDto();
        exchangeCurrentMedianApiDto.setBaseCurrentCode("USD");
        exchangeCurrentMedianApiDto.setCode("EUR");
        exchangeCurrentMedianApiDto.setValue(new BigDecimal("2.0123"));
        exchangeCurrentMedianApiDto.setSource("freecurrencyapi");
        exchangeCurrentMedianApiDto.setIsActual(true);
        exchangeCurrentMedianApiDto.setDate("2020-10-15T22:33:12");

        Mockito.when(currencyService.findByCode("EUR")).thenReturn(currency);
        Mockito.when(currencyExchangeRepository.getExchangeRateByIsActualAndCurrency(true, currency))
                .thenReturn(List.of(exchangeRate, exchangeRateMax, exchangeRateMedian));
        Mockito.when(converter.toDto(exchangeRate)).thenReturn(exchangeCurrentApiDto);
        Mockito.when(converter.toDto(exchangeRateMax)).thenReturn(exchangeCurrentMaxApiDto);
        Mockito.when(converter.toDto(exchangeRateMedian)).thenReturn(exchangeCurrentMedianApiDto);

        BestRateResponse response = exchangeRateService.getBestExchangeRate("EUR");

        Assertions.assertEquals(exchangeCurrentApiDto, response.getBuying());
        Assertions.assertEquals(exchangeCurrentMaxApiDto, response.getSelling());
    }

    @Test
    void shouldUnknownCurrencyErrorCurrencyGetBestCurrencyRateWithUnknownCurrency() {
        BestRateResponse response = new BestRateResponse();
        response.getErrors().add("Unknown currency");
        Mockito.when(currencyService.findByCode("EUR1")).thenReturn(null);
        BestRateResponse actual = exchangeRateService.getBestExchangeRate("EUR1");
        Assertions.assertEquals(response, actual);
    }

    @Test
    void shouldValidGetHistoryExchangeRate() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.getExchangeCurrentApiDtos().add(exchangeCurrentApiDto);
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("date", LocalDate.of(2020, 10, 15).format(format));
        parameters.put("currency", currency.getCode());
        Mockito.when(sourceService.findById(1L)).thenReturn(source);
        Mockito.when(downloaderMapper.loadExchangeRate("freecurrencyapi", source.getUrlHistory(), parameters, source, false))
                .thenReturn(Set.of(exchangeRate));
        Mockito.when(currencyExchangeRepository
                        .getCurrencyExchangesByDateAndSource(LocalDateTime.parse(parameters.get("date") + "T23:59:59"), source))
                .thenReturn(List.of(exchangeRate));
        Mockito.when(converter.toDto(exchangeRate)).thenReturn(exchangeCurrentApiDto);
        ExchangeRateResponse actual = exchangeRateService
                .getHistoryExchangeRates(LocalDate.of(2020, 10, 15), 1L, "EUR");
        Assertions.assertEquals(response, actual);
    }

    @Test
    void shouldUnknownSourceErrorGetHistoryExchangeRateWithUnknownSource() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.getErrors().add("Unknown source");
        Mockito.when(sourceService.findById(3L)).thenReturn(null);
        ExchangeRateResponse actual = exchangeRateService
                .getHistoryExchangeRates(LocalDate.of(2020, 10, 15), 3L, "EUR");
        Assertions.assertEquals(response, actual);
    }

    @Test
    void shouldEmptyDateErrorGetHistoryExchangeRateWithDateNull() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.getErrors().add("Empty date");
        Mockito.when(sourceService.findById(1L)).thenReturn(source);
        ExchangeRateResponse actual = exchangeRateService
                .getHistoryExchangeRates(null, 1L, "EUR");
        Assertions.assertEquals(response, actual);
    }
}