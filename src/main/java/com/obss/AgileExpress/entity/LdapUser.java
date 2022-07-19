package com.obss.AgileExpress.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;


@Entry(
        base = "ou=people,dc=springframework,dc=org",
        objectClasses = { "person", "inetOrgPerson", "top" })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LdapUser {
    @Id
    private Name id;

    private @Attribute(name = "cn") String username;
    private @Attribute(name = "mail") String email;
    private @Attribute(name = "userPassword") String password;

    // standard getters/setters
}
