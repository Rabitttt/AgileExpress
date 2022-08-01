package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.repository.ElsaticSearch.UserESRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthService authService;
    private final UserESRepository userESRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User saveUser(User user) {
        log.info("Saving new user {} to database",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = userRepository.save(user);
        userESRepository.save(UserES.builder().id(userEntity.getId()).email(userEntity.getEmail()).username(userEntity.getUsername()).roles(userEntity.getRoles()).build());
        return userEntity;
    }

    public User getUserByUsername(String username) {
        log.info("Fetching user {}",username);
        return userRepository.findByUsername(username);
    }

    public User getUserById(String userId) {
        return  userRepository.findById(userId).orElse(null);
    }

    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }


    /*
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
        authService.create(user.getUsername(),user.getEmail(),user.getPassword());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
     */

    /*
    @Bean
    public LdapUserDetailsService ldapUserDetailsService() {
        FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(ldapSearchBase, ldapSearchFilter, kerberosLdapContextSource());
        LdapUserDetailsService service = new LdapUserDetailsService(userSearch);
        service.setUserDetailsMapper(new LdapUserDetailsMapper());
        return service;
    }
    */
}
