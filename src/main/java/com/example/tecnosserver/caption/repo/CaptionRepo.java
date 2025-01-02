package com.example.tecnosserver.caption.repo;


import com.example.tecnosserver.caption.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptionRepo extends JpaRepository<Caption, Long> {
}
