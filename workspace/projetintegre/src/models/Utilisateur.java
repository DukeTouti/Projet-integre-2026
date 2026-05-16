package models;

import java.util.ArrayList;
import java.util.List;

import models.enums.Role;

public abstract class Utilisateur {
	protected int id;
	protected String nom;
	protected String prenom;
	protected String email;
	protected String motDePasse;
	protected Role role;
	protected boolean actif;
	protected List<Demande> demandes;
	protected List<Reclamation> reclamations;

	public Utilisateur(String nom, String prenom, String email, String motDePasse, Role role) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.motDePasse = motDePasse;
		this.role = role;
		this.actif = true;
		this.demandes = new ArrayList<Demande>();
		this.reclamations = new ArrayList<Reclamation>();
	}

	/* ===== GETTERS & SETTERS ===== */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	/* ===== METHODES METIER ===== */

	public String getNomComplet() {
		return this.prenom + " " + this.nom;
	}

	public boolean verifierMotDePasse(String mdp) {
		return this.motDePasse.equals(mdp);
	}

	public List<Demande> getMesDemandes() {
		return this.demandes;
	}

	public List<Reclamation> getMesReclamations() {
		return this.reclamations;
	}

	public void ajouterDemande(Demande demande) {
		this.demandes.add(demande);
	}

	public void ajouterReclamation(Reclamation reclamation) {
		this.reclamations.add(reclamation);
	}

	public abstract String[] getAcces();

}
