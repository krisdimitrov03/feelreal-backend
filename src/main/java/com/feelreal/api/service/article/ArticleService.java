package com.feelreal.api.service.article;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.enumeration.ArticleType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ArticleService {

    OperationResult<Article> getById(UUID id);

    OperationResult<List<Article>> getAll();

    OperationResult<List<Article>> getByType(ArticleType type);

}
