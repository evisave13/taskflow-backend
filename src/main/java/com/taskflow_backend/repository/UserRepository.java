package com.taskflow_backend.repository;

import com.taskflow_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository 
    extends JpaRepository<User, Long> {

    // Email se user dhundo (login ke liye)
    Optional<User> findByEmail(String email);

    // Username already exists check karo
    Boolean existsByUsername(String username);

    // Email already exists check karo
    Boolean existsByEmail(String email);
}