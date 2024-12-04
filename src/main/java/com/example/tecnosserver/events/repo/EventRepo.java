package com.example.tecnosserver.events.repo;

import com.example.tecnosserver.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    Optional<Event> findEventByEventCode(String eventCode);

}
