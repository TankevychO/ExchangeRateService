package com.example.testtaskoveronix.model.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.example.testtaskoveronix.model.downloader.ExchangeCurrentApiDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestRateResponse {
    private ExchangeCurrentApiDto buying;

    private ExchangeCurrentApiDto selling;

    private List<String> errors = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BestRateResponse)) return false;
        BestRateResponse response = (BestRateResponse) o;
        return Objects.equals(buying, response.buying) && Objects.equals(selling, response.selling) && Objects.equals(errors, response.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buying, selling, errors);
    }
}
