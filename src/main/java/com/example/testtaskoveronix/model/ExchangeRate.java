package com.example.testtaskoveronix.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "currency_exchange")
public class ExchangeRate implements Comparable<ExchangeRate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_currency")
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @Column(name = "exchange_rate")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "source")
    private Source source;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "is_actual")
    private Boolean isActual;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRate)) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(currency, that.currency) && Objects.equals(rate, that.rate) && Objects.equals(source, that.source) && Objects.equals(date, that.date) && Objects.equals(isActual, that.isActual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, currency, rate, source, date, isActual);
    }

    @Override
    public int compareTo(ExchangeRate o) {
        return o.getRate().compareTo(this.getRate());
    }
}
