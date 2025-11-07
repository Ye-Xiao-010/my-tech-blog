package com.xiaofei.my_tech_blog.repository;

import com.xiaofei.my_tech_blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 根据文章查找评论（顶级评论，非回复）
    List<Comment> findByArticleIdAndParentCommentIsNullOrderByCreatedAtDesc(Long articleId);

    // 查找某个评论的所有回复
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    // 查找用户在某个文章的评论
    List<Comment> findByArticleIdAndUserIdOrderByCreatedAtDesc(Long articleId, Long userId);

    // 统计文章的评论数量
    long countByArticleId(Long articleId);

    // 使用JPQL查询获取文章评论及其回复（优化查询）
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.article.id = :articleId AND c.parentComment IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findTopLevelCommentsWithUser(@Param("articleId") Long articleId);
}
