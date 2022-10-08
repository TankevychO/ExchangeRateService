package com.example.testtaskoveronix.model.downloader;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeCurrentApiDto {
    private String baseCurrentCode;
    private String code;
    private BigDecimal value;
    private String source;
    private Boolean isActual;
    private String date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeCurrentApiDto)) return false;
        ExchangeCurrentApiDto that = (ExchangeCurrentApiDto) o;
        return Objects.equals(baseCurrentCode, that.baseCurrentCode) && Objects.equals(code, that.code) && Objects.equals(value, that.value) && Objects.equals(source, that.source) && Objects.equals(isActual, that.isActual) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrentCode, code, value, source, isActual, date);
    }
}
