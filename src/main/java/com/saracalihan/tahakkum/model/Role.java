package com.saracalihan.tahakkum.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.saracalihan.tahakkum.constant.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity(name="roles")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    // @OneToMany(mappedBy = "users")
    public Roles slug = Roles.User;

    @CreatedDate
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Boolean isAdmin(){
        return slug == Roles.Admin;
    }

    public Role(Roles r){
        slug = r;
    }
}
