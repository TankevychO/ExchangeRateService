package com.example.testtaskoveronix.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.ExchangeRateCurrentApiResultDto;
import com.example.testtaskoveronix.model.downloader.ExchangeRateResultDtoExchangeRateApi;
import com.example.testtaskoveronix.service.downloader.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class DownloaderMapper {
    private final DownloadService<ExchangeRateCurrentApiResultDto, ExchangeRate> freeCurrentApiDownloader;
    private final DownloadService<ExchangeRateResultDtoExchangeRateApi, ExchangeRate> exchangeRateApiDownload;

    @Value("${apikey.currencyapi}")
    private String currencyApiKey;

    @Value("${apikey.exchangeratesapi}")
    private String exchangeApiKey;

    @Value("${apikey.exchangerate-api}")
    private String exchangeComApiKey;

    public Set<ExchangeRate> loadExchangeRate(String sourceName, String url,
                                              Map<String, String> parameters,
                                              Source source, Boolean isActual) {
        Set<ExchangeRate> exchangeRates = new HashSet<>();
        switch (sourceName) {
            case ("freecurrencyapi"):
                if (parameters.get("date") != null)
                    url = url + "?date=" + parameters.get("date");
                exchangeRates.addAll(freeCurrentApiDownloader.download(url, ExchangeRateCurrentApiResultDto.class,
                        currencyApiKey, source, isActual));
                log.info("Downloaded info from source with name: freecurrencyapi");
                break;
            case ("exchangeratesapi"):
                if (parameters.get("date") != null)
                    url = url + parameters.get("date");
                if (parameters.get("currency") != null)
                    url = url + "?" + "symbols=" + parameters.get("currency");
                exchangeRates.addAll(exchangeRateApiDownload.download(url, ExchangeRateResultDtoExchangeRateApi.class,
                        exchangeApiKey, source, isActual));
                log.info("Downloaded info from source with name: exchangeratesapi");
                break;
            case ("exchangerate-api.com"):
                if (parameters.get("date") != null) {
                    String[] date = parameters.get("date").split("-");
                    url = url + date[0] + "/" + date[1] + "/" + date[2];
                }
                exchangeRates.addAll(exchangeRateApiDownload.download(url, ExchangeRateResultDtoExchangeRateApi.class,
                        exchangeComApiKey, source, isActual));
                log.info("Downloaded info from source with name: exchangerate-api.com");
                break;
            default:
                log.info("Source with name: " + sourceName + " not found");
                break;
        }
        return exchangeRates;
    }
}
