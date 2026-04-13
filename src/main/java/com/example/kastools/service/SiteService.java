package com.example.kastools.service;

import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;

import java.util.List;

public interface SiteService {

    List<Site_list> getSiteList(int start);

    Site_list getSiteById(int siteId);

    List<SiteIcon> getSiteIcons(int siteId);

    List<Site_list> searchSites(String keyword, int start);

    int countSearchSites(String keyword);
}
