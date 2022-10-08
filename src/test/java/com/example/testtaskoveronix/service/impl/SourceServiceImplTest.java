package com.example.testtaskoveronix.service.impl;

import java.util.Optional;
import com.example.testtaskoveronix.model.Source;
import com.example.testtaskoveronix.repository.SourceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SourceServiceImplTest {
    @InjectMocks
    private SourceServiceImpl sourceService;
    private final Source source;

    {
        source = new Source("freecurrencyapi", "https://api.currencyapi.com/v3/currencies",
                "https://api.currencyapi.com/v3/latest", "https://api.currencyapi.com/v3/historical");
    }

    @Mock
    private SourceRepository sourceRepository;

    @Test
    void shouldValidFindByNameSource() {
        Optional<Source> optionalSource = Optional.of(source);
        Mockito.when(sourceRepository.findByName("freecurrencyapi")).thenReturn(optionalSource);
        Source actual = sourceService.findByName("freecurrencyapi");
        Assertions.assertEquals(source, actual);
    }

    @Test
    void shouldNullFindByNameSource() {
        Mockito.when(sourceRepository.findByName("freecurrency")).thenReturn(Optional.empty());
        Source actual = sourceService.findByName("freecurrency");
        Assertions.assertNull(actual);
    }

    @Test
    void shouldValidFindByIdSource() {
        Optional<Source> optionalSource = Optional.of(source);
        Mockito.when(sourceRepository.findById(1L)).thenReturn(optionalSource);
        Source actual = sourceService.findById(1L);
        Assertions.assertEquals(source, actual);
    }

    @Test
    void shouldNullFindByIdSource() {
        Mockito.when(sourceRepository.findById(1L)).thenReturn(Optional.empty());
        Source actual = sourceService.findById(1L);
        Assertions.assertNull(actual);
    }
}