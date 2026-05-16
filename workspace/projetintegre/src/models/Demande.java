package models;

import java.util.ArrayList;
import java.util.List;

import models.enums.TypeDemande;

public class Demande extends Dossier {
	private TypeDemande typeDemande;
	private List<PieceJustificative> pieces;
	private String commentaire;

	public Demande(String description, Utilisateur auteur, TypeDemande typeDemande) {
		super(description, auteur);
		// TODO Auto-generated constructor stub
		this.typeDemande = typeDemande;
		this.pieces = new ArrayList<PieceJustificative>();
		this.commentaire = "";
	}

	public TypeDemande getTypeDemande() {
		return typeDemande;
	}

	public void setTypeDemande(TypeDemande typeDemande) {
		this.typeDemande = typeDemande;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public List<PieceJustificative> getPieces() {
		return pieces;
	}

	@Override
	public String getType() {
		return this.typeDemande.name();
	}

	public void ajouterPiece(PieceJustificative piece) {
		this.pieces.add(piece);
	}

	public void supprimerPiece(PieceJustificative piece) {
		this.pieces.remove(piece);
	}

}
