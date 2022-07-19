package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.entity.LdapUser;
import com.obss.AgileExpress.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/getUser")
    public String index() {
        LdapUser ldapUser = authService.getUser("burakkaragol60@gmail.com");
        return ldapUser.toString();
    }

}
