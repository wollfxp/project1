package com.dataart.project1.repo;

import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface StarshipRepo extends JpaRepository<Starship, Long> {
    Optional<Starship> findById(Long id);
    List<Starship> findAllByOwner(User user);
    List<Starship> findAllByOwnerAndSquadIsNull(User user);
    Long countByOwner(User user);
}
