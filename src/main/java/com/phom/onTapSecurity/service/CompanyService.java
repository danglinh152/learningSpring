package com.phom.onTapSecurity.service;


import com.phom.onTapSecurity.domain.Company;
import com.phom.onTapSecurity.domain.DTO.response.ResResultPaginationDTO;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.repository.CompanyRepository;
import com.phom.onTapSecurity.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final UserService userService;
    private final UserRepository userRepository;
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository, UserService userService, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Company getCompany(long id) {
        return companyRepository.getCompanyById(id);
    }

    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    public ResResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = companyRepository.findAll(spec, pageable);

        ResResultPaginationDTO.Meta mt = new ResResultPaginationDTO.Meta();
        mt.setPage(pageCompanies.getNumber() + 1);
        mt.setPageSize(pageCompanies.getSize());
        mt.setTotalPages(pageCompanies.getTotalPages());
        mt.setTotalElements(pageCompanies.getTotalElements());

        ResResultPaginationDTO rs = new ResResultPaginationDTO();

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
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            List<User> users = userRepository.findByCompany(company.get());
            userRepository.deleteAll(users);
        }

        companyRepository.deleteById(id);
    }

    public void updateCompany(Company company) {
        companyRepository.save(company);
    }
}
