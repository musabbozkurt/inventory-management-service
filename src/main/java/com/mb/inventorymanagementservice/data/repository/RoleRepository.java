package com.mb.inventorymanagementservice.data.repository;

import com.mb.inventorymanagementservice.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findAllByDefaultRoleIsTrue();
}
