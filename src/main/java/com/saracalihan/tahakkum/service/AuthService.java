package com.saracalihan.tahakkum.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.saracalihan.tahakkum.model.User;
import com.saracalihan.tahakkum.constant.TokenStatuses;
import com.saracalihan.tahakkum.constant.TokenTypes;
import com.saracalihan.tahakkum.dto.authentication.LoginResponseDto;
import com.saracalihan.tahakkum.model.Token;
import com.saracalihan.tahakkum.repository.TokenRepository;
import com.saracalihan.tahakkum.repository.UserRepository;
import com.saracalihan.tahakkum.utility.Cryptation;

import jakarta.persistence.EntityManager;

@Service()
public class AuthService {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    private EntityManager entityManager;

    public AuthService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<User> checkLogin(String username, String password) {
        List<User> res = entityManager
                .createNativeQuery("SELECT *  FROM users where email='" + username + "' or username='" + username + "'",
                        User.class)
                .getResultList();
        if(res.size()==0){
            return Optional.empty();
        }
        User user = res.get(0);
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

        Optional<Token> ot = tokenService.findTokenByValue(t);
        if(ot.isEmpty()){
            return Optional.empty();
        }
        Token token = ot.get();
        if(!token.getType().equals(TokenTypes.AccessToken.toString())){
            return Optional.empty();
        }

        if(!token.getStatus().equals(TokenStatuses.Active.toString())){
            return Optional.empty();
        }

        return Optional.of(token);
    }
}
