package com.example.tecnosserver.caption.repo;


import com.example.tecnosserver.caption.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptionRepo extends JpaRepository<Caption, Long> {

    Optional<Caption> findByCode(String code);
}
