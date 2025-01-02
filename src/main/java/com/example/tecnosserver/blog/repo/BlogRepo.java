package com.example.tecnosserver.blog.repo;

import com.example.tecnosserver.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Long> {

    Optional<Blog> findByCode(String code);

}