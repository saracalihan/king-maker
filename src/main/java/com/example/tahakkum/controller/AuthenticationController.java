package com.example.tahakkum.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tahakkum.service.UserService;
import com.example.tahakkum.constant.Roles;
import com.example.tahakkum.dto.authentication.*;
import com.example.tahakkum.exception.ResponseException;
import com.example.tahakkum.model.User;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/authentication")
public class AuthenticationController {
    // private UserService userService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @PostMapping("/login")
    public String login(@Valid @RequestBody(required =true) LoginDto loginDto) throws ResponseException{
        throw new ResponseException("deneme");
       // return "user";
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody(required =true) RegisterDto registerDto) throws ResponseException{

        if(!registerDto.password.equals(registerDto.passwordAgain)){
            throw new ResponseException("Passwords must be equal!");
        }

        if(entityManager.createNativeQuery("SELECT *  FROM users where email='"+registerDto.username+"' or username='"+registerDto.username +"'").getResultList().size()>0){
            throw new ResponseException("Email or username already exist!");
        }

        Optional<Roles> role = Roles.fromString(registerDto.role);
        if(!role.isPresent()){
            throw new ResponseException("Wrong role: '" + registerDto.role + "'" );
        }

        User user = userService.create(registerDto.username, registerDto.email, registerDto.name, registerDto.password, role.get());

        return user;
    }

    @GetMapping("/me")
    public String me(){
        return new String("me");
    }
}
