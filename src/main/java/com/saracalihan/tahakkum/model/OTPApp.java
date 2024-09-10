package com.saracalihan.tahakkum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

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
@Entity(name = "otp_apps")
public class OTPApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column
    private String redirectUrl;
  
    @Column
    private String failRedirectUrl;

    // code redirect fields
    @Column
    private String codeRedirectUrl;
    @Column
    private String codeRedirectMethod;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
    private Map<String, String> codeRedirectHeader = null;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
    private Map<String, String> codeRedirectBody = null;

    // // use directly codeRedirectUrl
    // @JdbcTypeCode(SqlTypes.JSON)
    // @Column(columnDefinition = "jsonb", nullable = true)
    // private Map<String, String> codeRedirectQueries = null;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;

    // relitions

    @OneToMany(mappedBy = "app", fetch = FetchType.LAZY)
    private List<OTPCode> codes = new ArrayList<>();
}
