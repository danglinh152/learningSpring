package com.phom.onTapSecurity.repository;

import com.phom.onTapSecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);

    public User findById(long id);

    public boolean existsByEmail(String email);
}
