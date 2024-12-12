package com.example.tecnosserver.events.web;

import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.service.EventCommandService;
import com.example.tecnosserver.events.service.EventQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateEvent(
            @RequestPart("event") EventDTO eventDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            eventCommandService.updateEvent(eventDTO, image);
            return ResponseEntity.ok("Event updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update event: " + e.getMessage());
        }
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
