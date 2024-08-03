package com.example.tahakkum.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.tahakkum.constant.TokenStatuses;
import com.example.tahakkum.repository.OAuthTokenRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "oauth_tokens")
public class OAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private OAuthApp app;

    @Column(unique = true, nullable = false)
    private String value;

    @Column(nullable = false)
    private String status = TokenStatuses.Active.toString();

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // TODO:indexlenmeli
    private String clientId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;

    public boolean isExpired(OAuthTokenRepository tokenRepository) {
        if (this.getExpiredAt().isBefore(LocalDateTime.now())) {
            if (tokenRepository != null && !this.getStatus().equals(TokenStatuses.Expired.toString())) {
                this.status = TokenStatuses.Expired.toString();
                tokenRepository.save(this);
            }
            return true;
        }
        return false;
    }
}
