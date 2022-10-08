package com.example.testtaskoveronix.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.model.dto.response.BestRateResponse;
import com.example.testtaskoveronix.model.dto.response.ExchangeRateResponse;
import com.example.testtaskoveronix.repository.ExchangeRateRepository;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.DownloaderMapper;
import com.example.testtaskoveronix.service.ExchangeRateService;
import com.example.testtaskoveronix.service.SourceService;
import com.example.testtaskoveronix.service.converter.Converter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService<ExchangeCurrentApiDto> {
    private final SourceService sourceService;
    private final DownloaderMapper downloaderMapper;
    private final ExchangeRateRepository currencyExchangeRepository;
    private final CurrencyService<CurrencyDtoCurrencyApi> currencyService;
    private final Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> converter;

    @Override
    public ExchangeRateResponse findExchangeRate() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        List<Source> sources = sourceService.findAll();
        if (sources == null) response.getErrors().add("Not found sources");
        else {
            sources.forEach(source -> {
                deleteOldCurrencyExchange(source);
                saveAll(downloaderMapper.loadExchangeRate(source.getName(),
                        source.getUrlExchangeRate(),
                        new HashMap<>(), source, true));
            });
            Set<ExchangeCurrentApiDto> exchangeCurrentApiDtos = currencyExchangeRepository.findAllByIsActual(true)
                    .parallelStream()
                    .map(converter::toDto)
                    .collect(Collectors.toSet());
            response.getExchangeCurrentApiDtos().addAll(exchangeCurrentApiDtos);
        }
        return response;
    }

    @Override
    public ExchangeRateResponse getHistoryExchangeRates(LocalDate date, Long idSource, String currencyName) {
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
        ExchangeRateResponse response = new ExchangeRateResponse();
        Source source = sourceService.findById(idSource);
        if (source == null) response.getErrors().add("Unknown source");
        if (date == null) response.getErrors().add("Empty date");
        if (response.getErrors().isEmpty()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("date", date.format(format));
            if (currencyName != null)
                parameters.put("currency", currencyName);
            saveAll(downloaderMapper.loadExchangeRate(source.getName(), source.getUrlHistory(), parameters, source, false));
            Set<ExchangeCurrentApiDto> exchangeRateResponses = currencyExchangeRepository
                    .getCurrencyExchangesByDateAndSource(LocalDateTime.parse(date.format(format) + "T23:59:59"), source)
                    .parallelStream()
                    .map(converter::toDto)
                    .filter(e -> currencyName == null || Objects.equals(e.getCode(), currencyName.toUpperCase()))
                    .collect(Collectors.toSet());
            response.getExchangeCurrentApiDtos().addAll(exchangeRateResponses);
        }
        return response;
    }

    @Override
    public void deleteOldCurrencyExchange(Source source) {
        List<ExchangeRate> currencyExchanges = currencyExchangeRepository.getCurrencyExchangesByIsActualAndSource(true, source);
        for (ExchangeRate currencyExchange : currencyExchanges) {
            currencyExchange.setIsActual(false);
        }
        currencyExchangeRepository.saveAll(currencyExchanges);
    }

    @Override
    public void saveAll(Set<ExchangeRate> currencyExchanges) {
        currencyExchangeRepository.saveAll(currencyExchanges);
    }

    @Override
    public BestRateResponse getBestExchangeRate(String currencyName) {
        BestRateResponse response = new BestRateResponse();
        Currency currency = currencyService.findByCode(currencyName.toUpperCase());
        if (currency == null) response.getErrors().add("Unknown currency");
        else {
            List<ExchangeCurrentApiDto> exchangeCurrentApiDtos = currencyExchangeRepository.getExchangeRateByIsActualAndCurrency(true, currency)
                    .parallelStream()
                    .sorted()
                    .map(converter::toDto)
                    .collect(Collectors.toList());
            if (!exchangeCurrentApiDtos.isEmpty()) {
                response.setSelling(exchangeCurrentApiDtos.get(0));
                response.setBuying(exchangeCurrentApiDtos.get(exchangeCurrentApiDtos.size() - 1));
            }
        }
        return response;
    }
}
