package com.feelreal.api.service.article;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.enumeration.ArticleType;
import com.feelreal.api.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public OperationResult<Article> getById(UUID id) {
        return articleRepository.findById(id)
                .map(value -> new OperationResult<>(ResultStatus.SUCCESS, value))
                .orElseGet(() -> new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null));
    }

    @Override
    public OperationResult<List<Article>> getAll() {
        return new OperationResult<>(ResultStatus.SUCCESS, articleRepository.findAll());
    }

    @Override
    public OperationResult<List<Article>> getByType(ArticleType type) {
        return new OperationResult<>(ResultStatus.SUCCESS, articleRepository.findByType(type));
    }
}
