package com.dataart.project1.repo;

import com.dataart.project1.entity.Mission;
import com.dataart.project1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MissionRepo extends JpaRepository<Mission, Long> {
    Set<Mission> findAllByUser(User currentUser);

    @Query("SELECT m FROM Mission m WHERE m.status = com.dataart.project1.entity.MissionStatus.IN_PROGRESS AND m.user=:currentUser")
    Set<Mission> findAllByUserAndStatusIsLikeInProgress(@Param("currentUser") User currentUser);
}
