package com.example.tecnosserver.page.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.page.mapper.PageMapper;
import com.example.tecnosserver.page.repo.PageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageQueryServiceImpl implements PageQueryService {

    private final PageRepo pageRepo;
    private final PageMapper pageMapper;



}
