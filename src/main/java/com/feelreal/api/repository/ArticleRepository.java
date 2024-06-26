package com.feelreal.api.repository;

import com.feelreal.api.model.Article;
import com.feelreal.api.model.enumeration.MoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    List<Article> findByType(MoodType type);

}
