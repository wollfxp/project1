package com.dataart.project1;

import com.dataart.project1.entity.MarketListing;
import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.StarshipType;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.repo.StarshipTypeRepo;
import com.dataart.project1.service.MarketService;
import com.dataart.project1.service.UserDataService;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest/*(properties = {
        "spring.datasource.url=jdbc:tc:mysql:///starship",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.hibernate.ddl-auto=create"
})*/
public class RepoTests {

//    @ClassRule
//    public static MySQLContainer mysql = new MySQLContainer();

    @Autowired
    private MarketService marketService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private StarshipRepo repo;

    @Autowired
    private StarshipTypeRepo typeRepo;

    @Test
    @Transactional
    public void testMarketListingCreation() {

        User testUser = new User();
        testUser.setCredits(new BigDecimal(1000.f));
        testUser.setName("Big Tester");
        testUser.setOwnedShips(new ArrayList<>());
        testUser = userDataService.save(testUser);

        var newType = new StarshipType();
        newType.setName("Biguette");
        newType.setType("Carrier");
        typeRepo.save(newType);

        Starship newShip = new Starship();
        newShip.setOwner(testUser);
        newShip.setType(newType);
        newShip.setName("Biguette-12");
        repo.save(newShip);

        testUser.getOwnedShips().add(newShip);
        userDataService.save(testUser);

        MarketListing newListing = marketService.createListing(testUser, newShip, new BigDecimal(999.f));

        assertNotNull(newListing);
    }

    @Test
    @Transactional
    public void testMarketListingCreationAndPurchase() {

        User testUser = new User();
        testUser.setCredits(new BigDecimal(1000.f));
        testUser.setName("Big Tester");
        testUser.setOwnedShips(new ArrayList<>());

        User testUser2 = new User();
        testUser2.setCredits(new BigDecimal(1000.f));
        testUser2.setName("Big Tester");
        testUser2.setOwnedShips(new ArrayList<>());

        testUser = userDataService.save(testUser);
        testUser2 = userDataService.save(testUser2);


        var newType = new StarshipType();
        newType.setName("Biguette");
        newType.setType("Carrier");
        typeRepo.save(newType);

        Starship newShip = new Starship();
        newShip.setOwner(testUser);
        newShip.setType(newType);
        newShip.setName("Biguette-A9");
        repo.save(newShip);

        testUser.getOwnedShips().add(newShip);
        userDataService.save(testUser);

        MarketListing newListing = marketService.createListing(testUser, newShip, new BigDecimal(999.f));

        assertNotNull(newListing);

        boolean done = marketService.purchaseShip(testUser2, newListing);
        assert done;
        assertTrue(testUser2.getOwnedShips().contains(newShip));
        assertEquals(testUser2.getCredits(), new BigDecimal(1.f));
        assertEquals(testUser.getCredits(), new BigDecimal(999.f + 1000.f));
    }

}
