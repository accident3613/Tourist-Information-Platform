package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.Comment;
import com.example.kastools.entity.Result;
import com.example.kastools.service.CommentService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private Jwt jwt;

    @GetMapping("/list")
    public Result getComments(
            @RequestParam("siteId") Long siteId,
            @RequestParam(value = "start", defaultValue = "0") int start) {
        Result result = new Result();
        try {
            List<Comment> comments = commentService.getCommentsBySiteId(siteId, start);
            result.setCode(1);
            result.setData(JSON.toJSONString(comments));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取评论失败");
        }
        return result;
    }

    @GetMapping("/count")
    public Result getCount(@RequestParam("siteId") Long siteId) {
        Result result = new Result();
        try {
            int count = commentService.countCommentsBySiteId(siteId);
            result.setCode(1);
            result.setData(String.valueOf(count));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取数量失败");
        }
        return result;
    }

    @PostMapping("/add")
    public Result addComment(@RequestBody String body, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        JSONObject json = JSON.parseObject(body);
        Long siteId = json.getLong("site_id");
        String content = json.getString("content");
        Integer rating = json.getInteger("rating");

        if (content == null || content.trim().isEmpty()) {
            Result result = new Result();
            result.setCode(0);
            result.setData("评论内容不能为空");
            return result;
        }

        Comment comment = new Comment();
        comment.setSite_id(siteId);
        comment.setUsername(username);
        comment.setContent(content.trim());
        comment.setRating(rating != null ? rating : 5);

        return commentService.addComment(comment);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteComment(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        return commentService.deleteComment(id, username);
    }

    @GetMapping("/average")
    public Result getAverageRating(@RequestParam("siteId") Long siteId) {
        Result result = new Result();
        try {
            Double avg = commentService.getAverageRating(siteId);
            result.setCode(1);
            result.setData(String.valueOf(avg != null ? Math.round(avg * 10) / 10.0 : 0.0));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取评分失败");
        }
        return result;
    }
}
