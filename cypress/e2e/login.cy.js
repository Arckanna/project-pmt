describe("Test de connexion", () => {
  it("doit permettre à un utilisateur de se connecter", () => {
    cy.visit("http://localhost:4200/login");

    cy.get('input[name="email"]').type("valerie@example.com");
    cy.get('input[name="password"]').type("poi");
    cy.get('button[type="submit"]').click();

    cy.contains("Bienvenue").should("exist");
  });
  it("doit afficher une erreur si l'utilisateur se trompe d'identifiants", () => {
    cy.visit("http://localhost:4200/login");

    cy.get('input[name="email"]').type("email-invalide@example.com");
    cy.get('input[name="password"]').type("mauvaismotdepasse");
    cy.get('button[type="submit"]').click();

    cy.contains("Identifiants incorrects").should("exist");
  });
});
