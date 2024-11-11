package com.phom.onTapSecurity.service;

import com.phom.onTapSecurity.domain.Meta;
import com.phom.onTapSecurity.domain.ResultPagination;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResultPagination getAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(spec, pageable);

        Meta mt = new Meta();
        mt.setPage(pageUsers.getNumber() + 1);
        mt.setPageSize(pageUsers.getSize());
        mt.setTotalPages(pageUsers.getTotalPages());
        mt.setTotalElements(pageUsers.getTotalElements());

        ResultPagination rs = new ResultPagination();

        rs.setMeta(mt);
        rs.setData(pageUsers.getContent());


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
}
