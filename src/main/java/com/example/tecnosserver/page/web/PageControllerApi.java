package com.example.tecnosserver.page.web;

import com.example.tecnosserver.page.dto.PageDTO;
import com.example.tecnosserver.page.service.PageCommandService;
import com.example.tecnosserver.page.service.PageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/pages")
@RequiredArgsConstructor
public class PageControllerApi {

    private final PageCommandService pageCommandService;
    private final PageQueryService pageQueryService;

    @PostMapping("/create")
    public ResponseEntity<String> createPage(@RequestBody PageDTO pageDTO) {
        try {
            pageCommandService.createPage(pageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Page created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{slug}")
    public ResponseEntity<String> updatePage(
            @PathVariable String slug,
            @RequestBody PageDTO pageDTO) {
        try {
            pageCommandService.updatePage(slug, pageDTO);
            return ResponseEntity.ok("Page updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<String> deletePage(@PathVariable String slug) {
        try {
            pageCommandService.deletePage(slug);
            return ResponseEntity.ok("Page deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PageDTO> getPageBySlug(@PathVariable String slug) {
        return pageQueryService.findPageBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
