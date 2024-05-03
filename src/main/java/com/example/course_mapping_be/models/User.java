package com.example.course_mapping_be.models;


import com.example.course_mapping_be.constraints.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email(message = "Email is invalid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @CreatedDate
    @Column(name = "createAt", nullable = false)
    private Date createAt;

    @LastModifiedDate
    @Column(name = "updateAt", nullable = false)
    private Date updateAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;


    public User(String email, String password, String username, RoleType role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
