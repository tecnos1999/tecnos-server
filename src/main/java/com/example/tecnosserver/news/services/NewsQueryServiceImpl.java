package com.example.tecnosserver.news.services;

import com.example.tecnosserver.news.dto.NewsDTO;
import com.example.tecnosserver.news.mapper.NewsMapper;
import com.example.tecnosserver.news.repo.NewsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsQueryServiceImpl implements NewsQueryService {

    private final NewsRepo newsRepository;
    private final NewsMapper newsMapper;

    @Override
    public List<NewsDTO> findAllNews() {
        return newsRepository.findAll().stream()
                .map(newsMapper::toDTO)
                .toList();
    }

    @Override
    public NewsDTO findNewsByCode(String uniqueCode) {
        return newsRepository.findByCode(uniqueCode)
                .map(newsMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("News not found with code: " + uniqueCode));
    }
}
