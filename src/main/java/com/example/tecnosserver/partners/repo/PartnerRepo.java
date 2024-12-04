package com.example.tecnosserver.partners.repo;

import com.example.tecnosserver.partners.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepo extends JpaRepository<Partner, Long>{

    Optional<Partner> findByName(String name);
}
