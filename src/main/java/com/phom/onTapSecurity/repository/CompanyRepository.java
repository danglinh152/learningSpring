package com.phom.onTapSecurity.repository;

import com.phom.onTapSecurity.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    public Company getCompanyById(long id);
}
