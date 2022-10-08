package com.example.testtaskoveronix.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "source")
@NoArgsConstructor
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url_currency")
    private String urlCurrency;

    @Column(name = "url_exchange_rate")
    private String urlExchangeRate;

    @Column(name = "url_history")
    private String urlHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;
        Source source = (Source) o;
        return Objects.equals(name, source.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Source(String name, String urlCurrency, String urlExchangeRate, String urlHistory) {
        this.name = name;
        this.urlCurrency = urlCurrency;
        this.urlExchangeRate = urlExchangeRate;
        this.urlHistory = urlHistory;
    }
}
