package com.example.tahakkum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "oauth_apps")
public class OAuthApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "client_secret")
    public String clientSecret;

    @Column(name = "client_id")
    public String clientId;

    @Column
    public String name;

    @Column(nullable = true)
    public String homepage=null;

    @Column(nullable = true)
    public String photo=null;

    @Column(nullable = true)
    public String description=null;
    
    @Column(nullable = false)
    public String[] scopes;

    @Column(name = "redirect_url", nullable = false)
    public String redirectUrl;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    public User owner;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;
    // reletions
    @OneToMany(mappedBy = "app")
    public List<OAuthToken> tokens = new ArrayList<>();
}
