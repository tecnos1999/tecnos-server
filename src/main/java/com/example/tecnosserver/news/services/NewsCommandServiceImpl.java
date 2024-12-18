package com.example.tecnosserver.news.services;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.news.dto.NewsDTO;
import com.example.tecnosserver.news.mapper.NewsMapper;
import com.example.tecnosserver.news.model.News;
import com.example.tecnosserver.news.repo.NewsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsCommandServiceImpl implements NewsCommandService {

    private final NewsRepo newsRepository;
    private final NewsMapper newsMapper;

    @Override
    public void addNews(NewsDTO newsDTO) {
        validateNewsInput(newsDTO);

        if (newsRepository.count() >= 3) {
            throw new AppException("You can only have up to 3 news articles.");
        }

        News news = newsMapper.fromDTO(newsDTO);
        newsRepository.save(news);
    }

    @Override
    public void updateNews(String uniqueCode, NewsDTO newsDTO) {
        validateNewsInput(newsDTO);

        News news = newsRepository.findByCode(uniqueCode)
                .orElseThrow(() -> new NotFoundException("News not found with code: " + uniqueCode));

        news.setTitle(newsDTO.getTitle());
        news.setShortDescription(newsDTO.getShortDescription());
        news.setLongDescription(newsDTO.getLongDescription());
        news.setTags(newsMapper.fromDTO(newsDTO).getTags());
        news.setIcon(newsDTO.getIcon());

        newsRepository.save(news);
    }

    @Override
    public void deleteNews(String uniqueCode) {
        News news = newsRepository.findByCode(uniqueCode)
                .orElseThrow(() -> new NotFoundException("News not found with code: " + uniqueCode));

        newsRepository.delete(news);
    }

    private void validateNewsInput(NewsDTO newsDTO) {
        if (newsDTO.getTitle() == null || newsDTO.getTitle().trim().isEmpty()) {
            throw new AppException("Title cannot be null or empty.");
        }
        if (newsDTO.getShortDescription() == null || newsDTO.getShortDescription().trim().isEmpty()) {
            throw new AppException("Short description cannot be null or empty.");
        }
        if (newsDTO.getLongDescription() == null || newsDTO.getLongDescription().trim().isEmpty()) {
            throw new AppException("Long description cannot be null or empty.");
        }
    }
}
