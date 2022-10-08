package com.example.testtaskoveronix.service.converter;

import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.downloader.CurrencyDtoCurrencyApi;
import com.example.testtaskoveronix.model.dto.response.CurrencyResponse;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverter implements Converter<Currency, CurrencyDtoCurrencyApi, CurrencyResponse> {
    @Override
    public CurrencyResponse toDto(Currency entity) {
        if (entity == null) return null;
        CurrencyResponse dto = new CurrencyResponse();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return dto;
    }

    @Override
    public Currency toEntity(CurrencyDtoCurrencyApi dto) {
        if (dto == null) return null;
        Currency entity = new Currency();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        return entity;
    }
}
