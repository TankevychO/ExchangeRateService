package com.example.testtaskoveronix.service.converter;

import java.time.LocalDateTime;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.example.testtaskoveronix.service.CurrencyService;
import com.example.testtaskoveronix.service.SourceService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExchangeRateConverter implements Converter<ExchangeRate, ExchangeCurrentApiDto, ExchangeCurrentApiDto> {
    private final CurrencyService<CurrencyDtoCurrencyApi> currencyService;
    private final SourceService sourceService;

    @Override
    public ExchangeRate toEntity(ExchangeCurrentApiDto exchangeCurrentApiDto) {
        if (exchangeCurrentApiDto == null) return null;
        ExchangeRate exchangeRate = new ExchangeRate();
        Currency baseCurrency = null;
        if (exchangeCurrentApiDto.getBaseCurrentCode() != null)
            baseCurrency = currencyService.findByCode(exchangeCurrentApiDto.getBaseCurrentCode());
        Currency currency = null;
        if (exchangeCurrentApiDto.getCode() != null)
            currency = currencyService.findByCode(exchangeCurrentApiDto.getCode());
        Source source = null;
        if (exchangeCurrentApiDto.getSource() != null)
            source = sourceService.findByName(exchangeCurrentApiDto.getSource());
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setCurrency(currency);
        exchangeRate.setRate(exchangeCurrentApiDto.getValue());
        exchangeRate.setSource(source);
        exchangeRate.setIsActual(exchangeCurrentApiDto.getIsActual());
        if (StringUtils.isNotBlank(exchangeCurrentApiDto.getDate()))
            exchangeRate.setDate(LocalDateTime.parse(exchangeCurrentApiDto.getDate().replace("Z", "")));
        return exchangeRate;
    }

    @Override
    public ExchangeCurrentApiDto toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) return null;
        ExchangeCurrentApiDto dto = new ExchangeCurrentApiDto();
        if (exchangeRate.getBaseCurrency() != null)
            dto.setBaseCurrentCode(exchangeRate.getBaseCurrency().getCode());
        if (exchangeRate.getCurrency() != null)
            dto.setCode(exchangeRate.getCurrency().getCode());
        dto.setValue(exchangeRate.getRate());
        if (exchangeRate.getDate() != null)
            dto.setDate(exchangeRate.getDate().toString());
        if (exchangeRate.getSource() != null)
            dto.setSource(exchangeRate.getSource().getName());
        dto.setIsActual(exchangeRate.getIsActual());
        return dto;
    }
}
