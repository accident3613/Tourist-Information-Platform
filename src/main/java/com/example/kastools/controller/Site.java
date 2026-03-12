package com.example.kastools.controller;

import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;
import com.example.kastools.mapper.SiteMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/site")
public class Site {
@Autowired
    SiteMap siteMap;
@Cacheable(value = "site_list",key = "#start")
@GetMapping("/list")   //获取景点列表
    public List<Site_list> list(@RequestParam("start") int start){

List<Site_list> list=siteMap.list(start);
return list;
}
    @PostMapping ("/list1")   //获取指定景点列表信息
    public Site_list list1(int site_id){

        Site_list site=siteMap.site(site_id);
        return site;
    }
    @PostMapping ("/siteicon")   //获取指定景点照片
    public List<SiteIcon> site(int site_id){
        return siteMap.siteicon(site_id);
    }


}
