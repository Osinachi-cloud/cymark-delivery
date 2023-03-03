package com.cymark.cymarkdelivery.repository;

import java.util.Optional;

import com.cymark.cymarkdelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Transactional
    void deleteByEmail(String email);
}