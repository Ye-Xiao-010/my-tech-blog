package com.xiaofei.my_tech_blog.service;

import com.xiaofei.my_tech_blog.entity.Article;
import com.xiaofei.my_tech_blog.entity.User;
import com.xiaofei.my_tech_blog.repository.ArticleRepository;
import com.xiaofei.my_tech_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Article createArticle(Article article, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        article.setAuthor(author);
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long articleId, Article articleDetails, Long authorId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        // 检查作者权限
        if (!article.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("无权修改此文章");
        }

        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setSummary(articleDetails.getSummary());

        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long articleId, Long authorId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        if (!article.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("无权删除此文章");
        }

        articleRepository.delete(article);
    }

    @Override
    public Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
    }

    @Override
    public List<Article> getArticlesByAuthor(Long authorId) {
        return articleRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }

    @Override
    public List<Article> getPublishedArticles() {
        return articleRepository.findByStatusOrderByCreatedAtDesc(Article.ArticleStatus.PUBLISHED);
    }

    @Override
    public Article publishArticle(Long articleId, Long authorId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        if (!article.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("无权发布此文章");
        }

        article.setStatus(Article.ArticleStatus.PUBLISHED);
        return articleRepository.save(article);
    }
}
