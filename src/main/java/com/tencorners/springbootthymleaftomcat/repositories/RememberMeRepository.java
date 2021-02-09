package com.tencorners.springbootthymleaftomcat.repositories;

import com.tencorners.springbootthymleaftomcat.entities.RememberMe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RememberMeRepository extends JpaRepository<RememberMe, Long> {

    List<RememberMe> getAllByUsername(String username);
    Optional<RememberMe> getBySeries(String series);

}