package com.example.testtaskoveronix.model.dto.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRateResponse {
    @JsonAlias("data")
    private Set<ExchangeCurrentApiDto> exchangeCurrentApiDtos = new HashSet<>();

    private List<String> errors = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRateResponse)) return false;
        ExchangeRateResponse response = (ExchangeRateResponse) o;
        return Objects.equals(exchangeCurrentApiDtos, response.exchangeCurrentApiDtos) && Objects.equals(errors, response.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchangeCurrentApiDtos, errors);
    }
}
