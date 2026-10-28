package com.college.placement.security;

import com.college.placement.entity.User;
import com.college.placement.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Small helper so services can get the logged-in User entity without
 * duplicating security-context code everywhere.
 */
@Component
public class CurrentUser {

    private final UserRepository userRepository;

    public CurrentUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Returns the User entity for the authenticated username. */
    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new com.college.placement.exception.AccessDeniedException("No authenticated user");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new com.college.placement.exception.AccessDeniedException("User not found"));
    }
}
