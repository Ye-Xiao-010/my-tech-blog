package com.xiaofei.my_tech_blog.service;

import com.xiaofei.my_tech_blog.entity.Article;
import com.xiaofei.my_tech_blog.entity.Comment;
import com.xiaofei.my_tech_blog.entity.User;
import com.xiaofei.my_tech_blog.repository.ArticleRepository;
import com.xiaofei.my_tech_blog.repository.CommentRepository;
import com.xiaofei.my_tech_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long articleId, Long userId, String content) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 基本验证
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("评论内容不能为空");
        }

        Comment comment = new Comment(content, article, user);
        return commentRepository.save(comment);
    }

    @Override
    public Comment createReply(Long articleId, Long userId, Long parentCommentId, String content) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("父评论不存在"));

        // 验证回复的是同一篇文章的评论
        if (!parentComment.getArticle().getId().equals(articleId)) {
            throw new RuntimeException("只能回复同一篇文章的评论");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("回复内容不能为空");
        }

        Comment reply = new Comment(content, article, user, parentComment);
        return commentRepository.save(reply);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查权限：只有评论作者可以删除
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除此评论");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getCommentsByArticle(Long articleId) {
        // 使用优化查询，避免N+1问题
        return commentRepository.findTopLevelCommentsWithUser(articleId);
    }

    @Override
    public List<Comment> getRepliesByComment(Long parentCommentId) {
        return commentRepository.findByParentCommentIdOrderByCreatedAtAsc(parentCommentId);
    }

    @Override
    public long getCommentCountByArticle(Long articleId) {
        return commentRepository.countByArticleId(articleId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
    }
}
