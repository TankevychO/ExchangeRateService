package com.example.testtaskoveronix.repository;

import java.util.Optional;
import com.example.testtaskoveronix.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCode(String code);
}
