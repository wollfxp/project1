package com.dataart.project1.repo;

import com.dataart.project1.entity.StarshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarshipTypeRepo extends JpaRepository<StarshipType, Long> {
    StarshipType findByType(String shipType);
    List<StarshipType> findByIsEnemyOnlyFalse();
}
