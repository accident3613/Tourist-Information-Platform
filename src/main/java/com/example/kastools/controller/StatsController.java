package com.example.kastools.controller;

import com.example.kastools.entity.Result;
import com.example.kastools.service.StatsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/site/rating")
    public Result getSiteRatingDistribution(HttpServletRequest request) {
        return statsService.getRatingDistribution();
    }

    @GetMapping("/site/hot")
    public Result getSiteHotRanking(HttpServletRequest request) {
        return statsService.getTopSitesByViewCount();
    }

    @GetMapping("/site/income")
    public Result getSiteIncomeRanking(HttpServletRequest request) {
        return statsService.getSiteIncomeRanking();
    }

    @GetMapping("/site/orders")
    public Result getSiteOrderCount(HttpServletRequest request) {
        return statsService.getSiteOrderCount();
    }

    @GetMapping("/site/all")
    public Result getAllSiteStats(HttpServletRequest request) {
        return statsService.getAllSiteStats();
    }

    @GetMapping("/order/trend")
    public Result getOrderTrend7Days(HttpServletRequest request) {
        return statsService.getOrderTrend7Days();
    }
}
