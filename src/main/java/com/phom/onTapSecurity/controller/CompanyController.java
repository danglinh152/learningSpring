package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.Company;
import com.phom.onTapSecurity.domain.Message;
import com.phom.onTapSecurity.domain.ResultPagination;
import com.phom.onTapSecurity.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> postCompany(@RequestBody @Valid Company company) {
        companyService.addCompany(company);
        return ResponseEntity.ok(company);
    }


    @GetMapping("/companies")
    public ResponseEntity<ResultPagination> getAllCompanies(@Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.ok(companyService.getAllCompanies(spec, pageable));
    }

//    @GetMapping("/companies")
//    public ResponseEntity<ResultPagination> getAllCompanies(@Filter Specification<Company> specification) {
//
//        return ResponseEntity.ok(companyService.getAllCompanies(specification));
//    }

    @PutMapping("/companies")
    public ResponseEntity<Company> putCompany(@RequestBody @Valid Company company) {
        Company companyOrigin = companyService.getCompany(company.getId());
        companyOrigin.setName(company.getName());
        companyOrigin.setAddress(company.getAddress());
        companyOrigin.setDescription(company.getDescription());
        companyOrigin.setLogo(company.getLogo());
        companyService.updateCompany(companyOrigin);
        return ResponseEntity.ok(companyOrigin);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Message> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(new Message("Deleted"));
    }
}
