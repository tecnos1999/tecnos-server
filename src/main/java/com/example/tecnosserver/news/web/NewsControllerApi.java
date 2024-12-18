package com.example.tecnosserver.news.web;


import com.example.tecnosserver.news.dto.NewsDTO;
import com.example.tecnosserver.news.services.NewsCommandService;
import com.example.tecnosserver.news.services.NewsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("server/api/v1/news")
@RequiredArgsConstructor
public class NewsControllerApi {

    private final NewsCommandService newsCommandService;
    private final NewsQueryService newsQueryService;

    @PostMapping
    public ResponseEntity<String> addNews(@RequestBody NewsDTO newsDTO) {
        newsCommandService.addNews(newsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("News added successfully.");
    }

    @PutMapping("/{uniqueCode}")
    public ResponseEntity<String> updateNews(
            @PathVariable String uniqueCode,
            @RequestBody NewsDTO newsDTO) {
        newsCommandService.updateNews(uniqueCode, newsDTO);
        return ResponseEntity.ok("News updated successfully.");
    }

    @DeleteMapping("/{uniqueCode}")
    public ResponseEntity<String> deleteNews(@PathVariable String uniqueCode) {
        newsCommandService.deleteNews(uniqueCode);
        return ResponseEntity.ok("News deleted successfully.");
    }

    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(newsQueryService.findAllNews());
    }

    @GetMapping("/{uniqueCode}")
    public ResponseEntity<NewsDTO> getNewsByCode(@PathVariable String uniqueCode) {
        return ResponseEntity.ok(newsQueryService.findNewsByCode(uniqueCode));
    }
}

