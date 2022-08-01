package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.LdapUser;
import com.obss.AgileExpress.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("hiGoogle")
    public Map<String,Object> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String,Object>  attr= oAuth2AuthenticationToken.getPrincipal().getAttributes();
        return attr;
    }


    private final AuthService authService;

    @PreAuthorize("hasRole('TeamMember')")
    @GetMapping("/getUser")
    public String index() {
        LdapUser ldapUser = authService.getUser("burakkaragol60@gmail.com");
        return ldapUser.toString();
    }

    @PreAuthorize("hasRole('TeamMember')")

    @GetMapping("/user/deneme")
    public String TeamMember() {
        return "hi TeamMember";
    }
    @PreAuthorize("hasRole('ROLE_TeamLeader')")
    @GetMapping("/movie/deneme")
    public String TeamLeader() {
        return "hi TeamLeader";
    }
    @PreAuthorize("hasRole('ROLE_Admin')")

    @GetMapping("/admin/deneme")
    public String AdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "hi Admin";
    }


}
