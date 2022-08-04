package com.obss.AgileExpress.helper;

import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;


@Data
@RequiredArgsConstructor
@Service
public class AuthHelper {
    private final UserRepository userRepository;

    public User getUserPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        return userRepository.findByUsername(username);
    }

    public Boolean isUserPrincipal(String userIdOrUsername) {
        User user = getUserPrincipal();
        return user.getUsername().equals(userIdOrUsername) || user.getId().equals(userIdOrUsername);
    }

}
