package com.example.tecnosserver.page.repo;

import com.example.tecnosserver.page.model.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepo extends JpaRepository<Page, Long> {

    @EntityGraph(attributePaths = {"sections"})
    Optional<Page> findBySlug(String slug);
    boolean existsBySlug(String slug);

    @EntityGraph(attributePaths = {"sections"})
    Optional<List<Page>> findBySlugIn(List<String> slugs);
}

