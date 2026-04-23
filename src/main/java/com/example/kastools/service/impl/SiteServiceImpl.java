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

    @Override
    public List<Site_list> searchSites(String keyword, int start) {
        return siteMap.searchByName(keyword, start);
    }

    @Override
    public int countSearchSites(String keyword) {
        return siteMap.countByKeyword(keyword);
    }

    @Override
    public Site_list saveSite(Site_list site) {
        if (site.getId() == 0) {
            siteMap.insertSite(site);
        } else {
            siteMap.updateSite(site);
        }
        return site;
    }

    @Override
    public boolean deleteSite(int id) {
        return siteMap.deleteSite(id) > 0;
    }

    @Override
    public List<Site_list> getSitesByRating(double minRating, double maxRating, int start) {
        return siteMap.findByRating(minRating, maxRating, start);
    }

    @Override
    public List<Site_list> searchSitesByRating(String keyword, double minRating, double maxRating, int start) {
        return siteMap.searchByRatingAndKeyword(minRating, maxRating, keyword, start);
    }

    @Override
    public List<Site_list> getAllSites() {
        return siteMap.findAll();
    }

    @Override
    public List<Site_list> getTopSitesByCollectionCount(int limit) {
        return siteMap.findTopByCollectionCount(limit);
    }
}
