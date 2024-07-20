package com.example.tahakkum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tahakkum.repository.UserRepository;
import com.example.tahakkum.utility.Cryptation;
import com.example.tahakkum.utility.Cryptation.*;
import com.example.tahakkum.constant.Roles;
import com.example.tahakkum.model.User;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

        public User create(String username, String email, String name, String password, Roles role){
        User u = new User(name, username, email, role);
        HashAndSalt hns = Cryptation.hashAndSaltPassword(password);
        u.setPasswordHash(hns.hash);
        u.setPasswordSalt(hns.salt);

        userRepository.save(u);
        return u;
    }

    // public User findone(HashMap<String> w){
    
    //     Example<User> where = Example.of()
    //     return new User();
    // }
}
