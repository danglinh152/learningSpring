package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.DTO.ResponseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<ResponseToken> test() {
        ResponseToken test = new ResponseToken("success");
        return ResponseEntity.ok(test);
    }
}
