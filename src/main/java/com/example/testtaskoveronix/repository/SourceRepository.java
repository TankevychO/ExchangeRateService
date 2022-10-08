package com.example.testtaskoveronix.repository;

import java.util.Optional;
import com.example.testtaskoveronix.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source, Long> {

    Optional<Source> findByName(String name);
}
