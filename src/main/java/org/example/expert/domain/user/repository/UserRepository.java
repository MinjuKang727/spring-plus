package org.example.expert.domain.user.repository;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u.id, u.email, u.nickname, u.userRole from User u where u.id = :email")
    Optional<AuthUser> findAuthUserByEmail(String email);
}
