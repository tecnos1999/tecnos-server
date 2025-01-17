package com.example.tecnosserver.sections.repo;

import com.example.tecnosserver.sections.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepo extends JpaRepository<Section,Long> {
    Optional<List<Section>> findByPageId(Long pageId);
}
