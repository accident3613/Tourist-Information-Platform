package com.example.kastools.service.impl;

import com.example.kastools.entity.Comment;
import com.example.kastools.entity.Result;
import com.example.kastools.mapper.CommentMapper;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SiteMap siteMap;

    @Override
    public List<Comment> getCommentsBySiteId(Long siteId, int start) {
        return commentMapper.findBySiteId(siteId, start);
    }

    @Override
    public int countCommentsBySiteId(Long siteId) {
        return commentMapper.countBySiteId(siteId);
    }

    @Override
    public Result addComment(Comment comment) {
        Result result = new Result();
        try {
            // 插入评论
            int rows = commentMapper.insert(comment);
            if (rows > 0) {
                // 计算新的平均评分
                Double avgRating = commentMapper.getAverageRating(comment.getSite_id());
                if (avgRating != null) {
                    // 更新景点的rating字段
                    siteMap.updateRating(comment.getSite_id().intValue(), avgRating);
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
            // 先获取评论信息，以便后续更新景点评分
            Comment comment = commentMapper.findById(id);
            if (comment == null) {
                result.setCode(0);
                result.setData("评论不存在");
                return result;
            }

            int rows = commentMapper.delete(id);
            if (rows > 0) {
                // 重新计算该景点的平均评分
                Double avgRating = commentMapper.getAverageRating(comment.getSite_id());
                // 如果没有评论了，设置为0
                if (avgRating == null) {
                    avgRating = 0.0;
                }
                siteMap.updateRating(comment.getSite_id().intValue(), avgRating);

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
    public Double getAverageRating(Long siteId) {
        return commentMapper.getAverageRating(siteId);
    }
}
