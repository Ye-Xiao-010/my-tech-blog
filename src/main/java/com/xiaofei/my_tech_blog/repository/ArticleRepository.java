package com.xiaofei.my_tech_blog.repository;

import com.xiaofei.my_tech_blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 根据作者查找文章
    List<Article> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    // 查找已发布的文章
    List<Article> findByStatusOrderByCreatedAtDesc(Article.ArticleStatus status);

    // 根据标题搜索文章
    List<Article> findByTitleContainingIgnoreCase(String keyword);
}
