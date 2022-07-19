package com.obss.AgileExpress.service;

import com.obss.AgileExpress.entity.LdapUser;
import com.obss.AgileExpress.repository.LdapUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final LdapTemplate ldapTemplate;
    private final LdapUserRepository ldapUserRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Boolean authenticate(String e, String p) {
        return ldapUserRepository.findLdapUserByEmailAndPassword(e, p) != null;
    }

    public LdapUser getUser(String email) {
        //LdapUser requestedUser = ldapUserRepository.findLdapUserByUsername(username) != null ? new LdapUser() : ldapUserRepository.findLdapUserByUsername(username);
        try {
            LdapUser requestedUser = ldapUserRepository.findLdapUserByEmail(email);
            System.out.println(requestedUser);
            return requestedUser;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }


    //when new login request
    public void create(String username,String email, String password) {
        LdapUser newUser = new LdapUser(LdapUtils.emptyLdapName(),username,email,passwordEncoder.encode(password));
        ldapUserRepository.save(newUser);
    }

}
