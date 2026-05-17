package models;

import java.util.Date;

import models.enums.StatutDossier;
import models.interfaces.IArchivable;
import models.interfaces.IStatistique;

public abstract class Dossier implements IArchivable, IStatistique {

	protected int id;
	protected String description;
	protected Date dateCreation;
	protected Date dateMaj;
	protected StatutDossier statut;
	protected Utilisateur auteur;
	protected boolean archive;
	protected Date dateArchivage;

	public Dossier(String description, Utilisateur auteur) {
		this.description = description;
		this.auteur = auteur;
		this.dateCreation = new Date();
		this.dateMaj = new Date();
		this.statut = StatutDossier.EN_ATTENTE;
		this.archive = false;
		this.dateArchivage = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateMaj() {
		return dateMaj;
	}

	public StatutDossier getStatutDossier() {
		return statut;
	}

	public void setStatut(StatutDossier statut) {
		this.statut = statut;
	}

	public Utilisateur getAuteur() {
		return auteur;
	}

	public void setAuteur(Utilisateur auteur) {
		this.auteur = auteur;
	}

	/* ===== METHODES METIER ===== */

	public void majStatut(StatutDossier statut) {
		this.statut = statut;
		this.dateMaj = new Date();
	}

	@Override
	public void archiver() {
		// TODO Auto-generated method stub
		this.archive = true;
		this.dateArchivage = new Date();
		majStatut(StatutDossier.CLOTUREE);
	}

	@Override
	public boolean isArchive() {
		// TODO Auto-generated method stub
		return this.archive;
	}

	@Override
	public Date getDateArchivage() {
		// TODO Auto-generated method stub
		return this.dateArchivage;
	}

	@Override
	public String getStatut() {
		// TODO Auto-generated method stub
		return this.statut.name();
	}

	@Override
	public Date getDateCreation() {
		// TODO Auto-generated method stub
		return this.dateCreation;
	}

	public abstract String getType();

}
