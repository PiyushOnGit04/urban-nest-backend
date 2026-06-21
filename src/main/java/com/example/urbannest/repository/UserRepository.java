package com.example.urbannest.repository;

import com.example.urbannest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to look up a user for authentication
    Optional<User> findByEmail(String email);

    // Quick validation check for registration
    boolean existsByEmail(String email);
}
