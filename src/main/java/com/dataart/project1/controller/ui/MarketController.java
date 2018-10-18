package com.dataart.project1.controller.ui;

import com.dataart.project1.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MarketController {

    private MarketService marketService;
    private final static String GET_LISTINGS_PAGE = "market/market";
    private final static String CREATE_LISTING_PAGE = "market/create";

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PreAuthorize("isFullyAuthenticated()")
    @RequestMapping("/market/list")
    public String getMarketListingsPage(Model model) {
        model.addAttribute("listings", marketService.getListings());
        return GET_LISTINGS_PAGE;
    }

    @PreAuthorize("isFullyAuthenticated()")
    @RequestMapping("/market/create")
    public String getMarketCreatePage(Model model) {
        return CREATE_LISTING_PAGE;
    }
}
