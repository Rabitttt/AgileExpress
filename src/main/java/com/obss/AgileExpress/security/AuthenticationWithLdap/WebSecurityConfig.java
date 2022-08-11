package com.obss.AgileExpress.security.AuthenticationWithLdap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.AgileExpress.security.GoogleAuthenctication.CustomOAuth2UserService;
import com.obss.AgileExpress.service.UserService;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    //CustomAuthoritiesPopulator authoritiespopulator;
    private final MyAuthoritiesPopulator myAuthoritiesPopulator;
    private final UserService userService;

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
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/*", "/js/*").permitAll()
                .antMatchers("/**", "src/main/resources/static/**").permitAll()
                //.antMatchers("/user/deneme").hasAuthority("ROLE_TeamMember")
                //.antMatchers("/user/deneme").hasAuthority("ROLE_Admin")
                //.antMatchers("/user/deneme").hasAuthority("ROLE_TeamLeader")
                .antMatchers("/login/**").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("login/oauth/**").permitAll()
                .antMatchers("login/**").permitAll()
                .antMatchers("/login/oauth2/code/google").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

                        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        String username = oauthUser.getAttribute("given_name");

                        //Authorization
                        String[] roles = oauthUser.getAttribute("authorities");
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        if(roles == null) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
                        } else {
                            Arrays.stream(roles).forEach(role -> {
                                authorities.add(new SimpleGrantedAuthority(role));
                            });
                        }


                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        //Object idToken = ((DefaultOidcUser) oauthUser).getIdToken().getTokenValue();


                        //OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) idToken;

                        String access_token = JWT.create()
                                .withSubject(username)
                                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000 * 24 * 2))
                                .withIssuer(request.getRequestURI().toString())
                                .withClaim("roles",authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                                .sign(algorithm);
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(),access_token);
                    }
                })
                .and()
                .formLogin();

        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
       http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    private static final List<String> EXPOSED_HEADERS = Arrays.asList("Cache-Control", "Content-Language", "Content-Type", "Expires", "Last-Modified", "Pragma", "Authorization");



    private final CustomOAuth2UserService oauthUserService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
