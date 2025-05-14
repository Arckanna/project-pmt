package com.pmt.controllers;

import com.pmt.entities.User;
import com.pmt.repositories.UserRepository;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // utile si tu appelles depuis Angular plus tard
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println(">>> UserController instancié <<<");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println("Reçu depuis Postman : " + user);
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping("/ping")
    public String ping() {
        System.out.println(">>> Ping reçu <<<");
        return "pong";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println(">>> initBinder exécuté <<<");
    }
}

