package com.example.kastools.service;

import com.example.kastools.entity.Activity;
import com.example.kastools.entity.Result;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Result createActivity(Activity activity, List<Map<String, Object>> ticketPromoPrices);
    Result updateActivity(Activity activity, List<Map<String, Object>> ticketPromoPrices);
    Result deleteActivity(Long id);
    Activity getActivityById(Long id);
    List<Activity> getActiveActivities(int limit);
    List<Activity> getAllActivities();
    boolean isActivityActive(Long activityId);
    Activity getActiveActivityBySiteId(Long siteId);
    Result checkSiteHasActiveActivity(Long siteId, Long excludeActivityId);
}
