package com.sanalab.sijiusu.auth.database.repository;

import com.sanalab.sijiusu.auth.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Long countByEmail(String email);

}
