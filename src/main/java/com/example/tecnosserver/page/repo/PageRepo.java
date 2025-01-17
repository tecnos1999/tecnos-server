package com.example.tecnosserver.page.repo;

import com.example.tecnosserver.page.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepo extends JpaRepository<Page, Long> {
    Optional<Page> findBySlug(String slug);
}
