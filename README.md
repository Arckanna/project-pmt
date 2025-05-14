# 📌 PMT Demo – Étude de cas Spring Boot

Projet d’étude de cas développé dans le cadre du bloc de compétences *Intégration, industrialisation et déploiement de logiciel* – Titre RNCP niveau 7 – IN'TECH / ESIEA.

---

## 🚀 Objectif

Développer une application de gestion de projet permettant :
- La création d’utilisateurs
- La gestion des tâches et projets (à venir)
- Le stockage des données dans une base H2 ou PostgreSQL

---

## 🛠️ Stack technique

- **Java 17**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **H2 Database** (par défaut)
- **PostgreSQL** (en option)
- **Postman** (pour les tests API)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) pour le développement

---

## 📂 Structure actuelle du projet

```
src/
├── main/
│   ├── java/com/pmt/
│   │   ├── BackendApplication.java
│   │   ├── controllers/UserController.java
│   │   ├── entities/User.java
│   │   └── repositories/UserRepository.java
│   └── resources/application.properties
```

## 🔄 Évolution prévue

---

Ce projet a pour ambition de devenir une **plateforme complète de gestion de projet collaboratif**. Les prochaines étapes incluent :

- ✅ Création de projets par les utilisateurs
- ✅ Ajout et attribution de tâches
- ✅ Suivi de l’historique des modifications
- ✅ Gestion des rôles (admin, membre, observateur)
- ✅ Intégration d’une base PostgreSQL pour la persistance
- 🔄 Interface frontend en Angular (à venir)
- 🔄 CI/CD et déploiement Dockerisé (à venir)

Chaque étape sera ajoutée avec des commits dédiés et documentée dans ce `README`.

---

## 🧪 Tester l’API actuelle

...

Importer la collection Postman fournie dans le dossier `postman/`  
Endpoints disponibles :

| Méthode | URL                             | Description           |
|---------|----------------------------------|------------------------|
| GET     | `/api/users/ping`               | Vérifie l’API         |
| POST    | `/api/users`                    | Crée un utilisateur   |
| GET     | `/api/users`                    | Liste les utilisateurs |

---

## 🧰 Configuration base de données

Par défaut :
```properties
spring.datasource.url=jdbc:h2:mem:testdb
```

Pour utiliser PostgreSQL :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pmt
spring.datasource.username=pmt_user
spring.datasource.password=password
```

---

## ✅ Lancer l’application

```bash
./mvnw spring-boot:run
```

---

## 🙋‍♀️ Développé par

Valérie Lecoeur – avril 2025
