package com.example.tecnosserver.news.repo;

import com.example.tecnosserver.news.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {
    Optional<News> findByCode(String uniqueCode);
}
