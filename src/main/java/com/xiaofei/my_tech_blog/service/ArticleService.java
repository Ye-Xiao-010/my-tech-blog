package com.xiaofei.my_tech_blog.service;

import com.xiaofei.my_tech_blog.entity.Article;
import java.util.List;

public interface ArticleService {
    Article createArticle(Article article, Long authorId);
    Article updateArticle(Long articleId, Article articleDetails, Long authorId);
    void deleteArticle(Long articleId, Long authorId);
    Article getArticleById(Long articleId);
    List<Article> getArticlesByAuthor(Long authorId);
    List<Article> getPublishedArticles();
    Article publishArticle(Long articleId, Long authorId);
}
