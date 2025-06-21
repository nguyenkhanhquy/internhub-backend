package com.internhub.backend.repository;

import com.internhub.backend.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByEmailContainingIgnoreCase(String email);
}
