package com.dataart.project1.repo;

import com.dataart.project1.entity.MarketListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
public interface MarketRepo extends JpaRepository<MarketListing, Long> {
}
