package controllers;

import BD.BD;
import models.Demande;
import models.Dossier;
import models.Reclamation;
import models.Statistique;
import models.Utilisateur;
import models.enums.StatutDossier;
import models.enums.TypeDemande;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * StatistiqueController — ne calcule rien lui-même.
 * Son rôle : charger les dossiers depuis la BD, construire un objet Statistique,
 * et laisser la classe modèle faire tous les calculs.
 * Branché sur AdminStatsPanel.
 */
public class StatistiqueController {

	private StatistiqueController() {
	}

	// ================================================================
	// POINT D'ENTRÉE PRINCIPAL
	// ================================================================

	/**
	 * Construit un objet Statistique à partir de TOUS les dossiers (actifs +
	 * archivés). AdminStatsPanel l'appelle au chargement et après chaque filtre de
	 * période.
	 *
	 * @param debut date de début (inclusive), null = pas de limite
	 * @param fin   date de fin (inclusive), null = pas de limite
	 */
	public static Statistique getStatistiques(Date debut, Date fin) {
		List<Dossier> tousLesDossiers = chargerTousDossiers();

		// On délègue le filtrage par période à la classe modèle
		Statistique stat = new Statistique(tousLesDossiers, debut, fin);

		if (debut != null && fin != null) {
			List<Dossier> filtres = stat.filtrerParPeriode(debut, fin);
			stat = new Statistique(filtres, debut, fin);
		}

		return stat;
	}

	/**
	 * Statistiques sur tous les dossiers sans filtre de période. Utilisé par
	 * AdminDashboardPanel pour les chiffres globaux.
	 */
	public static Statistique getStatistiquesGlobales() {
		List<Dossier> tousLesDossiers = chargerTousDossiers();
		return new Statistique(tousLesDossiers, null, null);
	}

	// ================================================================
	// CHARGEMENT BD
	// ================================================================

	/**
	 * Charge tous les dossiers (demandes + réclamations, archivés ou non). Résultat
	 * passé brut au constructeur de Statistique.
	 */
	private static List<Dossier> chargerTousDossiers() {
		List<Dossier> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		// Demandes
		String sqlDemandes = "SELECT d.idDossier, d.description, d.statut, d.archive, d.dateArchivage, "
				+ "d.dateCreation, d.dateMaj, d.idAuteur, dm.typeDemande, dm.commentaire "
				+ "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier";

		try (PreparedStatement stmt = conn.prepareStatement(sqlDemandes); ResultSet rs = stmt.executeQuery()) {
			while (rs.next())
				liste.add(mapperDemande(rs));
		} catch (SQLException e) {
			System.err.println("[StatistiqueController] chargerDemandes : " + e.getMessage());
		}

		// Réclamations
		String sqlReclamations = "SELECT d.idDossier, d.description, d.statut, d.archive, d.dateArchivage, "
				+ "d.dateCreation, d.dateMaj, d.idAuteur, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier";

		try (PreparedStatement stmt = conn.prepareStatement(sqlReclamations); ResultSet rs = stmt.executeQuery()) {
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[StatistiqueController] chargerReclamations : " + e.getMessage());
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
			demande.majStatut(StatutDossier.EN_ATTENTE);
		}

		if (rs.getBoolean("archive"))
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
			reclamation.setStatut(StatutDossier.EN_ATTENTE);
		}

		if (rs.getBoolean("archive"))
			reclamation.archiver();

		return reclamation;
	}
}