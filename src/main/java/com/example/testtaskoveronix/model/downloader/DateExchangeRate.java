package com.example.testtaskoveronix.model.downloader;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateExchangeRate {
    @JsonAlias("last_updated_at")
    private String dateUpdate;
}
