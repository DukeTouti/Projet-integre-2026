package models;

public class Reclamation extends Dossier {

	private String motif;
	private Demande demande;
	private String reponse;

	/* ===== CONSTRUCTEURS ===== */

	public Reclamation(String description, Utilisateur auteur, Demande demande, String motif) {
		super(description, auteur);
		this.demande = demande;
		this.motif = motif;
		this.reponse = "";
	}

	public Reclamation(String description, Utilisateur auteur, String motif) {
		super(description, auteur);
		this.demande = null;
		this.motif = motif;
		this.reponse = "";
	}

	/* ===== GETTERS & SETTERS ===== */

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getReponse() {
		return reponse;
	}

	public void setReponse(String reponse) {
		this.reponse = reponse;
	}

	public Demande getDemande() {
		return demande;
	}

	public void setDemande(Demande demande) {
		this.demande = demande;
	}

	public boolean isLieeADemande() {
		return this.demande != null;
	}

	/* ===== METHODES METIER ===== */

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "RECLAMATION";
	}

}
