describe("Formulaire d'inscription", () => {
  it("devrait permettre l'inscription avec des champs valides", () => {
    cy.visit("http://localhost:4200/register");

    cy.get('input[name="username"]').type("Valérie");
    cy.get('input[name="email"]').type("valerie.test@demo.com");
    cy.get('input[name="password"]').type("motdepassefort");
    cy.get('button[type="submit"]').should("not.be.disabled").click();

    cy.contains("✅ Utilisateur créé avec succès").should("exist");
  });

  it("ne devrait pas permettre l'inscription sans email", () => {
    cy.visit("http://localhost:4200/register");

    cy.get('input[name="username"]').type("Valérie");
    cy.get('input[name="password"]').type("motdepassefort");

    cy.get('button[type="submit"]').should("be.disabled");
  });

  it("ne devrait pas permettre l'inscription sans nom", () => {
    cy.visit("http://localhost:4200/register");

    cy.get('input[name="email"]').type("valerie@demo.com");
    cy.get('input[name="password"]').type("motdepassefort");

    cy.get('button[type="submit"]').should("be.disabled");
  });

  it("ne devrait pas permettre l'inscription sans mot de passe", () => {
    cy.visit("http://localhost:4200/register");

    cy.get('input[name="username"]').type("Valérie");
    cy.get('input[name="email"]').type("valerie@demo.com");

    cy.get('button[type="submit"]').should("be.disabled");
  });
});
