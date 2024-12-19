package com.example.tecnosserver.news.services;


import com.example.tecnosserver.news.dto.NewsDTO;

public interface NewsCommandService {
    NewsDTO addNews(NewsDTO newsDTO);
    void updateNews(String uniqueCode, NewsDTO newsDTO);
    void deleteNews(String uniqueCode);
}

