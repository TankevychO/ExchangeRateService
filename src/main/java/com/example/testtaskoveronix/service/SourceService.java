package com.example.testtaskoveronix.service;

import java.util.List;
import java.util.Set;
import com.example.testtaskoveronix.model.Source;

public interface SourceService {

    Source findByName(String name);

    Source findById(Long id);

    List<Source> findAll();

    void saveAll(Set<Source> sourceSet);


    void loadData();

    void clear();
}
