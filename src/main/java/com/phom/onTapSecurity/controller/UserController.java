package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.DTO.UserDTO;
import com.phom.onTapSecurity.domain.Message;
import com.phom.onTapSecurity.domain.ResultPagination;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.service.UserService;
import com.phom.onTapSecurity.util.SecurityUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
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
    public ResponseEntity<?> post(@RequestBody User userParam) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() ||
//                securityUtil.isTokenExpired(jwt.getTokenValue())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        if (userService.isEmailExist(userParam.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Email existed!"));
        }
        // Tiến hành thêm người dùng mới
        User user = new User();
        user.setPassword(passwordEncoder.encode(userParam.getPassword()));
        user.setFirstName(userParam.getFirstName());
        user.setLastName(userParam.getLastName());
        user.setEmail(userParam.getEmail());
        user.setGender(userParam.getGender());
        user.setAddress(userParam.getAddress());
        user.setAge(userParam.getAge());


        UserDTO userDTO = new UserDTO(user);
        userService.addUser(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> putUser(@RequestBody @Valid User user) {
        User userOrigin = userService.findById(user.getId());
        userOrigin.setFirstName(user.getFirstName());
        userOrigin.setLastName(user.getLastName());
        userOrigin.setEmail(user.getEmail());
        userOrigin.setPassword(passwordEncoder.encode(user.getPassword()));
        userOrigin.setAge(user.getAge());
        userOrigin.setAddress(user.getAddress());

        UserDTO userDTO = new UserDTO(userOrigin);
        userService.updateUser(userOrigin);
        return ResponseEntity.ok(userDTO);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Deleted");
    }

}
