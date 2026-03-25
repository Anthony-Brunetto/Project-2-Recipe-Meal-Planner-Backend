package com.mealmap.repository;

import com.mealmap.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySupabaseId(String supabaseId);
    boolean existsBySupabaseId(String supabaseId);
    Optional<User> findByEmail(String email);
}
