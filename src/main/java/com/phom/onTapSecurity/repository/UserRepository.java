package com.phom.onTapSecurity.repository;

import com.phom.onTapSecurity.domain.Company;
import com.phom.onTapSecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);

    public User findById(long id);

    public boolean existsByEmail(String email);

    public User findByEmailAndRefreshToken(String email, String refreshToken);

    public List<User> findByCompany(Company company);

}
