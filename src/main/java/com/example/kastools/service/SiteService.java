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

    Site_list saveSite(Site_list site);

    boolean deleteSite(int id);

    List<Site_list> getSitesByRating(double minRating, double maxRating, int start);

    List<Site_list> searchSitesByRating(String keyword, double minRating, double maxRating, int start);

    List<Site_list> getAllSites();

    List<Site_list> getTopSitesByCollectionCount(int limit);
}
