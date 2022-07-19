package com.obss.AgileExpress.repository;

import com.obss.AgileExpress.entity.LdapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {

    LdapUser findLdapUserByEmail(String email);
    LdapUser findLdapUserByEmailAndPassword(String username, String password);
    LdapUser findLdapUserByUsername(String name);
}