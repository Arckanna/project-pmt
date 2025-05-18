package com.pmt.controllers;

import com.pmt.entities.User;
import com.pmt.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println(">>> UserController instancié <<<");
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println(">>> POST reçu <<< " + user.getEmail());
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst()
                .map(user -> ResponseEntity.ok().body("Connexion réussie"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects"));
    }
}
