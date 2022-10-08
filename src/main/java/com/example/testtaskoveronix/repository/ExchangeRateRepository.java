package com.example.testtaskoveronix.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import com.example.testtaskoveronix.model.Currency;
import com.example.testtaskoveronix.model.ExchangeRate;
import com.example.testtaskoveronix.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> getCurrencyExchangesByIsActualAndSource(Boolean isActual, Source source);

    List<ExchangeRate> getCurrencyExchangesByDateAndSource(LocalDateTime dateTime, Source source);

    List<ExchangeRate> getExchangeRateByIsActualAndCurrency(Boolean isActual, Currency currency);

    Set<ExchangeRate> findAllByIsActual(Boolean isActual);
}
