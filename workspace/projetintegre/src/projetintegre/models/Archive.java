package projetintegre.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projetintegre.models.enums.StatutDossier;

public class Archive {
	private int id;
	private List<Dossier> dossiers;
	private Date dateCreation;

	public Archive() {
		this.dossiers = new ArrayList<Dossier>();
		this.dateCreation = new Date();
	}

	/* ===== GETTERS & SETTERS ===== */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/* ===== METHODES METIER ===== */

	public void ajouterDossier(Dossier dossier) {
		dossier.archiver();
		this.dossiers.add(dossier);
	}

	public void retirerDossier(Dossier dossier) {
		this.dossiers.remove(dossier);
	}

	public List<Demande> getDemandes() {
		List<Demande> dm = new ArrayList<Demande>();

		for (Dossier d : this.dossiers) {
			if (d instanceof Demande) {
				dm.add((Demande) d);
			}
		}

		return dm;
	}

	public List<Reclamation> getReclamations() {
		List<Reclamation> recl = new ArrayList<Reclamation>();

		for (Dossier d : this.dossiers) {
			if (d instanceof Reclamation) {
				recl.add((Reclamation) d);
			}
		}

		return recl;
	}

	public List<Dossier> rechercherParStatut(StatutDossier statut) {
		List<Dossier> resultat = new ArrayList<Dossier>();

		for (Dossier d : this.dossiers) {
			if (d.getStatutDossier() == statut) {
				resultat.add(d);
			}
		}

		return resultat;
	}

	public List<Dossier> rechercherParType(String type) {
		List<Dossier> resultat = new ArrayList<Dossier>();

		for (Dossier d : this.dossiers) {
			if (d.getType().equalsIgnoreCase(type)) {
				resultat.add(d);
			}
		}

		return resultat;
	}

	public List<Dossier> rechercherParDate(Date debut, Date fin) {
		List<Dossier> resultat = new ArrayList<Dossier>();

		for (Dossier d : this.dossiers) {
			Date date = d.getDateCreation();

			if (!date.before(debut) && !date.after(fin)) {
				resultat.add(d);
			}
		}

		return resultat;
	}

	public List<Dossier> rechercherParAuteur(Utilisateur auteur) {
		List<Dossier> resultat = new ArrayList<Dossier>();
		
		for (Dossier d : this.dossiers) {
			if (d.getAuteur().getId() == auteur.getId()) {
				resultat.add(d);
			}
		}
		
		return resultat;
	}
	
}
