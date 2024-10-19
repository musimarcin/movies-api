package com.movies_api.data.repository;

import com.movies_api.data.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
//
//    @NonNull
//    Optional<User> findById(@Nullable Long id);
}
