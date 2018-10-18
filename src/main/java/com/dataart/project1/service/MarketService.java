package com.dataart.project1.service;

import com.dataart.project1.entity.MarketListing;
import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.MarketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MarketService {

    private UserDataService userDataService;
    private MarketRepo marketRepo;

    @Autowired
    public MarketService(UserDataService userDataService, MarketRepo marketRepo) {
        this.userDataService = userDataService;
        this.marketRepo = marketRepo;
    }

    @Transactional
    public MarketListing createListing(User user, Starship ship, BigDecimal price) {

        if (ship.getOwner() != user || !user.getOwnedShips().contains(ship)) {
            throw new RuntimeException("Starship doesn't belong to user");
        }

        MarketListing newListing = new MarketListing();
        newListing.setOwner(user);
        newListing.setShip(ship);
        newListing.setPrice(price);

        MarketListing savedListing = marketRepo.save(newListing);
        user.getOwnedShips().remove(ship);
        userDataService.save(user);

        return savedListing;
    }

    @Transactional
    public Boolean purchaseShip(User user, MarketListing listing) {
        if (user.getCredits().compareTo(listing.getPrice()) > 0 && !listing.getOwner().equals(user)) {
            user.setCredits(user.getCredits().subtract(listing.getPrice()));
            user.getOwnedShips().add(listing.getShip());
            listing.getShip().setOwner(user);
            listing.getOwner().setCredits(listing.getOwner().getCredits().add(listing.getPrice()));

            userDataService.save(user);
            userDataService.save(listing.getOwner());

            removeListing(listing);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public List<MarketListing> getListings() {
        return marketRepo.findAll();
    }

    /*
        Removing market listing returns ship to someone's hangar
     */
    @Transactional
    public void removeListing(MarketListing listing) {
        marketRepo.delete(listing);
    }
}
