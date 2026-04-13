package com.example.kastools.controller;

import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;
import com.example.kastools.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/site")
public class Site {

    @Autowired
    private SiteService siteService;

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
}
