package com.phom.onTapSecurity.service;

import com.nimbusds.jose.util.Base64;
import com.phom.onTapSecurity.domain.DTO.response.ResResultPaginationDTO;
import com.phom.onTapSecurity.domain.DTO.response.ResUserDTO;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.List;
import java.util.stream.Collectors;

import static com.phom.onTapSecurity.util.SecurityUtil.JWT_ALGORITHM;


@Service
public class UserService {
    private final UserRepository userRepository;
    @Value("${danglinh.jwt.base64-secret}")
    private String base64Secret;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(spec, pageable);

        ResResultPaginationDTO.Meta mt = new ResResultPaginationDTO.Meta();
        mt.setPage(pageUsers.getNumber() + 1);
        mt.setPageSize(pageUsers.getSize());
        mt.setTotalPages(pageUsers.getTotalPages());
        mt.setTotalElements(pageUsers.getTotalElements());

        ResResultPaginationDTO rs = new ResResultPaginationDTO();

        rs.setMeta(mt);

        List<ResUserDTO> users = pageUsers.getContent().stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getFirstName(),
                item.getLastName(),
                item.getEmail(),
                item.getAge(),
                item.getGender(),
                item.getAddress(),
                new ResUserDTO.ResCompanyDTO(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null
                )
        )).collect(Collectors.toList());

        rs.setData(users);


        return rs;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void saveRefreshToken(String email, String refreshToken) {
        User user = this.findByUsername(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public Jwt checkRefreshToken(String refreshToken) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            Jwt jwt = jwtDecoder.decode(refreshToken);
            return jwt;
        } catch (Exception e) {
            return null;
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(base64Secret).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.name());
    }

    public User getUserByEmailAndRefreshToken(String email, String refreshToken) {
        return userRepository.findByEmailAndRefreshToken(email, refreshToken);
    }

    public void deleteRefreshTokenByEmail(String email) {
        User user = userRepository.findByEmail(email);
        user.setRefreshToken(null);
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();

        resUserDTO.setFirstName(user.getFirstName());
        resUserDTO.setLastName(user.getLastName());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setAge(user.getAge());
        resUserDTO.setGender(user.getGender());
        resUserDTO.setAddress(user.getAddress());
        if (user.getCompany() != null) {
            resUserDTO.setCompany(new ResUserDTO.ResCompanyDTO(user.getCompany().getId(), user.getCompany().getName()));
        } else {
            resUserDTO.setCompany(null);
        }

        return resUserDTO;
    }
}
