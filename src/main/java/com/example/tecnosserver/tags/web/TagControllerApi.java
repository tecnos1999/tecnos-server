package com.example.tecnosserver.tags.web;

import com.example.tecnosserver.tags.dto.TagDTO;
import com.example.tecnosserver.tags.services.TagCommandService;
import com.example.tecnosserver.tags.services.TagQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/tags")
@RequiredArgsConstructor
public class TagControllerApi {

    private final TagCommandService tagCommandService;
    private final TagQueryService tagQueryService;

    @PostMapping("/create")
    public ResponseEntity<String> addTag(@Valid @RequestBody TagDTO tagDTO) {
        tagCommandService.addTag(tagDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tag added successfully.");
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<String> updateTag(
            @PathVariable String name,
            @Valid @RequestBody TagDTO tagDTO) {
        tagCommandService.updateTag(name, tagDTO);
        return ResponseEntity.ok("Tag updated successfully.");
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteTag(@PathVariable String name) {
        tagCommandService.deleteTag(name);
        return ResponseEntity.ok("Tag deleted successfully.");
    }

    @GetMapping("/{name}")
    public ResponseEntity<TagDTO> getTagByName(@PathVariable String name) {
        return tagQueryService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        Optional<List<TagDTO>> tags = tagQueryService.findAllTags();
        return tags.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

