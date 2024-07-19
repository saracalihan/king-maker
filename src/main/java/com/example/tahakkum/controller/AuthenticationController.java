package com.example.tahakkum.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tahakkum.service.AuthService;
import com.example.tahakkum.service.UserService;
import com.example.tahakkum.constant.Roles;
import com.example.tahakkum.dto.authentication.*;
import com.example.tahakkum.exception.ResponseException;
import com.example.tahakkum.model.Token;
import com.example.tahakkum.model.User;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/authentication")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EntityManager entityManager;

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody(required = true) LoginDto loginDto) throws ResponseException {
        // TODO:store login success and fail dates
        Optional<LoginResponseDto> isLogin = authService.login(loginDto.username, loginDto.password);
        if (!isLogin.isPresent()) {
            throw new ResponseException("Email or password is incorrect!", HttpStatus.UNAUTHORIZED);
        }

        return isLogin.get();
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody(required = true) RegisterDto registerDto) throws ResponseException {

        if (!registerDto.password.equals(registerDto.passwordAgain)) {
            throw new ResponseException("Passwords must be equal!");
        }

        if (entityManager.createNativeQuery("SELECT *  FROM users where email='" + registerDto.username
                + "' or username='" + registerDto.username + "'").getResultList().size() > 0) {
            throw new ResponseException("Email or username already exist!", HttpStatus.UNAUTHORIZED);
        }

        Optional<Roles> role = Optional.of(Roles.User);
        if (registerDto.role != null) {
            role = Roles.fromString(registerDto.role);
            if (!role.isPresent()) {
                throw new ResponseException("Wrong role: '" + registerDto.role + "'", HttpStatus.UNAUTHORIZED);
            }
        }

        User user = userService.create(registerDto.username, registerDto.email, registerDto.name, registerDto.password,
                role.get());

        return user;
    }

    @GetMapping("/me")
    public LoginResponseDto me(@RequestHeader(name = "x-access-token") String accessToken) throws ResponseException {
        if (accessToken == null) {
            throw new ResponseException("unauthoried!", HttpStatus.UNAUTHORIZED);
        }

        Optional<Token> token = authService.loginWithAccessToken(accessToken);

        if(token.isEmpty()){
            throw new ResponseException("unouthoried!",HttpStatus.UNAUTHORIZED);
        }
        Token tt = token.get();
        LoginResponseDto res= new LoginResponseDto(tt.getUser(), tt);
        return res;
    }
}
