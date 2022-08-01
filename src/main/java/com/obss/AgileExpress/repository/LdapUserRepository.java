package com.obss.AgileExpress.repository;

import com.obss.AgileExpress.documents.LdapUser;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {

    LdapUser findLdapUserByEmail(String email);
    LdapUser findLdapUserByEmailAndPassword(String username, String password);
    LdapUser findLdapUserByUsername(String name);
}