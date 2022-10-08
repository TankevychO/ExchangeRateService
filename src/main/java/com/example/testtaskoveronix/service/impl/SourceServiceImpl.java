package com.example.testtaskoveronix.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.repository.SourceRepository;
import com.example.testtaskoveronix.service.SourceService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class SourceServiceImpl implements SourceService {
    private final SourceRepository sourceRepository;

    @Override
    public Source findByName(String name) {
        Optional<Source> optionalSource = sourceRepository.findByName(name);
        if (optionalSource.isEmpty()) log.warn("Source with name: " + name + " not found");
        return optionalSource.isEmpty() ? null : optionalSource.get();
    }

    @Override
    public Source findById(Long id) {
        Optional<Source> optionalSource = sourceRepository.findById(id);
        if (optionalSource.isEmpty()) log.warn("Source with id: " + id + " not found");
        return optionalSource.isEmpty() ? null : optionalSource.get();
    }

    @Override
    public List<Source> findAll() {
        return sourceRepository.findAll();
    }

    @Override
    public void saveAll(Set<Source> sourceSet) {
        sourceRepository.saveAll(sourceSet);
    }

    @Override
    public void loadData() {
        clear();
        Set<Source> sourceSet = new HashSet<>();
        sourceSet.add(new Source("freecurrencyapi", "https://api.currencyapi.com/v3/currencies",
                "https://api.currencyapi.com/v3/latest", "https://api.currencyapi.com/v3/historical"));
        sourceSet.add(new Source("exchangeratesapi", "https://api.apilayer.com/exchangerates_data/symbols",
                "https://api.apilayer.com/exchangerates_data/latest&base=usd", "https://api.apilayer.com/exchangerates_data/"));
        sourceSet.add(new Source("exchangerate-api.com", "https://v6.exchangerate-api.com/v6/YOUR-API-KEY/codes",
                "https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/USD", "https://v6.exchangerate-api.com/v6/YOUR-API-KEY/history/USD/"));
        saveAll(sourceSet);
    }

    @Override
    public void clear() {
        sourceRepository.deleteAll();
    }
}
