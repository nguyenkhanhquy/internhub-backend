package com.internhub.backend.repository;

import com.internhub.backend.entity.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByName(String name);
}
