package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.LdapUser;
import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.repository.LdapUserRepository;
import com.obss.AgileExpress.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MyAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private final UserRepository userRepository;
    private final LdapUserRepository ldapUserRepository;

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) throws UsernameNotFoundException {
        LdapUser ldapUser = ldapUserRepository.findLdapUserByUsername(username);
        User user = userRepository.findUserByEmail(ldapUser.getEmail());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }
}