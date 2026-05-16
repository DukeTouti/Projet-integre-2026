package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.enums.StatutDossier;

public class Statistique {
	private List<Dossier> dossiers;
	private Date dateDebut;
	private Date dateFin;

	public Statistique(List<Dossier> dossiers, Date dateDebut, Date dateFin) {
		this.dossiers = dossiers;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}

	/* ===== GETTERS & SETTERS ===== */

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public List<Dossier> getDossiers() {
		return dossiers;
	}

	/* ===== METHODES METIER ===== */

	public int getNbParStatut(StatutDossier statut) {
		int cmpt = 0;

		for (Dossier d : this.dossiers) {
			if (d.getStatutDossier() == statut) {
				cmpt++;
			}
		}

		return cmpt;
	}

	public int getNbParType(String type) {
		int cmpt = 0;

		for (Dossier d : this.dossiers) {
			if (d.getType().equalsIgnoreCase(type)) {
				cmpt++;
			}
		}

		return cmpt;
	}

	public int getNbTotal() {
		return this.dossiers.size();
	}

	public int getNbDemandes() {
		int cmpt = 0;

		for (Dossier d : this.dossiers) {
			if (d instanceof Demande) {
				cmpt++;
			}
		}

		return cmpt;
	}

	public int getNbReclamations() {
		int cmpt = 0;

		for (Dossier d : this.dossiers) {
			if (d instanceof Reclamation) {
				cmpt++;
			}
		}

		return cmpt;
	}

	public double getTauxAcceptation() {
		int nbDm = getNbDemandes();

		if (nbDm == 0) {
			return 0.0;
		}

		return (double) getNbParStatut(StatutDossier.ACCEPTEE) / nbDm * 100;
	}

	public double getTauxRefus() {
		int nbDm = getNbDemandes();

		if (nbDm == 0) {
			return 0.0;
		}

		return (double) getNbParStatut(StatutDossier.REFUSEE) / nbDm * 100;
	}

	public List<Dossier> filtrerParPeriode(Date debut, Date fin) {
		List<Dossier> resultat = new ArrayList<Dossier>();

		for (Dossier d : this.dossiers) {
			Date date = d.getDateCreation();

			if (!date.before(debut) && !date.after(fin)) {
				resultat.add(d);
			}
		}

		return resultat;
	}

	public List<Dossier> filtrerParType(String type) {
		List<Dossier> resultat = new ArrayList<Dossier>();

		for (Dossier d : this.dossiers) {
			if (d.getType().equalsIgnoreCase(type)) {
				resultat.add(d);
			}
		}

		return resultat;
	}
}
