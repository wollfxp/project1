package com.dataart.project1.repo;

import com.dataart.project1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserRepo extends JpaRepository<User, Long> {
    User findByName(String name);
    Optional<User> findById(Long id);
}
