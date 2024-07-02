package com.example.demo.model;

import java.time.LocalTime;

import org.springframework.data.annotation.CreatedDate;

import com.example.demo.constant.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name="roles")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    // @OneToMany(mappedBy = "users")
    public Roles slug = Roles.User;

    @CreatedDate
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalTime createdAt;

    public Boolean isAdmin(){
        return slug == Roles.Admin;
    }

    public Role(Roles r){
        slug = r;
    }
}
