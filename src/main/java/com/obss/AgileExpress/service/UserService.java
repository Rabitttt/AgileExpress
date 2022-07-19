package com.obss.AgileExpress.service;

import com.obss.AgileExpress.entity.User;
import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveUser(User user) {
        log.info("Saving new user {} to database",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(UserRoles.TeamMember.name(),UserRoles.TeamLeader.name()));
        return userRepository.insert(user);
    }

    public User getUserByUsername(String username) {
        log.info("Fetching user {}",username);
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}",username);
        }
        //Collection<Role> roles = List.of(roleRepository.findById(user.getId()).orElseThrow());
        //user.setRoles(roles);
        //Collection<Role> roles = List.of(new Role("62a2a8031b57aa725c7d338c","ROLE_USER"));
        //user.setRoles(roles);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}