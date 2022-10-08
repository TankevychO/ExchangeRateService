package com.example.testtaskoveronix.service.downloader.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.downloader.ExchangeRateResultDtoExchangeRateApi;
import com.example.testtaskoveronix.service.converter.Converter;
import com.example.testtaskoveronix.service.downloader.HttpClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CurrencyExchangeDownloadExchangeRateApiImpl extends CurrencyExchangeDownloaderService<ExchangeRateResultDtoExchangeRateApi, ExchangeRate> {


    public CurrencyExchangeDownloadExchangeRateApiImpl(HttpClient httpClient, Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter) {
        super(httpClient, converter);
    }

    @Override
    public Set<ExchangeCurrentApiDto> getExchangeSet(ExchangeRateResultDtoExchangeRateApi dto,
                                                     String sourceName,
                                                     Boolean isActual) {
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String date = dto.getTimestamp() == null ? null : LocalDateTime
                .ofInstant(Instant.ofEpochSecond(dto.getTimestamp()), ZoneOffset.ofHours(0)).format(format);
        String baseCurrency = dto.getBase();
        Set<ExchangeCurrentApiDto> exchangeCurrentApiDtos = new HashSet<>();
        if (dto.getRates() != null)
            dto.getRates().entrySet()
                    .parallelStream()
                    .filter(e -> e.getValue() != null && e.getKey() != null)
                    .forEach(e -> {
                        ExchangeCurrentApiDto newDto = new ExchangeCurrentApiDto();
                        newDto.setDate(date);
                        newDto.setSource(sourceName);
                        newDto.setIsActual(isActual);
                        newDto.setCode(e.getKey());
                        newDto.setValue(e.getValue());
                        newDto.setBaseCurrentCode(baseCurrency);
                        exchangeCurrentApiDtos.add(newDto);
                    });
        return exchangeCurrentApiDtos;
    }
}
