package com.example.testtaskoveronix.model.dto.response;


import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyResponse {
    private String name;
    private String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyResponse)) return false;
        CurrencyResponse that = (CurrencyResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
