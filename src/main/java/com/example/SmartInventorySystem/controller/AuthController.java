package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.model.User;
import com.example.SmartInventorySystem.service.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userService.createUser(user);
        return "User has created";
    }
}
