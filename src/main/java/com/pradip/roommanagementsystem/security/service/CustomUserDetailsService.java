package com.pradip.roommanagementsystem.security.service;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.projection.JwtUser;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.security.dto.CustomUserDetails;
import com.pradip.roommanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUser user = (JwtUser) userService.getUserByEmail(username, "JwtUser");
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        CustomUserDetails customUserDetails =new CustomUserDetails();
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getEmail());
        customUserDetails.setFullName(user.getFirstName()+" "+user.getLastName());
        customUserDetails.setAuthorities(authorities);
        customUserDetails.setPassword(user.getPassword());

        return customUserDetails;
    }
}

