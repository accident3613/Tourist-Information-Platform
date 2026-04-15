package com.example.kastools.service;

import com.example.kastools.entity.Comment;
import com.example.kastools.entity.Result;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsBySiteId(Long siteId, int start);
    int countCommentsBySiteId(Long siteId);
    Result addComment(Comment comment);
    Result deleteComment(Long id, String username);
    Double getAverageRating(Long siteId);
}
