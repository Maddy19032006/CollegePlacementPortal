package com.college.placement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.placement.entity.Role;
import com.college.placement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRemovedFalse();

    List<User> findByRoleAndRemovedFalse(Role role);
}
