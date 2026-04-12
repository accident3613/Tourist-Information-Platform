package com.example.kastools.service.impl;

import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteMap siteMap;

    @Override
    public List<Site_list> getSiteList(int start) {
        return siteMap.list(start);
    }

    @Override
    public Site_list getSiteById(int siteId) {
        return siteMap.site(siteId);
    }

    @Override
    public List<SiteIcon> getSiteIcons(int siteId) {
        return siteMap.siteicon(siteId);
    }
}
