package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.DTO.ResLoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<ResLoginDTO> test() {
        ResLoginDTO test = new ResLoginDTO();
        return ResponseEntity.ok(test);
    }
}
