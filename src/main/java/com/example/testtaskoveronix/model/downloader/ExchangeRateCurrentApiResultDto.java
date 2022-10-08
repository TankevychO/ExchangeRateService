package com.example.testtaskoveronix.model.downloader;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRateCurrentApiResultDto {

    private DateExchangeRate meta;

    private Map<String, ExchangeCurrentApiDto> data;
}
