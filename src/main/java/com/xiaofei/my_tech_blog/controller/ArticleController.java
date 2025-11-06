package com.xiaofei.my_tech_blog.controller;

import com.xiaofei.my_tech_blog.entity.Article;
import com.xiaofei.my_tech_blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 创建文章
    @PostMapping
    public ResponseEntity<Map<String, Object>> createArticle(
            @RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            String title = (String) request.get("title");
            String content = (String) request.get("content");
            String summary = (String) request.get("summary");
            Long authorId = Long.valueOf(request.get("authorId").toString());

            // 基本验证
            if (title == null || title.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "文章标题不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            // 创建文章对象
            Article article = new Article();
            article.setTitle(title);
            article.setContent(content);
            article.setSummary(summary);

            Article savedArticle = articleService.createArticle(article, authorId);

            response.put("success", true);
            response.put("message", "文章创建成功");
            response.put("article", Map.of(
                    "id", savedArticle.getId(),
                    "title", savedArticle.getTitle(),
                    "summary", savedArticle.getSummary(),
                    "status", savedArticle.getStatus(),
                    "createdAt", savedArticle.getCreatedAt()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 更新文章
    @PutMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable Long articleId,
            @RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            String title = (String) request.get("title");
            String content = (String) request.get("content");
            String summary = (String) request.get("summary");
            Long authorId = Long.valueOf(request.get("authorId").toString());

            Article articleDetails = new Article();
            articleDetails.setTitle(title);
            articleDetails.setContent(content);
            articleDetails.setSummary(summary);

            Article updatedArticle = articleService.updateArticle(articleId, articleDetails, authorId);

            response.put("success", true);
            response.put("message", "文章更新成功");
            response.put("article", Map.of(
                    "id", updatedArticle.getId(),
                    "title", updatedArticle.getTitle(),
                    "summary", updatedArticle.getSummary(),
                    "status", updatedArticle.getStatus(),
                    "updatedAt", updatedArticle.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 删除文章
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> deleteArticle(
            @PathVariable Long articleId,
            @RequestParam Long authorId) {

        Map<String, Object> response = new HashMap<>();

        try {
            articleService.deleteArticle(articleId, authorId);

            response.put("success", true);
            response.put("message", "文章删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 获取文章详情
    @GetMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> getArticleById(@PathVariable Long articleId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Article article = articleService.getArticleById(articleId);

            response.put("success", true);
            response.put("article", Map.of(
                    "id", article.getId(),
                    "title", article.getTitle(),
                    "content", article.getContent(),
                    "summary", article.getSummary(),
                    "status", article.getStatus(),
                    "author", Map.of(
                            "id", article.getAuthor().getId(),
                            "username", article.getAuthor().getUsername()
                    ),
                    "createdAt", article.getCreatedAt(),
                    "updatedAt", article.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 获取用户文章列表
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Map<String, Object>> getArticlesByAuthor(@PathVariable Long authorId) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Article> articles = articleService.getArticlesByAuthor(authorId);

            // 使用显式类型声明
            List<Map<String, Object>> articleList = new ArrayList<>();
            for (Article article : articles) {
                Map<String, Object> articleMap = new HashMap<>();
                articleMap.put("id", article.getId());
                articleMap.put("title", article.getTitle());
                articleMap.put("summary", article.getSummary());
                articleMap.put("status", article.getStatus().toString());
                articleMap.put("createdAt", article.getCreatedAt());
                articleList.add(articleMap);
            }

            response.put("success", true);
            response.put("articles", articleList);
            response.put("count", articleList.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 获取已发布文章列表
    @GetMapping("/published")
    public ResponseEntity<Map<String, Object>> getPublishedArticles() {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Article> articles = articleService.getPublishedArticles();

            List<Map<String, Object>> articleList = articles.stream()
                    .map(article -> Map.of(
                            "id", article.getId(),
                            "title", article.getTitle(),
                            "summary", article.getSummary(),
                            "author", Map.of(
                                    "id", article.getAuthor().getId(),
                                    "username", article.getAuthor().getUsername()
                            ),
                            "createdAt", article.getCreatedAt()
                    ))
                    .toList();

            response.put("success", true);
            response.put("articles", articleList);
            response.put("count", articleList.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 发布文章
    @PostMapping("/{articleId}/publish")
    public ResponseEntity<Map<String, Object>> publishArticle(
            @PathVariable Long articleId,
            @RequestParam Long authorId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Article publishedArticle = articleService.publishArticle(articleId, authorId);

            response.put("success", true);
            response.put("message", "文章发布成功");
            response.put("article", Map.of(
                    "id", publishedArticle.getId(),
                    "title", publishedArticle.getTitle(),
                    "status", publishedArticle.getStatus()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

