package com.example.tecnosserver.events.web;

import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.service.EventCommandService;
import com.example.tecnosserver.events.service.EventQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/events")
@RequiredArgsConstructor
public class EventControllerApi {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;

    @PostMapping("/create")
    public ResponseEntity<String> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        eventCommandService.addEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event added successfully.");
    }

    @DeleteMapping("/delete/{eventCode}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eventCode) {
        eventCommandService.deleteEvent(eventCode);
        return ResponseEntity.ok("Event deleted successfully.");
    }

    @GetMapping("/{eventCode}")
    public ResponseEntity<EventDTO> getEventByCode(@PathVariable String eventCode) {
        return eventQueryService.findEventByCode(eventCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<Optional<List<EventDTO>>> getAllEvents() {
        Optional<List<EventDTO>> events = eventQueryService.findAllEvents();
        return ResponseEntity.ok(events);
    }
}
