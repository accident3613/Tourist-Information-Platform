package com.example.kastools.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Activity;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Ticket;
import com.example.kastools.mapper.ActivityMapper;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.mapper.TicketMapper;
import com.example.kastools.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private SiteMap siteMap;

    @Override
    @Transactional
    public Result createActivity(Activity activity, List<Map<String, Object>> ticketPromoPrices) {
        Result result = new Result();
        
        try {
            if (activity.getSiteId() == null) {
                result.setCode(0);
                result.setData("请选择关联景点");
                return result;
            }
            
            if (activity.getStartTime() == null || activity.getEndTime() == null) {
                result.setCode(0);
                result.setData("请设置活动时间");
                return result;
            }
            
            if (!activity.getStartTime().isBefore(activity.getEndTime())) {
                result.setCode(0);
                result.setData("结束时间必须大于开始时间");
                return result;
            }
            
            int activeCount = activityMapper.countActiveBySiteId(activity.getSiteId(), LocalDateTime.now());
            if (activeCount > 0) {
                result.setCode(0);
                result.setData("该景点已有进行中的活动");
                return result;
            }
            
            int rows = activityMapper.insert(activity);
            if (rows > 0) {
                if (ticketPromoPrices != null && !ticketPromoPrices.isEmpty()) {
                    for (Map<String, Object> promo : ticketPromoPrices) {
                        Long ticketId = Long.parseLong(promo.get("ticketId").toString());
                        BigDecimal promoPrice = new BigDecimal(promo.get("promoPrice").toString());
                        ticketMapper.updatePromoPrice(ticketId, promoPrice);
                    }
                }
                result.setCode(1);
                result.setData("创建成功");
            } else {
                result.setCode(0);
                result.setData("创建失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("创建失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Result updateActivity(Activity activity, List<Map<String, Object>> ticketPromoPrices) {
        Result result = new Result();
        
        try {
            Activity exist = activityMapper.findById(activity.getId());
            if (exist == null) {
                result.setCode(0);
                result.setData("活动不存在");
                return result;
            }
            
            if (activity.getStartTime() == null || activity.getEndTime() == null) {
                result.setCode(0);
                result.setData("请设置活动时间");
                return result;
            }
            
            if (!activity.getStartTime().isBefore(activity.getEndTime())) {
                result.setCode(0);
                result.setData("结束时间必须大于开始时间");
                return result;
            }
            
            if (!exist.getSiteId().equals(activity.getSiteId())) {
                int activeCount = activityMapper.countActiveBySiteIdExclude(activity.getSiteId(), activity.getId(), LocalDateTime.now());
                if (activeCount > 0) {
                    result.setCode(0);
                    result.setData("目标景点已有进行中的活动");
                    return result;
                }
                
                List<Ticket> oldTickets = ticketMapper.findBySiteIdAll(exist.getSiteId());
                for (Ticket ticket : oldTickets) {
                    ticketMapper.clearPromoPrice(ticket.getId());
                }
            }
            
            int rows = activityMapper.update(activity);
            if (rows > 0) {
                List<Ticket> tickets = ticketMapper.findBySiteIdAll(activity.getSiteId());
                for (Ticket ticket : tickets) {
                    ticketMapper.clearPromoPrice(ticket.getId());
                }
                
                if (ticketPromoPrices != null && !ticketPromoPrices.isEmpty()) {
                    for (Map<String, Object> promo : ticketPromoPrices) {
                        Long ticketId = Long.parseLong(promo.get("ticketId").toString());
                        BigDecimal promoPrice = new BigDecimal(promo.get("promoPrice").toString());
                        ticketMapper.updatePromoPrice(ticketId, promoPrice);
                    }
                }
                
                result.setCode(1);
                result.setData("更新成功");
            } else {
                result.setCode(0);
                result.setData("更新失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("更新失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Result deleteActivity(Long id) {
        Result result = new Result();
        
        try {
            Activity activity = activityMapper.findById(id);
            if (activity == null) {
                result.setCode(0);
                result.setData("活动不存在");
                return result;
            }
            
            List<Ticket> tickets = ticketMapper.findBySiteIdAll(activity.getSiteId());
            for (Ticket ticket : tickets) {
                ticketMapper.clearPromoPrice(ticket.getId());
            }
            
            int rows = activityMapper.delete(id);
            if (rows > 0) {
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
    public Activity getActivityById(Long id) {
        return activityMapper.findById(id);
    }

    @Override
    public List<Activity> getActiveActivities(int limit) {
        return activityMapper.findActiveActivities(LocalDateTime.now(), limit);
    }

    @Override
    public List<Activity> getAllActivities() {
        return activityMapper.findAll();
    }

    @Override
    public boolean isActivityActive(Long activityId) {
        Activity activity = activityMapper.findById(activityId);
        if (activity == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(activity.getStartTime()) && !now.isAfter(activity.getEndTime());
    }

    @Override
    public Activity getActiveActivityBySiteId(Long siteId) {
        return activityMapper.findActiveBySiteId(siteId, LocalDateTime.now());
    }

    @Override
    public Result checkSiteHasActiveActivity(Long siteId, Long excludeActivityId) {
        Result result = new Result();
        int count;
        if (excludeActivityId != null) {
            count = activityMapper.countActiveBySiteIdExclude(siteId, excludeActivityId, LocalDateTime.now());
        } else {
            count = activityMapper.countActiveBySiteId(siteId, LocalDateTime.now());
        }
        result.setCode(1);
        result.setData(JSON.toJSONString(count > 0));
        return result;
    }
}
