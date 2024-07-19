package com.example.tahakkum.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.springframework.data.annotation.CreatedDate;

import com.example.tahakkum.constant.Roles;
import com.example.tahakkum.constant.TokenStatuses;
import com.example.tahakkum.constant.TokenTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    List<Token> tokens;

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
        token.value = LocalDateTime.now().toString();
        token.type = TokenTypes.AccessToken.toString();
        token.status = TokenStatuses.Active.toString();
        token.setExpiredAt(LocalDateTime.now().plusHours(72));

        cancelAuthTokens();

        token.setUser(this);
        this.tokens.add(token);
        return token;
    }

    public void cancelAuthTokens(){
        this.tokens.forEach(token -> {
            if(token.getType().equals(TokenTypes.AccessToken.toString()) && (token.status.equals(TokenStatuses.Active.toString()) || token.status.equals(TokenStatuses.Pending.toString()))){
                token.status = TokenStatuses.Cancelled.toString();
            }
        });
    }
}
