package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        List<User> users=  userService.getAllUsers();
        return users;
    }

    @GetMapping("getUserDetails/{username}")
    public User getUserByUsername(@PathVariable(value = "username") String username) {
        User user = userService.getUserByUsername(username);
        return user;
    }

    @GetMapping("/profile/{userId}")
    public User getUserProfile(@PathVariable(value = "userId") String userId) {
        return userService.getUserById(userId);
    }

}
