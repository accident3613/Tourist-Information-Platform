package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Activity;
import com.example.kastools.entity.Result;
import com.example.kastools.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/list")
    public Result getActiveActivities(@RequestParam(defaultValue = "3") int limit) {
        Result result = new Result();
        try {
            List<Activity> activities = activityService.getActiveActivities(limit);
            result.setCode(1);
            result.setData(JSON.toJSONString(activities));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取活动失败");
        }
        return result;
    }

    @GetMapping("/all")
    public Result getAllActivities() {
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
    public Result getActivityById(@PathVariable Long id) {
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

    @GetMapping("/site/{siteId}")
    public Result getActiveActivityBySiteId(@PathVariable Long siteId) {
        Result result = new Result();
        try {
            Activity activity = activityService.getActiveActivityBySiteId(siteId);
            result.setCode(1);
            result.setData(JSON.toJSONString(activity));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取活动失败");
        }
        return result;
    }
}
