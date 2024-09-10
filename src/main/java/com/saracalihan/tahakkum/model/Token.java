package com.saracalihan.tahakkum.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.saracalihan.tahakkum.constant.TokenStatuses;
import com.saracalihan.tahakkum.constant.TokenTypes;
import com.saracalihan.tahakkum.repository.TokenRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="tokens")
public class Token {
    @Id
    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String type = TokenTypes.AccessToken.toString();

    private String status = TokenStatuses.Pending.toString();

    @Column(name="expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt = null;

    public boolean isExpired(TokenRepository tokenRepository){
        if(this.getExpiredAt().isBefore(LocalDateTime.now())){
            if(tokenRepository != null && !this.getStatus().equals(TokenStatuses.Expired.toString())){
                this.status = TokenStatuses.Expired.toString();
                tokenRepository.save(this);
            }
            return true;
        }
        return false;
    }
}
