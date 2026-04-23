package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.Activity;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.service.ActivityService;
import com.example.kastools.service.AdminPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/activity")
public class AdminActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private AdminPermissionService permissionService;

    @GetMapping("/list")
    public Result getAllActivities(HttpServletRequest request) {
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        Result result = new Result();
        try {
            List<Activity> activities = activityService.getAllActivities();
            result.setCode(1);
            result.setData(JSON.toJSONString(activities));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取活动失败");
        }
        return result;
    }

    @GetMapping("/{id}")
    public Result getActivityById(@PathVariable Long id, HttpServletRequest request) {
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        Result result = new Result();
        try {
            Activity activity = activityService.getActivityById(id);
            if (activity != null) {
                result.setCode(1);
                result.setData(JSON.toJSONString(activity));
            } else {
                result.setCode(0);
                result.setData("活动不存在");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取活动失败");
        }
        return result;
    }

    @PostMapping("/create")
    public Result createActivity(@RequestBody String body, HttpServletRequest request) {
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        if (permissionService.isOperator(admin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("运营人员没有权限创建活动");
            return result;
        }

        JSONObject json = JSON.parseObject(body);
        Activity activity = new Activity();
        activity.setTitle(json.getString("title"));
        activity.setDescription(json.getString("description"));
        activity.setImageUrl(json.getString("imageUrl"));
        activity.setSiteId(json.getLong("siteId"));
        String startTimeStr = json.getString("startTime");
        String endTimeStr = json.getString("endTime");
        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            activity.setStartTime(LocalDateTime.parse(startTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            activity.setEndTime(LocalDateTime.parse(endTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        activity.setCreatedBy(admin.getId());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ticketPromoPrices = (List<Map<String, Object>>) (List<?>) json.getList("ticketPromoPrices", Map.class);

        return activityService.createActivity(activity, ticketPromoPrices);
    }

    @PutMapping("/update")
    public Result updateActivity(@RequestBody String body, HttpServletRequest request) {
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        if (permissionService.isOperator(admin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("运营人员没有权限编辑活动");
            return result;
        }

        JSONObject json = JSON.parseObject(body);
        Activity activity = new Activity();
        activity.setId(json.getLong("id"));
        activity.setTitle(json.getString("title"));
        activity.setDescription(json.getString("description"));
        activity.setImageUrl(json.getString("imageUrl"));
        activity.setSiteId(json.getLong("siteId"));
        String startTimeStr2 = json.getString("startTime");
        String endTimeStr2 = json.getString("endTime");
        if (startTimeStr2 != null && !startTimeStr2.isEmpty()) {
            activity.setStartTime(LocalDateTime.parse(startTimeStr2, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (endTimeStr2 != null && !endTimeStr2.isEmpty()) {
            activity.setEndTime(LocalDateTime.parse(endTimeStr2, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ticketPromoPrices = (List<Map<String, Object>>) (List<?>) json.getList("ticketPromoPrices", Map.class);

        return activityService.updateActivity(activity, ticketPromoPrices);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteActivity(@PathVariable Long id, HttpServletRequest request) {
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        if (permissionService.isOperator(admin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("运营人员没有权限删除活动");
            return result;
        }

        return activityService.deleteActivity(id);
    }

    @GetMapping("/check-site/{siteId}")
    public Result checkSiteHasActiveActivity(@PathVariable Long siteId,
                                              @RequestParam(required = false) Long excludeId,
                                              HttpServletRequest request) {
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        return activityService.checkSiteHasActiveActivity(siteId, excludeId);
    }
}
