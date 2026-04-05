package com.finance.dashboard.repository;

import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
