package com.pmt.controllers;

import com.pmt.entities.User;
import com.pmt.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur exposant les points d'accés pour la gestion et l'authentification des utilisateurs.
 * Fournit des opérations pour créer des utilisateurs, les répertorier et effectuer une vérification de connexion de base.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println(">>> UserController instancié <<<");
    }

    /**
     * Point d'accés afin de verifier l'état de santé.
     *
     * @return: 200 avec la chaîne "pong" pour indiquer que le service est en cours d'exécution.
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param user: entité utilisateur contenant au moins une adresse e-mail et un mot de passe.
     * @return: 200 avec l'utilisateur créé.
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println(">>> POST reçu <<< " + user.getEmail());
        return userRepository.save(user);
    }
    /**
     * Récupére tous les utilisateurs enregistrés dans le système.
     *
     * @return 200 avec la liste complète des utilisateurs.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Requete l'identification d'un utilisateur avec les identifiants fournis.
     *
     * @param : mappage d'identifiants contenant les clés "email"  et "password" .
     * @return: 200 si les identifiants correspondent à un utilisateur, ou 401 si les identifiants sont incorrects.
     */
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
