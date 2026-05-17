package controllers;

import BD.BD;
import models.Demande;
import models.Reclamation;
import models.Utilisateur;
import models.enums.StatutDossier;
import models.enums.TypeDemande;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReclamationController {

	private ReclamationController() {
	}

	/*
	 * =================================================== ComplaintsPanel —
	 * soumettre une réclamation =================================================
	 */

	public static int creerReclamation(Reclamation reclamation) {
		Connection conn = BD.getConnection();
		if (conn == null)
			return -1;

		String sqlDossier = "INSERT INTO dossier (description, statut, typeDossier, archive, idAuteur) "
				+ "VALUES (?, ?, 'RECLAMATION', FALSE, ?)";
		String sqlRecl = "INSERT INTO reclamation (idDossier, motif, reponse, idDemande) VALUES (?, ?, '', ?)";

		try {
			conn.setAutoCommit(false);

			int idDossier;
			try (PreparedStatement stmtD = conn.prepareStatement(sqlDossier, Statement.RETURN_GENERATED_KEYS)) {
				stmtD.setString(1, reclamation.getDescription());
				stmtD.setString(2, reclamation.getStatutDossier().name());
				stmtD.setInt(3, reclamation.getAuteur().getId());
				stmtD.executeUpdate();

				ResultSet keys = stmtD.getGeneratedKeys();
				if (!keys.next())
					throw new SQLException("Pas de clé générée.");
				idDossier = keys.getInt(1);
			}

			try (PreparedStatement stmtR = conn.prepareStatement(sqlRecl)) {
				stmtR.setInt(1, idDossier);
				stmtR.setString(2, reclamation.getMotif());
				if (reclamation.isLieeADemande()) {
					stmtR.setInt(3, reclamation.getDemande().getId());
				} else {
					stmtR.setNull(3, Types.INTEGER);
				}
				stmtR.executeUpdate();
			}

			conn.commit();
			reclamation.setId(idDossier);
			System.out.println("[ReclamationController] Réclamation créée #" + idDossier);
			return idDossier;

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				/* ignoré */ }
			System.err.println("[ReclamationController] creerReclamation : " + e.getMessage());
			return -1;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				/* ignoré */ }
		}
	}

	/*
	 * =================================================== MyDossiersPanel —
	 * réclamations du PSH =================================================
	 */

	public static List<Reclamation> getMesReclamations(int idAuteur) {
		List<Reclamation> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		String sql = "SELECT d.*, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier "
				+ "WHERE d.idAuteur = ? AND d.archive = FALSE ORDER BY d.dateCreation DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idAuteur);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ReclamationController] getMesReclamations : " + e.getMessage());
		}
		return liste;
	}

	/*
	 * =================================================== AdminComplaintsPanel —
	 * toutes les réclamations actives
	 * =================================================
	 */

	public static List<Reclamation> getToutesReclamations() {
		List<Reclamation> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		String sql = "SELECT d.*, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier "
				+ "WHERE d.archive = FALSE ORDER BY d.dateCreation DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ReclamationController] getToutesReclamations : " + e.getMessage());
		}
		return liste;
	}

	/*
	 * =================================================== AdminComplaintsPanel —
	 * rechercher par période (dateCreation)
	 * =================================================
	 */

	public static List<Reclamation> rechercherParPeriode(Date debut, Date fin) {
		List<Reclamation> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		String sql = "SELECT d.*, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier " + "WHERE d.archive = FALSE "
				+ "AND d.dateCreation BETWEEN ? AND ? " + "ORDER BY d.dateCreation DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setTimestamp(1, new Timestamp(debut.getTime()));
			stmt.setTimestamp(2, new Timestamp(fin.getTime()));
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ReclamationController] rechercherParPeriode : " + e.getMessage());
		}
		return liste;
	}

	/*
	 * =================================================== AdminComplaintsPanel —
	 * rechercher par statut =================================================
	 */

	public static List<Reclamation> rechercherParStatut(StatutDossier statut) {
		List<Reclamation> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		String sql = "SELECT d.*, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier "
				+ "WHERE d.archive = FALSE AND d.statut = ? " + "ORDER BY d.dateCreation DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, statut.name());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ReclamationController] rechercherParStatut : " + e.getMessage());
		}
		return liste;
	}

	/*
	 * =================================================== AdminComplaintsPanel —
	 * réclamations liées à une demande spécifique
	 * =================================================
	 */

	public static List<Reclamation> rechercherParDemande(int idDemande) {
		List<Reclamation> liste = new ArrayList<>();
		Connection conn = BD.getConnection();
		if (conn == null)
			return liste;

		String sql = "SELECT d.*, r.motif, r.reponse, r.idDemande "
				+ "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier " + "WHERE r.idDemande = ? "
				+ "ORDER BY d.dateCreation DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idDemande);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				liste.add(mapperReclamation(rs));
		} catch (SQLException e) {
			System.err.println("[ReclamationController] rechercherParDemande : " + e.getMessage());
		}
		return liste;
	}

	/*
	 * =================================================== AdminComplaintsPanel —
	 * Enregistrer Réponse =================================================
	 */

	public static boolean repondre(Reclamation reclamation, String reponse) {
		Connection conn = BD.getConnection();
		if (conn == null)
			return false;

		reclamation.setReponse(reponse);
		reclamation.majStatut(StatutDossier.CLOTUREE);

		try {
			conn.setAutoCommit(false);

			try (PreparedStatement stmt = conn
					.prepareStatement("UPDATE reclamation SET reponse = ? WHERE idDossier = ?")) {
				stmt.setString(1, reclamation.getReponse());
				stmt.setInt(2, reclamation.getId());
				stmt.executeUpdate();
			}

			try (PreparedStatement stmt = conn.prepareStatement(
					"UPDATE dossier SET statut = ?, dateMaj = CURRENT_TIMESTAMP WHERE idDossier = ?")) {
				stmt.setString(1, reclamation.getStatutDossier().name());
				stmt.setInt(2, reclamation.getId());
				stmt.executeUpdate();
			}

			conn.commit();
			System.out.println("[ReclamationController] Réclamation #" + reclamation.getId() + " traitée.");
			return true;

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				/* ignoré */ }
			System.err.println("[ReclamationController] repondre : " + e.getMessage());
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				/* ignoré */ }
		}
	}

	/*
	 * =================================================== ComplaintsPanel —
	 * modifier motif si EN_ATTENTE
	 * =================================================
	 */

	public static boolean modifierMotif(Reclamation reclamation, String nouveauMotif) {
		Connection conn = BD.getConnection();
		if (conn == null)
			return false;

		if (reclamation.getStatutDossier() != StatutDossier.EN_ATTENTE) {
			System.out.println(
					"[ReclamationController] Modification refusée — statut : " + reclamation.getStatutDossier().name());
			return false;
		}

		reclamation.setMotif(nouveauMotif);

		String sql = "UPDATE reclamation SET motif = ? WHERE idDossier = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, reclamation.getMotif());
			stmt.setInt(2, reclamation.getId());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("[ReclamationController] modifierMotif : " + e.getMessage());
			return false;
		}
	}

	/*
	 * =================================================== MAPPER
	 * =================================================
	 */

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

		return reclamation;
	}
}