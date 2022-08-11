package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.helper.AuthHelper;
import com.obss.AgileExpress.repository.ElsaticSearch.UserESRepository;
import com.obss.AgileExpress.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthService authService;
    private final UserESRepository userESRepository;

    private final AuthHelper authHelper;


    public void processOAuthPostLogin(String username) {
        User existUser = userRepository.findByUsername(username);

        /*
        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setEnabled(true);

            repo.save(newUser);
        }
         */

    }



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User saveUser(User user) {
        log.info("Saving new user {} to database",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = userRepository.save(user);
        userESRepository.save(UserES.builder().id(userEntity.getId()).email(userEntity.getEmail()).username(userEntity.getUsername()).roles(userEntity.getRoles()).build());
        return userEntity;
    }

    public User getUserByUsername(String username) {
        log.info("Fetching user {}",username);
        return userRepository.findByUsername(username);
    }

    public User getUserById(String userId) {
        return  userRepository.findById(userId).orElse(null);
    }

    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    /*
    public void addProjectToUser (Project project) {
        User user = authHelper.getUserPrincipal();
        user.getProjects().add(project);
        userRepository.save(user);
    }

    public void deleteProjectFromUser(Project project) {
        User user = authHelper.getUserPrincipal();
        user.getProjects().remove(project);
        userRepository.save(user);
    }
     */
}
