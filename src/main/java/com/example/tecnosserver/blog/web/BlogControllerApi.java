package com.example.tecnosserver.blog.web;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.service.BlogCommandService;
import com.example.tecnosserver.blog.service.BlogQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/blogs")
@RequiredArgsConstructor
public class BlogControllerApi {

    private final BlogCommandService blogCommandService;
    private final BlogQueryService blogQueryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addBlog(
            @RequestPart("blog") @Valid BlogDTO blogDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "broschure", required = false) MultipartFile broschure) {
        blogCommandService.addBlog(blogDTO, image, broschure);
        return ResponseEntity.status(HttpStatus.CREATED).body("Blog added successfully.");
    }

    @PutMapping(value = "/update/{blogCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBlog(
            @PathVariable String blogCode,
            @RequestPart("blog") BlogDTO blogDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "broschure", required = false) MultipartFile broschure) {
        try {
            blogCommandService.updateBlog(blogCode, blogDTO, image, broschure);
            return ResponseEntity.ok("Blog updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update blog: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{blogCode}")
    public ResponseEntity<String> deleteBlog(@PathVariable String blogCode) {
        blogCommandService.deleteBlog(blogCode);
        return ResponseEntity.ok("Blog deleted successfully.");
    }

    @GetMapping("/{blogCode}")
    public ResponseEntity<BlogDTO> getBlogByCode(@PathVariable String blogCode) {
        try {
            BlogDTO blogDTO = blogQueryService.getBlogByCode(blogCode);
            return ResponseEntity.ok(blogDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {
        List<BlogDTO> blogs = blogQueryService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/getBlogsByCodeIn")
    public ResponseEntity<List<BlogDTO>> getBlogsByCodeIn(@RequestParam List<String> codes) {
        List<BlogDTO> blogs = blogQueryService.getBlogsByCodeIn(codes);
        return ResponseEntity.ok(blogs);
    }
}
