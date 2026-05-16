package projetintegre.models;

import projetintegre.models.enums.Role;

public class Administrateur extends Utilisateur {
	private String matricule;
	private String departement;

	public Administrateur(String nom, String prenom, String email, String motDePasse, String matricule) {
		super(nom, prenom, email, motDePasse, Role.ADMINISTRATEUR);
		// TODO Auto-generated constructor stub
		this.matricule = matricule;
		this.departement = "";
	}

	/* ===== GETTERS & SETTERS ===== */

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getDepartement() {
		return departement;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	/* ===== METHODES METIER ===== */

	@Override
	public String[] getAcces() {
		// TODO Auto-generated method stub
		return new String[] { "VALIDER_COMPTE", "DESACTIVER_COMPTE", "SUPPRIMER_COMPTE", "LISTER_COMPTES",
				"CONSULTER_DEMANDE", "MAJ_STATUT_DEMANDE", "CLASSIFIER_DEMANDE", "CONSULTER_RECLAMATION",
				"TRAITER_RECLAMATION", "MAJ_RECLAMATION", "LISTER_RECLAMATIONS", "LISTER_STATISTIQUES",
				"FILTRER_STATISTIQUES", "LISTER_HISTORIQUE_DEMANDES", "LISTER_HISTORIQUE_RECLAMATIONS",
				"RECHERCHER_DEMANDE", "RECHERCHER_RECLAMATION", "SUPPRIMER_DEMANDE_ARCHIVE",
				"SUPPRIMER_RECLAMATION_ARCHIVE" };
	}

}
