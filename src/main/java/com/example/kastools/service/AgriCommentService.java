package com.example.kastools.service;

import com.example.kastools.entity.AgriComment;
import com.example.kastools.entity.Result;

import java.util.List;

public interface AgriCommentService {
    List<AgriComment> getCommentsByProductId(Long productId, int start);
    int countCommentsByProductId(Long productId);
    Result addComment(AgriComment comment);
    Result deleteComment(Long id, String username);
    Double getAverageRating(Long productId);
}
