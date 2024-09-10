package com.saracalihan.tahakkum.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saracalihan.tahakkum.service.AuthService;
import com.saracalihan.tahakkum.service.TokenService;
import com.saracalihan.tahakkum.service.UserService;
import com.saracalihan.tahakkum.constant.Roles;
import com.saracalihan.tahakkum.constant.TokenStatuses;
import com.saracalihan.tahakkum.constant.TokenTypes;
import com.saracalihan.tahakkum.dto.authentication.*;
import com.saracalihan.tahakkum.exception.ResponseException;
import com.saracalihan.tahakkum.model.Token;
import com.saracalihan.tahakkum.model.User;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController()
@RequestMapping("/authentication")
@Tag(name = "Basic Authentication", description = "`username`, `password` and `access-token` authentication")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EntityManager entityManager;

    @Operation(summary = "Login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login"),
        @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody(required = true) LoginDto loginDto) throws ResponseException {
        // TODO:store login success and fail dates
        Optional<LoginResponseDto> isLogin = authService.login(loginDto.username, loginDto.password);
        if (!isLogin.isPresent()) {
            throw new ResponseException("Email or password is incorrect!", HttpStatus.UNAUTHORIZED);
        }

        return isLogin.get();
    }

    @Operation(summary = "Register")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Password mismatch or invalid input"),
        @ApiResponse(responseCode = "401", description = "Email or username already exists")
    })

    @PostMapping("/register")
    @ResponseStatus(code=HttpStatus.CREATED)
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

    @Operation(summary = "Get my information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized or invalid access token")
    })

    @GetMapping("/me")
    public LoginResponseDto me(@RequestHeader(name = "x-access-token") String accessToken) throws ResponseException {
        if (accessToken == null) {
            throw new ResponseException("unauthoried!", HttpStatus.UNAUTHORIZED);
        }

        Optional<Token> token = authService.loginWithAccessToken(accessToken);

        if(token.isEmpty()){
            throw new ResponseException("invalid access token!",HttpStatus.UNAUTHORIZED);
        }
        Token tt = token.get();
        System.out.println(tt.getUser().getName());
        LoginResponseDto res= new LoginResponseDto(tt.getUser(), tt);
        return res;
    }

    @Operation(summary = "Logout")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged out successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized or invalid access token")
    })
    @GetMapping("/logout")
    public void logout(@RequestHeader(name = "x-access-token") String accessToken) throws ResponseException {
        if (accessToken == null) {
            throw new ResponseException("unauthoried!", HttpStatus.UNAUTHORIZED);
        }

        Optional<Token> token = tokenService.findTokenByValue(accessToken);

        if(token.isEmpty()){
            throw new ResponseException("invalid access token!",HttpStatus.UNAUTHORIZED);
        }
        Token tt = token.get();
        if(!tt.getType().equals(TokenTypes.AccessToken.toString()) || !tt.getStatus().equals(TokenStatuses.Active.toString())){
            throw new ResponseException("invalid access token!",HttpStatus.UNAUTHORIZED);
        }

        tt.setStatus(TokenStatuses.Cancelled.toString());
        tokenService.save(tt);
    }
}
