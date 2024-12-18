package com.example.tecnosserver.news.services;
import com.example.tecnosserver.news.dto.NewsDTO;
import java.util.List;

public interface NewsQueryService {
    List<NewsDTO> findAllNews();
    NewsDTO findNewsByCode(String uniqueCode);
}

