package com.saracalihan.tahakkum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    private Long id;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_id")
    private String clientId;

    @Column
    private String name;

    @Column(nullable = true)
    private String homepage=null;

    @Column(nullable = true)
    private String photo=null;

    @Column(nullable = true)
    private String description=null;
    
    @Column(nullable = false)
    private String[] scopes;

    @Column(name = "redirect_url", nullable = false)
    private String redirectUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;
    // reletions
    @OneToMany(mappedBy = "app")
    private List<OAuthToken> tokens = new ArrayList<>();
}
