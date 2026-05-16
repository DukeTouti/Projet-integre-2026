package projetintegre.models;

public class Reclamation extends Dossier {
	private String motif;
	private Demande demande;
	private String reponse;

	public Reclamation(String description, Utilisateur auteur, Demande demande, String motif) {
		super(description, auteur);
		// TODO Auto-generated constructor stub
		this.demande = demande;
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

	/* ===== METHODES METIER ===== */

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "RECLAMATION";
	}

}
