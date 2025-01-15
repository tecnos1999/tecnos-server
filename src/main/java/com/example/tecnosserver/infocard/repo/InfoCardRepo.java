package com.example.tecnosserver.infocard.repo;

import com.example.tecnosserver.infocard.model.InfoCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfoCardRepo extends JpaRepository<InfoCard, Long> {

    Optional<InfoCard> findByCode(String code);
    
}
