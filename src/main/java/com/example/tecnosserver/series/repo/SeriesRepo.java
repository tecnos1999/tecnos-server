package com.example.tecnosserver.series.repo;

import com.example.tecnosserver.series.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeriesRepo extends JpaRepository<Series, Long> {

    Optional<Series> findByCode(String code);

    Optional<Series> findByName(String name);

}

