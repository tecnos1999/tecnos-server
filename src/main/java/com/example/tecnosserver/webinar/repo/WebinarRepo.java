package com.example.tecnosserver.webinar.repo;

import com.example.tecnosserver.webinar.model.Webinar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebinarRepo extends JpaRepository<Webinar, Long> {

    Optional<Webinar> findWebinarByWebCode(String webCode);
}
