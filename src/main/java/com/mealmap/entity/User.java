package com.mealmap.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String supabaseId;

    @Column(unique = true, nullable = false)
    private String email;

    private String username;

    private Instant createdAt;

    public User() {}

    public User(String supabaseId, String email, String username) {
        this.supabaseId = supabaseId;
        this.email = email;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSupabaseId() {
        return supabaseId;
    }

    public void setSupabaseId(String supabaseId) {
        this.supabaseId = supabaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return (
            "User{" +
            "userId=" +
            userId +
            ", supabaseId='" +
            supabaseId +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", createdAt=" +
            createdAt +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
