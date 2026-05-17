package controllers;

import BD.BD;
import models.Demande;
import models.Dossier;
import models.Reclamation;
import models.Utilisateur;
import models.enums.StatutDossier;
import models.enums.TypeDemande;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * ArchiveController — lecture des dossiers archivés (archive = TRUE).
 * Branché sur AdminArchivesPanel.
 * Délègue la logique de filtrage en mémoire aux méthodes de la classe Archive.
 */
public class ArchiveController {

	private ArchiveController() {
	}

	// ================================================================
	// CHARGEMENT COMPLET DES ARCHIVES
	// ================================================================

	/**
	 * Charge tous les dossiers archivés (demandes + réclamations).
	 * AdminArchivesPanel l'appelle au chargement du panel.
	 */
	public static List<Dossier> getTousArchives() {
		List<Dossier> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		// Demandes archivées
		String sqlDemandes = "SELECT d.idDossier, d.description, d.statut, d.archive, d.dateArchivage, "
				+ "d.dateCreation, d.dateMaj, d.idAuteur, dm.typeDemande, dm.commentaire "
				+ "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier "
				+ "WHERE d.archive = TRUE ORDER BY d.dateArchivage DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sqlDemandes); ResultSet rs = stmt.executeQuery()) {
			while (rs.next())
				liste.add(mapperDemande(rs));
		} catch (SQLException e) {
			System.err.println("[ArchiveController] getTousArchives (demandes) : " + e.getMessage());
		}

		// Réclamations archivées
		String sqlReclamations = "SELECT d.idDossier, d.description, d.statut, d.archive, d.dateArchivage, "
				+ "d.dateCreation, d.dateMaj, d.idAuteur, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier "
				+ "WHERE d.archive = TRUE ORDER BY d.dateArchivage DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sqlReclamations); ResultSet rs = stmt.executeQuery()) {
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ArchiveController] getTousArchives (reclamations) : " + e.getMessage());
		}

		return liste;
	}

	// ================================================================
	// RECHERCHE MULTICRITÈRE — AdminArchivesPanel comboBox
	// ================================================================

	/**
	 * Recherche dans les archives par statut. Correspond au choix
	 * "rechercherParStatut()" dans le comboBox.
	 */
	public static List<Dossier> rechercherParStatut(StatutDossier statut) {
		List<Dossier> tous = getTousArchives();
		List<Dossier> resultat = new ArrayList<>();
		for (Dossier d : tous) {
			if (d.getStatutDossier() == statut)
				resultat.add(d);
		}
		return resultat;
	}

	/**
	 * Recherche dans les archives par type ("DEMANDE" ou "RECLAMATION"). Correspond
	 * au choix "rechercherParType()" dans le comboBox.
	 */
	public static List<Dossier> rechercherParType(String type) {
		List<Dossier> tous = getTousArchives();
		List<Dossier> resultat = new ArrayList<>();
		for (Dossier d : tous) {
			if (d.getType().equalsIgnoreCase(type))
				resultat.add(d);
		}
		return resultat;
	}

	/**
	 * Recherche dans les archives par id auteur. Correspond au choix
	 * "rechercherParAuteur()" dans le comboBox.
	 */
	public static List<Dossier> rechercherParAuteur(int idAuteur) {
		List<Dossier> tous = getTousArchives();
		List<Dossier> resultat = new ArrayList<>();
		for (Dossier d : tous) {
			if (d.getAuteur() != null && d.getAuteur().getId() == idAuteur)
				resultat.add(d);
		}
		return resultat;
	}

	/**
	 * Recherche dans les archives par période (dateArchivage).
	 */
	public static List<Dossier> rechercherParPeriode(Date debut, Date fin) {
		Connection conn = BD.getConnection();
		List<Dossier> liste = new ArrayList<>();
		if (conn == null)
			return liste;

		String sqlDemandes = "SELECT d.idDossier, d.description, d.statut, d.archive, d.dateArchivage, "
				+ "d.dateCreation, d.dateMaj, d.idAuteur, dm.typeDemande, dm.commentaire "
				+ "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier "
				+ "WHERE d.archive = TRUE AND d.dateArchivage BETWEEN ? AND ? ORDER BY d.dateArchivage DESC";

		String sqlReclamations = "SELECT d.idDossier, d.description, d.statut, d.archive, d.dateArchivage, "
				+ "d.dateCreation, d.dateMaj, d.idAuteur, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier "
				+ "WHERE d.archive = TRUE AND d.dateArchivage BETWEEN ? AND ? ORDER BY d.dateArchivage DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sqlDemandes)) {
			stmt.setTimestamp(1, new Timestamp(debut.getTime()));
			stmt.setTimestamp(2, new Timestamp(fin.getTime()));
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				liste.add(mapperDemande(rs));
		} catch (SQLException e) {
			System.err.println("[ArchiveController] rechercherParPeriode (demandes) : " + e.getMessage());
		}

		try (PreparedStatement stmt = conn.prepareStatement(sqlReclamations)) {
			stmt.setTimestamp(1, new Timestamp(debut.getTime()));
			stmt.setTimestamp(2, new Timestamp(fin.getTime()));
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ArchiveController] rechercherParPeriode (reclamations) : " + e.getMessage());
		}

		return liste;
	}

	// ================================================================
	// MAPPERS
	// ================================================================

	private static Demande mapperDemande(ResultSet rs) throws SQLException {
		Utilisateur auteur = new Utilisateur("", "", "", "", models.enums.Role.PSH) {
			@Override
			public String[] getAcces() {
				return new String[0];
			}
		};
		auteur.setId(rs.getInt("idAuteur"));

		TypeDemande type;
		try {
			type = TypeDemande.valueOf(rs.getString("typeDemande"));
		} catch (IllegalArgumentException e) {
			type = TypeDemande.AUTRE;
		}

		Demande demande = new Demande(rs.getString("description"), auteur, type);
		demande.setId(rs.getInt("idDossier"));
		demande.setCommentaire(rs.getString("commentaire") != null ? rs.getString("commentaire") : "");

		try {
			demande.majStatut(StatutDossier.valueOf(rs.getString("statut")));
		} catch (IllegalArgumentException e) {
			demande.majStatut(StatutDossier.CLOTUREE);
		}

		// Le dossier est archivé — on appelle archiver() du modèle
		demande.archiver();

		return demande;
	}

	private static Reclamation mapperReclamation(ResultSet rs) throws SQLException {
		Utilisateur auteur = new Utilisateur("", "", "", "", models.enums.Role.PSH) {
			@Override
			public String[] getAcces() {
				return new String[0];
			}
		};
		auteur.setId(rs.getInt("idAuteur"));

		int idDemandeLiee = rs.getInt("idDemande");
		Demande demandeLiee = null;
		if (!rs.wasNull()) {
			demandeLiee = new Demande("", auteur, TypeDemande.AUTRE);
			demandeLiee.setId(idDemandeLiee);
		}

		Reclamation reclamation;
		if (demandeLiee != null) {
			reclamation = new Reclamation(rs.getString("description"), auteur, demandeLiee, rs.getString("motif"));
		} else {
			reclamation = new Reclamation(rs.getString("description"), auteur, rs.getString("motif"));
		}

		reclamation.setId(rs.getInt("idDossier"));
		reclamation.setReponse(rs.getString("reponse") != null ? rs.getString("reponse") : "");

		try {
			reclamation.setStatut(StatutDossier.valueOf(rs.getString("statut")));
		} catch (IllegalArgumentException e) {
			reclamation.setStatut(StatutDossier.CLOTUREE);
		}

		reclamation.archiver();

		return reclamation;
	}
}