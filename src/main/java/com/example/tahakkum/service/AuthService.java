package com.example.tahakkum.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.tahakkum.model.User;
import com.example.tahakkum.constant.TokenStatuses;
import com.example.tahakkum.constant.TokenTypes;
import com.example.tahakkum.dto.authentication.LoginResponseDto;
import com.example.tahakkum.model.Token;
import com.example.tahakkum.repository.TokenRepository;
import com.example.tahakkum.repository.UserRepository;
import com.example.tahakkum.utility.Cryptation;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

@Service()
public class AuthService {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public AuthService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<User> checkLogin(String username, String password) {
        User user = (User) entityManager
                .createNativeQuery("SELECT *  FROM users where email='" + username + "' or username='" + username + "'",
                        User.class)
                .getResultList().get(0);

        if (user == null) {
            return Optional.empty();
        }

        if (!Cryptation.comparePassword(password, user.getPasswordHash(), user.getPasswordSalt())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    public Optional<LoginResponseDto> login(String username, String password) {
        Optional<User> user = checkLogin(username, password);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        Token accessToken = user.get().createAccessToken();
        userRepository.save(user.get());
        tokenRepository.save(accessToken);
        return Optional.of(new LoginResponseDto(user.get(), accessToken));
    }

    public Optional<Token> loginWithAccessToken(String t){
        if(t==null){
            return Optional.empty();
        }

        Token token = tokenRepository.findOneByValue(t);
        if(token==null){
            return Optional.empty();
        }

        if(token.getExpiredAt().isBefore(LocalDateTime.now())){
            if(!token.getStatus().equals(TokenStatuses.Expired.toString())){
                token.status = TokenStatuses.Expired.toString();
                tokenRepository.save(token);
            }
            return Optional.empty();
        }
        if(!token.getType().equals(TokenTypes.AccessToken.toString())){
            return Optional.empty();
        }

        if(!token.getStatus().equals(TokenStatuses.Active.toString())){
            return Optional.empty();
        }

        return Optional.of(token);
    }
}
