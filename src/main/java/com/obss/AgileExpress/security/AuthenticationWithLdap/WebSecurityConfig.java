package com.obss.AgileExpress.security.AuthenticationWithLdap;

import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.service.MyAuthoritiesPopulator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    //CustomAuthoritiesPopulator authoritiespopulator;
    private final MyAuthoritiesPopulator myAuthoritiesPopulator;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .ldapAuthoritiesPopulator(myAuthoritiesPopulator)
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
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**", "src/main/resources/static/**").permitAll()
                .antMatchers("/user/deneme").hasAuthority("ROLE_TeamMember")
                .antMatchers("/user/deneme").hasAuthority("ROLE_Admin")
                .antMatchers("/user/deneme").hasAuthority("ROLE_TeamLeader")
                .anyRequest()
                .authenticated()
                .and()

                .formLogin()
                //.defaultSuccessUrl("http://localhost:3000", true)
                .and()
                .oauth2Login();

    }



}
