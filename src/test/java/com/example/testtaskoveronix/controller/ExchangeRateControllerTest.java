package com.example.testtaskoveronix.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Set;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.dto.response.BestRateResponse;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import com.example.testtaskoveronix.model.dto.response.ExchangeRateResponse;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.ExchangeRateService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.Data;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ExchangeRateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static ExchangeRateResponse exchangeRateResponse;
    private static ExchangeCurrentApiDto exchangeCurrentApiDto;

    @BeforeAll
    public static void setup() {
        exchangeRateResponse = new ExchangeRateResponse();
        exchangeCurrentApiDto = new ExchangeCurrentApiDto();
    }

    @BeforeEach
    void init() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        exchangeCurrentApiDto.setBaseCurrentCode("USD");
        exchangeCurrentApiDto.setCode("EUR");
        exchangeCurrentApiDto.setValue(BigDecimal.valueOf(1.0123));
        exchangeCurrentApiDto.setSource("freecurrencyapi");
        exchangeCurrentApiDto.setIsActual(true);
        exchangeCurrentApiDto.setDate("2020-10-15T22:33:12");
    }

    @AfterEach
    void clean() {
        exchangeRateResponse.getErrors().clear();
        exchangeRateResponse.getExchangeCurrentApiDtos().clear();
    }

    @MockBean
    private CurrencyService<CurrencyDtoCurrencyApi> currencyService;
    @MockBean
    private ExchangeRateService<ExchangeCurrentApiDto> exchangeRateService;

    @Test
    void shouldShowAllCurrencies() {
        CurrencyResponse currencyResponse = new CurrencyResponse();
        currencyResponse.setName("US Dollar");
        currencyResponse.setCode("USD");
        Set<CurrencyResponse> currencies = Set.of(currencyResponse);
        Mockito.when(currencyService.findAllCurrency()).thenReturn(currencies);
        RestAssuredMockMvc.when()
                .get("/test-task-over-onix/currency")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(1))
                .body("[0].code", Matchers.equalTo("USD"))
                .body("[0].name", Matchers.equalTo("US Dollar"));
    }

    @Test
    void shouldShowExchangeRates() {
        exchangeRateResponse.getExchangeCurrentApiDtos().add(exchangeCurrentApiDto);
        Mockito.when(exchangeRateService.findExchangeRate()).thenReturn(exchangeRateResponse);
        RestAssuredMockMvc.when()
                .get("/test-task-over-onix/exchange-rate")
                .then()
                .statusCode(200)
                .body("exchangeCurrentApiDtos.size()", Matchers.equalTo(1))
                .body("exchangeCurrentApiDtos[0].code", Matchers.equalTo("EUR"))
                .body("exchangeCurrentApiDtos[0].value", Matchers.equalTo(1.0123F))
                .body("exchangeCurrentApiDtos[0].isActual", Matchers.equalTo(true));
    }

    @Test
    void shouldShowExchangeRateWithError() {
        exchangeRateResponse.getErrors().add("Not found sources");
        Mockito.when(exchangeRateService.findExchangeRate()).thenReturn(exchangeRateResponse);
        RestAssuredMockMvc.when()
                .get("/test-task-over-onix/exchange-rate")
                .then()
                .statusCode(200)
                .body("errors.size()", Matchers.equalTo(1))
                .body("errors[0]", Matchers.equalTo("Not found sources"));
    }

    @Test
    void shouldShowBestRate() {
        ExchangeCurrentApiDto exchangeCurrentMaxApiDto = new ExchangeCurrentApiDto();
        exchangeCurrentMaxApiDto.setBaseCurrentCode("USD");
        exchangeCurrentMaxApiDto.setCode("EUR");
        exchangeCurrentMaxApiDto.setValue(new BigDecimal("3.0123"));
        exchangeCurrentMaxApiDto.setSource("freecurrencyapi");
        exchangeCurrentMaxApiDto.setIsActual(true);
        exchangeCurrentMaxApiDto.setDate("2020-10-15T22:33:12");

        BestRateResponse bestRateResponse = new BestRateResponse();
        bestRateResponse.setBuying(exchangeCurrentApiDto);
        bestRateResponse.setSelling(exchangeCurrentMaxApiDto);

        Mockito.when(exchangeRateService.getBestExchangeRate("USD")).thenReturn(bestRateResponse);
        RestAssuredMockMvc
                .when()
                .get("/test-task-over-onix/best-rate/{currency}", "USD")
                .then()
                .statusCode(200)
                .body("buying.value", Matchers.equalTo(1.0123F))
                .body("selling.value", Matchers.equalTo(3.0123F));
    }

    @Test
    void shouldShowBestRateWithError() {
        BestRateResponse bestRateResponse = new BestRateResponse();
        bestRateResponse.getErrors().add("Unknown currency");

        Mockito.when(exchangeRateService.getBestExchangeRate("EUR1")).thenReturn(bestRateResponse);
        RestAssuredMockMvc
                .when()
                .get("/test-task-over-onix/best-rate/{currency}", "EUR1")
                .then()
                .statusCode(200)
                .body("errors.size()", Matchers.equalTo(1))
                .body("errors[0]", Matchers.equalTo("Unknown currency"));
    }
}