package com.example.tahakkum.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tahakkum.constant.Constants;
import com.example.tahakkum.constant.TokenStatuses;
import com.example.tahakkum.constant.TokenTypes;
import com.example.tahakkum.model.OAuthApp;
import com.example.tahakkum.model.OAuthToken;
import com.example.tahakkum.model.Token;
import com.example.tahakkum.model.User;
import com.example.tahakkum.repository.OAuthTokenRepository;
import com.example.tahakkum.repository.TokenRepository;
import com.example.tahakkum.repository.UserRepository;
import com.example.tahakkum.utility.Cryptation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OAuthTokenRepository oauthTokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create and save token
     * 
     * @param user
     * @param type
     * @param initialStatus
     * @param expireSec
     * @return
     */
    public Token createToken(User user, TokenTypes type, TokenStatuses initialStatus, Long expireSec) {
        Token token = new Token();
        token.setValue(Cryptation.randomString(Constants.TOKEN_LENGTH));
        token.setUser(user);
        token.setType(type.toString());
        token.setStatus(initialStatus.toString());
        token.setExpiredAt(LocalDateTime.now().plusSeconds(expireSec));
        tokenRepository.save(token);
        userRepository.save(user);
        return token;
    }

    public OAuthToken createOauthToken(OAuthApp app, User user, String clientId) {
        OAuthToken token = new OAuthToken();
        token.setValue(Cryptation.randomString(Constants.TOKEN_LENGTH));
        token.setApp(app);
        token.setExpiredAt(LocalDateTime.now().plusHours(Constants.OAUTH_ACCESS_TOKEN_TTL_HOUR));
        token.setClientId(clientId);
        token.setUser(user);
        app.getTokens().add(token);
        oauthTokenRepository.save(token);
        userRepository.save(user);
        // entityManager.persist(token);
        return token;
    }

    public Optional<Token> findTokenByValue(String value) {
        Token token = tokenRepository.findOneByValue(value);
        if (token == null) {
            return Optional.empty();
        }

        if (token.isExpired(tokenRepository)) {
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public Token save(Token token){
        return tokenRepository.save(token);
    }
}
