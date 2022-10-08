package com.example.testtaskoveronix.model.downloader;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeRateResultDtoExchangeRateApi {
    @JsonAlias({"base", "base_code"})
    private String base;
    @JsonAlias({"date", "time_last_update_utc"})
    private String date;
    @JsonAlias({"conversion_rates", "rates"})
    private Map<String, BigDecimal> rates;
    @JsonAlias({"time_last_update_unix", "timestamp"})
    private Integer timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRateResultDtoExchangeRateApi)) return false;
        ExchangeRateResultDtoExchangeRateApi that = (ExchangeRateResultDtoExchangeRateApi) o;
        return Objects.equals(base, that.base) && Objects.equals(date, that.date) && Objects.equals(rates, that.rates) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, date, rates, timestamp);
    }
}
