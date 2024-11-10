package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.Company;
import com.phom.onTapSecurity.domain.DTO.ResponseToken;
import com.phom.onTapSecurity.domain.ResultPagination;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.service.UserService;
import com.phom.onTapSecurity.util.SecurityUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private SecurityUtil securityUtil;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, SecurityUtil securityUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
    }


    @GetMapping("/users")
    public ResponseEntity<ResultPagination> get(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(userService.getAllUser(spec, pageable));

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> post(@RequestBody User userParam, @AuthenticationPrincipal Jwt jwt) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() ||
//                securityUtil.isTokenExpired(jwt.getTokenValue())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        // Tiến hành thêm người dùng mới
        User user = new User();
        user.setPassword(passwordEncoder.encode(userParam.getPassword()));
        user.setFirstName(userParam.getFirstName());
        user.setLastName(userParam.getLastName());
        user.setEmail(userParam.getEmail());
        userService.addUser(user);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> putUser(@RequestBody @Valid User user) {
        User userOrigin = userService.findById(user.getId());
        userOrigin.setFirstName(user.getFirstName());
        userOrigin.setLastName(user.getLastName());
        userOrigin.setEmail(user.getEmail());
        userOrigin.setPassword(passwordEncoder.encode(user.getPassword()));
        userOrigin.setAge(user.getAge());
        userOrigin.setAddress(user.getAddress());

        userOrigin.updateUser(userOrigin);
        return ResponseEntity.ok(userOrigin);
    }


    @DeleteMapping("/users")
    public ResponseEntity<String> delete(@RequestParam(name = "id") long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Deleted");
    }

}
