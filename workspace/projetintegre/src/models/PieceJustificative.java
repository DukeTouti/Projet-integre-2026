package models;

import java.util.Date;

public class PieceJustificative {
	private int id;
	private String nomFichier;
	private String cheminFichier;
	private Date dateAjout;
	private String typeFichier;

	public PieceJustificative(String nomFichier, String cheminFichier, String typeFichier) {
		this.nomFichier = nomFichier;
		this.cheminFichier = cheminFichier;
		this.typeFichier = typeFichier;
		this.dateAjout = new Date();
	}

	/* ===== GETTERS & SETTERS ===== */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	public String getCheminFichier() {
		return cheminFichier;
	}

	public void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	public String getTypeFichier() {
		return typeFichier;
	}

	public void setTypeFichier(String typeFichier) {
		this.typeFichier = typeFichier;
	}

	public Date getDateAjout() {
		return dateAjout;
	}

}
