package com.example.testtaskoveronix.service.downloader.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.downloader.ExchangeRateCurrentApiResultDto;
import com.example.testtaskoveronix.service.converter.Converter;
import com.example.testtaskoveronix.service.downloader.HttpClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CurrencyExchangeDownloadFreeCurrencyApiImpl extends
        CurrencyExchangeDownloaderService<ExchangeRateCurrentApiResultDto, ExchangeRate> {

    public CurrencyExchangeDownloadFreeCurrencyApiImpl(HttpClient httpClient, Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter) {
        super(httpClient, converter);
    }

    @Override
    public Set<ExchangeCurrentApiDto> getExchangeSet(ExchangeRateCurrentApiResultDto dto,
                                                     String sourceName,
                                                     Boolean isActual) {
        Set<ExchangeCurrentApiDto> exchangeCurrentApiDtos = new HashSet<>();
        if (dto.getData() != null)
            exchangeCurrentApiDtos.addAll(dto.getData().entrySet()
                    .parallelStream()
                    .filter(e -> e.getValue() != null && e.getKey() != null)
                    .map(Map.Entry::getValue)
                    .peek(e -> {
                        e.setIsActual(isActual);
                        if (dto.getMeta() != null)
                            e.setDate(dto.getMeta().getDateUpdate());
                        e.setBaseCurrentCode("USD");
                        e.setSource(sourceName);
                    }).collect(Collectors.toSet()));
        return exchangeCurrentApiDtos;
    }
}
