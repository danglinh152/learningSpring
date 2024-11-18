package com.phom.onTapSecurity.config;

import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
public class UserDetailsCustomService implements UserDetailsService {

    private final UserService userService;
    public UserDetailsCustomService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword()
                , Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
