package com.example.testtaskoveronix.service.downloader.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.service.converter.Converter;
import com.example.testtaskoveronix.service.downloader.DownloadService;
import com.example.testtaskoveronix.service.downloader.HttpClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public abstract class CurrencyExchangeDownloaderService<R, E> implements DownloadService<R, E> {
    private static final String YOUR_API_KEY = "YOUR-API-KEY";
    private final HttpClient httpClient;
    private final Converter<E, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter;

    @Override
    public Set<E> download(String url, Class<? extends R> clazz, String apiKey, Source source, Boolean isActual) {
        if (url != null) {
            if (url.contains(YOUR_API_KEY)) {
                url = url.replace(YOUR_API_KEY, apiKey);
                apiKey = null;
            }
            R dto = httpClient.get(url, clazz, apiKey);
            return getExchangeSet(dto, source.getName(), isActual).parallelStream()
                    .map(converter::toEntity)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public abstract Set<ExchangeCurrentApiDto> getExchangeSet(R dto, String sourceName, Boolean isActual);
}
