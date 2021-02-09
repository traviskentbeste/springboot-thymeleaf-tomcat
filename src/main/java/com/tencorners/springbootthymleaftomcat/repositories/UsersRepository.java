package com.tencorners.springbootthymleaftomcat.repositories;

import com.tencorners.springbootthymleaftomcat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
