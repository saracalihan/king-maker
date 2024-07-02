package com.example.demo.model;

import com.example.demo.constant.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name="users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String passwordHash;
    private String passwordSalt;

    // @ManyToOne
    private String role;

    public User(String name, String username, String email, Roles role){
        this.name=name;
        this.username=username;
        this.email=email;
        this.role=role.toString();
    }

    public User() {}

    // public Roles getRole(){
    //     return role;
    // }

}
