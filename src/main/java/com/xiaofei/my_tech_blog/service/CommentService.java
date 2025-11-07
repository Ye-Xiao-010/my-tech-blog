package com.xiaofei.my_tech_blog.service;

import com.xiaofei.my_tech_blog.entity.Comment;
import java.util.List;

public interface CommentService {
    Comment createComment(Long articleId, Long userId, String content);
    Comment createReply(Long articleId, Long userId, Long parentCommentId, String content);
    void deleteComment(Long commentId, Long userId);
    List<Comment> getCommentsByArticle(Long articleId);
    List<Comment> getRepliesByComment(Long parentCommentId);
    long getCommentCountByArticle(Long articleId);
    Comment getCommentById(Long commentId);
}