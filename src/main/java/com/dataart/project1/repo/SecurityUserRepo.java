package com.dataart.project1.repo;

import com.dataart.project1.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface SecurityUserRepo extends JpaRepository<SecurityUser, Long> {

    SecurityUser findByUsernameAndPassword(String username, String password);

}
