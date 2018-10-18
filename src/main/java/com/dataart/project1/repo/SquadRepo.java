package com.dataart.project1.repo;

import com.dataart.project1.entity.Squad;
import com.dataart.project1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface SquadRepo extends JpaRepository<Squad, Long> {
    List<Squad> findAllByOwner(User owner);
    List<Squad> findAllByOwnerAndActiveMissionIsNull(User owner);
    List<Squad> findAllByOwnerAndActiveMissionIsNullAndShipsIsNotEmpty(User owner);
    Squad findByNameAndOwner(String name, User owner);
    Squad findByIdAndOwner(Long id, User owner);
}
