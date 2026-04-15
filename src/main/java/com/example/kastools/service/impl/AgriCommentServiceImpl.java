package com.example.kastools.service.impl;

import com.example.kastools.entity.AgriComment;
import com.example.kastools.entity.Result;
import com.example.kastools.mapper.AgriCommentMapper;
import com.example.kastools.mapper.AgriProductMapper;
import com.example.kastools.service.AgriCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgriCommentServiceImpl implements AgriCommentService {

    @Autowired
    private AgriCommentMapper agriCommentMapper;

    @Autowired
    private AgriProductMapper agriProductMapper;

    @Override
    public List<AgriComment> getCommentsByProductId(Long productId, int start) {
        return agriCommentMapper.findByProductId(productId, start);
    }

    @Override
    public int countCommentsByProductId(Long productId) {
        return agriCommentMapper.countByProductId(productId);
    }

    @Override
    public Result addComment(AgriComment comment) {
        Result result = new Result();
        try {
            int rows = agriCommentMapper.insert(comment);
            if (rows > 0) {
                // 更新产品评分
                Double avgRating = agriCommentMapper.getAverageRating(comment.getProduct_id());
                if (avgRating != null) {
                    agriProductMapper.updateRating(comment.getProduct_id(), avgRating);
                }
                result.setCode(1);
                result.setData("评论成功");
            } else {
                result.setCode(0);
                result.setData("评论失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("评论失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Result deleteComment(Long id, String username) {
        Result result = new Result();
        try {
            AgriComment comment = agriCommentMapper.findById(id);
            if (comment == null) {
                result.setCode(0);
                result.setData("评论不存在");
                return result;
            }

            int rows = agriCommentMapper.delete(id);
            if (rows > 0) {
                // 更新产品评分
                Double avgRating = agriCommentMapper.getAverageRating(comment.getProduct_id());
                if (avgRating == null) {
                    avgRating = 0.0;
                }
                agriProductMapper.updateRating(comment.getProduct_id(), avgRating);

                result.setCode(1);
                result.setData("删除成功");
            } else {
                result.setCode(0);
                result.setData("删除失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("删除失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Double getAverageRating(Long productId) {
        return agriCommentMapper.getAverageRating(productId);
    }
}
