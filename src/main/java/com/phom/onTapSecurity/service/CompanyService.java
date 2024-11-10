package com.phom.onTapSecurity.service;


import com.phom.onTapSecurity.domain.Company;
import com.phom.onTapSecurity.domain.Meta;
import com.phom.onTapSecurity.domain.ResultPagination;
import com.phom.onTapSecurity.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getCompany(long id) {
        return companyRepository.getCompanyById(id);
    }

    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    public ResultPagination getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = companyRepository.findAll(spec, pageable);

        Meta mt = new Meta();
        mt.setPage(pageCompanies.getNumber() + 1);
        mt.setPageSize(pageCompanies.getSize());
        mt.setTotalPages(pageCompanies.getTotalPages());
        mt.setTotalElements(pageCompanies.getTotalElements());

        ResultPagination rs = new ResultPagination();

        rs.setMeta(mt);
        rs.setData(pageCompanies.getContent());


        return rs;
    }

//    public ResultPagination getAllCompanies(Specification<Company> spec, Pageable pageable) {
//        Page<Company> pageCompanies = companyRepository.findAll(spec, pageable);
//
//
//        ResultPagination rs = new ResultPagination();
//
//        rs.setData(pageCompanies);
//
//
//        return rs;
//    }


    public void deleteCompany(long id) {
        companyRepository.deleteById(id);
    }

    public void updateCompany(Company company) {
        companyRepository.save(company);
    }
}
