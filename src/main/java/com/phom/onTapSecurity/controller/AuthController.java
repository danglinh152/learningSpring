package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.DTO.LoginDTO;
import com.phom.onTapSecurity.domain.DTO.ResLoginDTO;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.service.UserService;
import com.phom.onTapSecurity.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO userDTO) {
        try {
            // Tạo đối tượng Authentication từ username và password
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

            // Thực hiện xác thực
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(usernamePasswordAuthenticationToken);

            User currentUser = userService.findByUsername(userDTO.getUsername());

            // Tạo token truy cập
            String accessToken = securityUtil.createToken(authentication);
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            resLoginDTO.setToken(accessToken);
            ResLoginDTO.UserLoginDTO userLoginDTO = new ResLoginDTO.UserLoginDTO(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());
            resLoginDTO.setUser(userLoginDTO);


            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Trả về token
            return ResponseEntity.ok(resLoginDTO);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            // Log lỗi nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


}
