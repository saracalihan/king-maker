package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;
import com.example.demo.utility.Cryption;
import com.example.demo.utility.Cryption.*;
import com.example.demo.constant.Roles;
import com.example.demo.model.User;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

        public User create(String username, String email, String name, String password, Roles role){
        User u = new User(name, username, email, role);

        HashAndSalt hns = Cryption.hashAndSaltPassword(password);
        u.setPasswordHash(hns.hash);
        u.setPasswordSalt(hns.salt);

        userRepository.save(u);
        return u;
    }

    public User find(){
        return new User();
    }
}
