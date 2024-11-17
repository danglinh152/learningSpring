package com.phom.onTapSecurity.util;


import com.phom.onTapSecurity.domain.DTO.ResLoginDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${danglinh.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${danglinh.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public SecurityUtil(JwtEncoder jwtEncoder, @Qualifier("jwtDecoder") JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String createAccessToken(String email, ResLoginDTO.UserLoginDTO userLoginDTO) {
        // Lấy danh sách quyền của người dùng
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity = now.plusSeconds(accessTokenExpiration); // Đảm bảo tokenExpiration là dương

        List<String> listAuthorities = new ArrayList<>();
        listAuthorities.add("ROLE_USER");
        listAuthorities.add("ROLE_ADMIN");

        // Tạo claims cho token
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userLoginDTO)
                .claim("authorities", listAuthorities)
                .build();

        // Tạo header cho token
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(String email, ResLoginDTO resLoginDTO) {

        Instant now = Instant.now();
        Instant validity = now.plusSeconds(refreshTokenExpiration); // Đảm bảo tokenExpiration là dương

        // Tạo claims cho token
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", resLoginDTO.getUser())
                .build();

        // Tạo header cho token
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

//    public boolean isTokenExpired(String token) {
//        try {
//            Jwt jwt = jwtDecoder.decode(token); // Giải mã token
//            Instant expiration = jwt.getExpiresAt(); // Lấy thời gian hết hạn
//            // Kiểm tra xem token đã hết hạn chưa
//            return expiration != null && expiration.isBefore(Instant.now());
//        } catch (JwtException e) {
//            // Nếu có lỗi trong việc giải mã, coi như token không hợp lệ
//            return true;
//        }
//    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

}
