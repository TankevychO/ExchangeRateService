package com.example.testtaskoveronix.model.downloader;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyResultsDto {
    private Map<String, CurrencyDtoCurrencyApi> data;
}
