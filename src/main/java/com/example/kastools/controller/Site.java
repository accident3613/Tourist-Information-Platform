package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;
import com.example.kastools.service.AdminPermissionService;
import com.example.kastools.service.SiteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/site")
public class Site {

    @Autowired
    private SiteService siteService;

    @Autowired
    private AdminPermissionService permissionService;

    @GetMapping("/list")
    public List<Site_list> list(@RequestParam("start") int start) {
        return siteService.getSiteList(start);
    }

    @PostMapping("/list1")
    public Site_list list1(int site_id) {
        return siteService.getSiteById(site_id);
    }

    @PostMapping("/siteicon")
    public List<SiteIcon> site(int site_id) {
        return siteService.getSiteIcons(site_id);
    }

    @GetMapping("/search")
    public List<Site_list> search(@RequestParam("keyword") String keyword, @RequestParam("start") int start) {
        return siteService.searchSites(keyword, start);
    }

    @GetMapping("/search/count")
    public int searchCount(@RequestParam("keyword") String keyword) {
        return siteService.countSearchSites(keyword);
    }

    @PostMapping("/admin/save")
    public Result adminSave(@RequestBody Site_list site, HttpServletRequest request) {
        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, (long) site.getId());
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        Result result = new Result();
        try {
            Site_list saved = siteService.saveSite(site);
            result.setCode(1);
            result.setData(JSON.toJSONString(saved));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("保存失败: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/admin/delete/{id}")
    public Result adminDelete(@PathVariable int id, HttpServletRequest request) {
        // 检查权限 - 只有超级管理员和管理员可以删除景点
        Result checkResult = permissionService.checkAdminPermission(request);
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        Result result = new Result();
        try {
            boolean deleted = siteService.deleteSite(id);
            if (deleted) {
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

    @GetMapping("/filter")
    public List<Site_list> filterByRating(
            @RequestParam(value = "minRating", defaultValue = "0") double minRating,
            @RequestParam(value = "maxRating", defaultValue = "5") double maxRating,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return siteService.searchSitesByRating(keyword, minRating, maxRating, start);
        }
        return siteService.getSitesByRating(minRating, maxRating, start);
    }
}
