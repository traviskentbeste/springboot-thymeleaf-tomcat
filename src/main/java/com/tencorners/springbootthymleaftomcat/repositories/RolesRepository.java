package com.tencorners.springbootthymleaftomcat.repositories;

import com.tencorners.springbootthymleaftomcat.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);

}