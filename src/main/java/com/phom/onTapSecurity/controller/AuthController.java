package com.phom.onTapSecurity.controller;


import com.phom.onTapSecurity.domain.DTO.request.ReqLoginDTO;
import com.phom.onTapSecurity.domain.DTO.response.ResLoginDTO;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.service.UserService;
import com.phom.onTapSecurity.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;


    @Value("${danglinh.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ReqLoginDTO userDTO) {
        try {
            // Tạo đối tượng Authentication từ username và password
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

            // Thực hiện xác thực
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(usernamePasswordAuthenticationToken);

            User currentUser = userService.findByUsername(userDTO.getUsername());

            // Tạo token truy cập
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            ResLoginDTO.UserLoginDTO userLoginDTO = new ResLoginDTO.UserLoginDTO(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());
            resLoginDTO.setUser(userLoginDTO);


            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println(authentication.getName());

            String accessToken = securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());
            resLoginDTO.setToken(accessToken);


            String refreshToken = securityUtil.createRefreshToken(userDTO.getUsername(), resLoginDTO);

            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .maxAge(refreshTokenExpiration)
                    .path("/")
                    .secure(true)
                    .build();
            // Trả về token
            userService.saveRefreshToken(currentUser.getEmail(), refreshToken);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            // Log lỗi nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", defaultValue = "abc") String refreshToken) {
        if (refreshToken.equals("abc")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error");
        }
        Jwt decodedToken = userService.checkRefreshToken(refreshToken);

        String email = decodedToken.getSubject();

        User currentUser = userService.getUserByEmailAndRefreshToken(email, refreshToken);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error");
        }

        try {
            // Tạo token truy cập
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            ResLoginDTO.UserLoginDTO userLoginDTO = new ResLoginDTO.UserLoginDTO(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());
            resLoginDTO.setUser(userLoginDTO);

            String accessToken = securityUtil.createAccessToken(currentUser.getEmail(), resLoginDTO.getUser());
            resLoginDTO.setToken(accessToken);


            String newRefreshToken = securityUtil.createRefreshToken(currentUser.getEmail(), resLoginDTO);

            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true)
                    .maxAge(refreshTokenExpiration)
                    .path("/")
                    .secure(true)
                    .build();
            // Trả về token
            userService.saveRefreshToken(currentUser.getEmail(), newRefreshToken);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            // Log lỗi nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

    }


    @GetMapping("/account")
    public ResponseEntity<ResLoginDTO.GetAccountDTO> getAccount() {
        String mail = SecurityUtil.getCurrentUserLogin().orElse("null");
        User currentUser = userService.findByUsername(mail);

        ResLoginDTO.UserLoginDTO userLoginDTO = new ResLoginDTO.UserLoginDTO(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());

        ResLoginDTO.GetAccountDTO getAccountDTO = new ResLoginDTO.GetAccountDTO();
        getAccountDTO.setUser(userLoginDTO);

        return ResponseEntity.ok().body(getAccountDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String mail = SecurityUtil.getCurrentUserLogin().orElse("null");

        userService.deleteRefreshTokenByEmail(mail);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .secure(true)
                .build();


        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body("logged out");
    }
}
