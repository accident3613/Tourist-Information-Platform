package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.AgriComment;
import com.example.kastools.entity.Result;
import com.example.kastools.service.AgriCommentService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agri/comment")
public class AgriCommentController {

    @Autowired
    private AgriCommentService agriCommentService;

    @Autowired
    private Jwt jwt;

    @GetMapping("/list")
    public Result getComments(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "start", defaultValue = "0") int start) {
        Result result = new Result();
        try {
            List<AgriComment> comments = agriCommentService.getCommentsByProductId(productId, start);
            result.setCode(1);
            result.setData(JSON.toJSONString(comments));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取评论失败");
        }
        return result;
    }

    @GetMapping("/count")
    public Result getCount(@RequestParam("productId") Long productId) {
        Result result = new Result();
        try {
            int count = agriCommentService.countCommentsByProductId(productId);
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
        Long productId = json.getLong("product_id");
        String content = json.getString("content");
        Integer rating = json.getInteger("rating");

        if (content == null || content.trim().isEmpty()) {
            Result result = new Result();
            result.setCode(0);
            result.setData("评论内容不能为空");
            return result;
        }

        AgriComment comment = new AgriComment();
        comment.setProduct_id(productId);
        comment.setUsername(username);
        comment.setContent(content.trim());
        comment.setRating(rating != null ? rating : 5);

        return agriCommentService.addComment(comment);
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

        return agriCommentService.deleteComment(id, username);
    }

    @GetMapping("/average")
    public Result getAverageRating(@RequestParam("productId") Long productId) {
        Result result = new Result();
        try {
            Double avg = agriCommentService.getAverageRating(productId);
            result.setCode(1);
            result.setData(String.valueOf(avg != null ? Math.round(avg * 10) / 10.0 : 0.0));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取评分失败");
        }
        return result;
    }
}