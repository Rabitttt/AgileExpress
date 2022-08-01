package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.LdapUser;
import com.obss.AgileExpress.repository.LdapUserRepository;
import lombok.RequiredArgsConstructor;
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

    //DEMO
    public Boolean authenticate(String e, String p) {
        return ldapUserRepository.findLdapUserByEmailAndPassword(e, p) != null;
    }

    // DEMO
    public LdapUser getUser(String username) {
        //LdapUser requestedUser = ldapUserRepository.findLdapUserByUsername(username) != null ? new LdapUser() : ldapUserRepository.findLdapUserByUsername(username);
        try {
            LdapUser requestedUser = ldapUserRepository.findLdapUserByUsername(username);
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

/*
// Get user from mongo database and load it to ldap server
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //User yükle spring security için
        LdapUser user = ldapUserRepository.findLdapUserByEmail(email);

        if (getUser("Ben Alex") != null)
        {
            UserDetails userDetails = (UserDetails) getUser("Ben Alex");

            //Add user to ldap
            create(user.getUsername(), user.getEmail(), user.getPassword());

            return userDetails;
        }
        else {
            return null;
        }

    }

 */

}
