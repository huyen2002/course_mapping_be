package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.SearchUserParams;
import com.example.course_mapping_be.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user u WHERE BINARY u.username = :name", nativeQuery = true)
    Optional<User> findByUsername(String name);


    @Query("SELECT u FROM User u WHERE (:role IS NULL OR u.role = :role) AND (:createYear IS NULL OR YEAR(u.createAt) = :createYear) AND (:createMonth IS NULL OR MONTH(u.createAt) = :createMonth)")
    Page<User> searchUsers(RoleType role, Integer createYear, Integer createMonth, Pageable pageable);
}
