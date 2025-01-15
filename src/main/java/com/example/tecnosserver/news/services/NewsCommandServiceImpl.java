package com.example.tecnosserver.news.services;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.news.dto.NewsDTO;
import com.example.tecnosserver.news.mapper.NewsMapper;
import com.example.tecnosserver.news.model.News;
import com.example.tecnosserver.news.repo.NewsRepo;
import com.example.tecnosserver.tags.dto.TagDTO;
import com.example.tecnosserver.tags.model.Tag;
import com.example.tecnosserver.tags.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsCommandServiceImpl implements NewsCommandService {

    private final NewsRepo newsRepository;
    private final NewsMapper newsMapper;
    private final TagRepo tagRepo;

    @Override
    public NewsDTO addNews(NewsDTO newsDTO) {
        validateNewsInput(newsDTO);

        if (newsRepository.count() >= 3) {
            throw new AppException("You can only have up to 3 news articles.");
        }

        List<Tag> tags = validateTagsExistence(newsDTO.getTags());
        News news = newsMapper.fromDTO(newsDTO);
        news.setTags(tags);

        News savedNews = newsRepository.save(news);

        return newsMapper.toDTO(savedNews);
    }


    @Override
    public void updateNews(String uniqueCode, NewsDTO newsDTO) {
        validateNewsInput(newsDTO);

        News news = newsRepository.findByCode(uniqueCode)
                .orElseThrow(() -> new NotFoundException("News not found with code: " + uniqueCode));

        List<Tag> tags = validateTagsExistence(newsDTO.getTags());
        news.setTitle(newsDTO.getTitle());
        news.setShortDescription(newsDTO.getShortDescription());
        news.setLongDescription(newsDTO.getLongDescription());
        news.setTags(tags);
        news.setIcon(newsDTO.getIcon());
        news.setLink(newsDTO.getLink());

        newsRepository.save(news);
    }

    @Override
    public void deleteNews(String uniqueCode) {
        News news = newsRepository.findByCode(uniqueCode)
                .orElseThrow(() -> new NotFoundException("News not found with code: " + uniqueCode));

        newsRepository.delete(news);
    }

    private List<Tag> validateTagsExistence(List<TagDTO> tagDTOs) {
        List<Tag> tags = new ArrayList<>();
        for (TagDTO tagDTO : tagDTOs) {
            Tag tag = tagRepo.findByName(tagDTO.name())
                    .orElseThrow(() -> new AppException("Tag with name '" + tagDTO.name() + "' does not exist."));
            tags.add(tag);
        }
        return tags;
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
