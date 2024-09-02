package com.example.tahakkum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.tahakkum.constant.Constants;
import com.example.tahakkum.constant.Roles;
import com.example.tahakkum.constant.TokenStatuses;
import com.example.tahakkum.constant.TokenTypes;
import com.example.tahakkum.utility.Cryptation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "users")
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

    @JsonIgnore
    private String passwordHash;
    @JsonIgnore
    private String passwordSalt;

    private String role = Roles.User.toString();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt = null;

    // relitions
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Token> tokens = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<OAuthToken> oauthTokens = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    OAuthApp oauthApp;

    @JsonIgnore
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    OTPApp otpApp;

    public User(String name, String username, String email, Roles role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role.toString();
    }

    public User() {
    }

    public Token createAccessToken() {
        Token token = new Token();
        token.setValue(Cryptation.randomString(Constants.TOKEN_LENGTH));
        token.setType(TokenTypes.AccessToken.toString());
        token.setStatus(TokenStatuses.Active.toString());
        token.setExpiredAt(LocalDateTime.now().plusHours(Constants.ACCESS_TOKEN_TTL_HOUR));

        cancelAuthTokens();

        token.setUser(this);
        this.tokens.add(token);
        return token;
    }

    public void cancelAuthTokens() {
        this.tokens.forEach(token -> {
            if (token.getType().equals(TokenTypes.AccessToken.toString())
                    && (token.getStatus().equals(TokenStatuses.Active.toString())
                            || token.getStatus().equals(TokenStatuses.Pending.toString()))) {
                token.setStatus(TokenStatuses.Cancelled.toString());
            }
        });
    }

    public Map<String, Object> getFields(String[] scope) {
        Map<String, Object> res = new HashMap<>();

        for (String s : scope) {
            // Constants.OAUTH_ACCESSABLE_USER_FIELDS
            switch (s) {
                case "name":
                    res.put(s, this.getName());
                    break;
                case "username":
                    res.put(s, this.getUsername());
                    break;
                case "id":
                    res.put(s, this.getId());
                    break;
                case "email":
                    res.put(s, this.getEmail());
                    break;
                default:
                    break;
            }
        }
        return res;
    }
}
