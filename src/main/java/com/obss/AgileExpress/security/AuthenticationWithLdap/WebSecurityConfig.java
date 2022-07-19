package com.obss.AgileExpress.security.AuthenticationWithLdap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    //CustomAuthoritiesPopulator authoritiespopulator;
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                    .userDnPatterns("uid={0},ou=people")
                    .groupSearchBase("ou=groups")
                    .contextSource()
                        .url("ldap://localhost:8389/dc=springframework,dc=org")
                        .and()
                    .passwordCompare()
                        .passwordEncoder(new BCryptPasswordEncoder())
                        .passwordAttribute("userPassword");
                    //    .and()
                    //.ldapAuthoritiesPopulator(authoritiespopulator);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                    .and()
                        .formLogin()
                        //.defaultSuccessUrl("http://localhost:3000", true)
                    .and()
                        .oauth2Login();

    }


}
