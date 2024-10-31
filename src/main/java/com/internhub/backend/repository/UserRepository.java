package com.internhub.backend.repository;

import com.internhub.backend.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
