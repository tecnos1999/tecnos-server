package com.example.tecnosserver.motto.repo;

import org.springframework.stereotype.Repository;


import com.example.tecnosserver.motto.model.Motto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface MottoRepo extends JpaRepository<Motto, Long> {

    Optional<Motto> findByCode(String code);

}

