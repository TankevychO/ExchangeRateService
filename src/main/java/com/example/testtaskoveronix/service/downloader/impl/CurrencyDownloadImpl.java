package com.example.testtaskoveronix.service.downloader.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.CurrencyResultsDto;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.SourceService;
import com.example.testtaskoveronix.service.converter.Converter;
import com.example.testtaskoveronix.service.downloader.DownloadService;
import com.example.testtaskoveronix.service.downloader.HttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CurrencyDownloadImpl implements DownloadService<CurrencyResultsDto, Currency> {
    private final HttpClient httpClient;
    private final SourceService sourceService;
    private final CurrencyService<Currency> currencyService;
    private final Converter<Currency, CurrencyDtoCurrencyApi, CurrencyResponse> converter;

    @Value("${apikey.currencyapi}")
    private String currencyApiKey;

    @Override
    public Set<Currency> download(String url, Class<? extends CurrencyResultsDto> clazz,
                                  String apiKey,
                                  Source source, Boolean isActual) {
        Set<Currency> currencies = new HashSet<>();
        if (url != null) {
            CurrencyResultsDto currencyResultsDto = httpClient.get(url, CurrencyResultsDto.class, apiKey);
            currencies = currencyResultsDto.getData().entrySet().parallelStream()
                    .filter(e -> e.getValue() != null && e.getKey() != null)
                    .map(e -> converter.toEntity(e.getValue()))
                    .collect(Collectors.toSet());
            currencyService.saveAll(currencies);
        } else log.error("Empty url");
        return currencies;
    }

    @PostConstruct
    public void loadInfo() {
        sourceService.loadData();
        Source source = sourceService.findByName("freecurrencyapi");
        download(source.getUrlCurrency(), CurrencyResultsDto.class, currencyApiKey, source, false);
    }
}
