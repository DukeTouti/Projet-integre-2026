package models;

import java.util.Date;

import models.enums.Role;

public class PSH extends Utilisateur {
	private String typeHandicap;
	private String numeroEtudiant;
	private Date dateInscription;

	public PSH(String nom, String prenom, String email, String motDePasse, String typeHandicap, String numeroEtudiant) {
		super(nom, prenom, email, motDePasse, Role.PSH);
		// TODO Auto-generated constructor stub
		this.typeHandicap = typeHandicap;
		this.numeroEtudiant = numeroEtudiant;
		this.dateInscription = new Date();
	}

	/* ===== GETTERS & SETTERS ===== */

	public String getTypeHandicap() {
		return typeHandicap;
	}

	public void setTypeHandicap(String typeHandicap) {
		this.typeHandicap = typeHandicap;
	}

	public String getNumeroEtudiant() {
		return numeroEtudiant;
	}

	public void setNumeroEtudiant(String numeroEtudiant) {
		this.numeroEtudiant = numeroEtudiant;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	/* ===== METHODES METIER ===== */

	@Override
	public String[] getAcces() {
		// TODO Auto-generated method stub
		return new String[] { "SOUMETTRE_DEMANDE", "CONSULTER_DEMANDE", "MODIFIER_DEMANDE", "SUPPRIMER_DEMANDE",
				"SOUMETTRE_RECLAMATION", "CONSULTER_RECLAMATION", "MODIFIER_RECLAMATION", "SUIVRE_RECLAMATION",
				"LISTER_RECLAMATIONS", "LISTER_HISTORIQUE_DEMANDES", "LISTER_HISTORIQUE_RECLAMATIONS",
				"RECHERCHER_DEMANDE", "RECHERCHER_RECLAMATION" };
	}

}
