package controllers;

import BD.BD;
import models.Demande;
import models.PieceJustificative;
import models.Utilisateur;
import models.enums.StatutDossier;
import models.enums.TypeDemande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeController {

    private DemandeController() {}

    /* ===================================================
     *  NewDossierPanel — soumettre une nouvelle demande
     * ================================================= */

    public static int creerDemande(Demande demande) {
        Connection conn = BD.getConnection();
        if (conn == null) return -1;

        String sqlDossier = "INSERT INTO dossier (description, statut, typeDossier, archive, idAuteur) " +
                            "VALUES (?, ?, 'DEMANDE', FALSE, ?)";
        String sqlDemande = "INSERT INTO demande (idDossier, typeDemande, commentaire) VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            int idDossier;
            try (PreparedStatement stmtD = conn.prepareStatement(sqlDossier, Statement.RETURN_GENERATED_KEYS)) {
                stmtD.setString(1, demande.getDescription());
                stmtD.setString(2, demande.getStatutDossier().name());
                stmtD.setInt(3, demande.getAuteur().getId());
                stmtD.executeUpdate();

                ResultSet keys = stmtD.getGeneratedKeys();
                if (!keys.next()) throw new SQLException("Pas de clé générée.");
                idDossier = keys.getInt(1);
            }

            try (PreparedStatement stmtDm = conn.prepareStatement(sqlDemande)) {
                stmtDm.setInt(1, idDossier);
                stmtDm.setString(2, demande.getTypeDemande().name());
                stmtDm.setString(3, demande.getCommentaire());
                stmtDm.executeUpdate();
            }

            conn.commit();
            demande.setId(idDossier);
            System.out.println("[DemandeController] Demande créée #" + idDossier);
            return idDossier;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { /* ignoré */ }
            System.err.println("[DemandeController] creerDemande : " + e.getMessage());
            return -1;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { /* ignoré */ }
        }
    }

    /* ===================================================
     *  MyDossiersPanel — demandes du PSH connecté
     * ================================================= */

    public static List<Demande> getMesDemandes(int idAuteur) {
        List<Demande> liste = new ArrayList<>();
        Connection conn = BD.getConnection();
        if (conn == null) return liste;

        String sql = "SELECT d.*, dm.typeDemande, dm.commentaire " +
                     "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier " +
                     "WHERE d.idAuteur = ? AND d.archive = FALSE ORDER BY d.dateCreation DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAuteur);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(mapperDemande(rs));
        } catch (SQLException e) {
            System.err.println("[DemandeController] getMesDemandes : " + e.getMessage());
        }
        return liste;
    }

    /* ===================================================
     *  AdminDossiersListPanel — toutes les demandes actives
     * ================================================= */

    public static List<Demande> getToutesDemandes() {
        List<Demande> liste = new ArrayList<>();
        Connection conn = BD.getConnection();
        if (conn == null) return liste;

        String sql = "SELECT d.*, dm.typeDemande, dm.commentaire " +
                     "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier " +
                     "WHERE d.archive = FALSE ORDER BY d.dateCreation DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) liste.add(mapperDemande(rs));
        } catch (SQLException e) {
            System.err.println("[DemandeController] getToutesDemandes : " + e.getMessage());
        }
        return liste;
    }

    /* ===================================================
     *  AdminDossiersListPanel — Accepter / Refuser
     * ================================================= */

    public static boolean majStatut(Demande demande, StatutDossier statut) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        demande.majStatut(statut);

        String sql = "UPDATE dossier SET statut = ?, dateMaj = CURRENT_TIMESTAMP WHERE idDossier = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, demande.getStatutDossier().name());
            stmt.setInt(2, demande.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DemandeController] majStatut : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  AdminDossiersListPanel — Ajouter Commentaire
     * ================================================= */

    public static boolean ajouterCommentaire(Demande demande, String commentaire) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        demande.setCommentaire(commentaire);

        String sql = "UPDATE demande SET commentaire = ? WHERE idDossier = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, demande.getCommentaire());
            stmt.setInt(2, demande.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DemandeController] ajouterCommentaire : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  NewDossierPanel — upload pièce justificative
     * ================================================= */

    public static int ajouterPiece(Demande demande, PieceJustificative piece) {
        Connection conn = BD.getConnection();
        if (conn == null) return -1;

        String sql = "INSERT INTO pieceJustificative (nomFichier, cheminFichier, typeFichier, idDemande) " +
                     "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, piece.getNomFichier());
            stmt.setString(2, piece.getCheminFichier());
            stmt.setString(3, piece.getTypeFichier());
            stmt.setInt(4, demande.getId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                piece.setId(id);
                demande.ajouterPiece(piece);
                return id;
            }
        } catch (SQLException e) {
            System.err.println("[DemandeController] ajouterPiece : " + e.getMessage());
        }
        return -1;
    }

    /* ===================================================
     *  MyDossiersPanel — charger les pièces d'une demande
     * ================================================= */

    public static void chargerPieces(Demande demande) {
        Connection conn = BD.getConnection();
        if (conn == null) return;

        String sql = "SELECT * FROM pieceJustificative WHERE idDemande = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, demande.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PieceJustificative p = new PieceJustificative(
                    rs.getString("nomFichier"),
                    rs.getString("cheminFichier"),
                    rs.getString("typeFichier")
                );
                p.setId(rs.getInt("idPiece"));
                demande.ajouterPiece(p);
            }
        } catch (SQLException e) {
            System.err.println("[DemandeController] chargerPieces : " + e.getMessage());
        }
    }

    /* ===================================================
     *  MyDossiersPanel — supprimer une pièce
     * ================================================= */

    public static boolean supprimerPiece(Demande demande, PieceJustificative piece) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        String sql = "DELETE FROM pieceJustificative WHERE idPiece = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, piece.getId());
            boolean ok = stmt.executeUpdate() > 0;
            if (ok) demande.supprimerPiece(piece);
            return ok;
        } catch (SQLException e) {
            System.err.println("[DemandeController] supprimerPiece : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  MyDossiersPanel — supprimer une demande EN_ATTENTE
     * ================================================= */

    public static boolean supprimerDemande(Demande demande) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        if (demande.getStatutDossier() != StatutDossier.EN_ATTENTE) {
            System.out.println("[DemandeController] Suppression refusée — statut : "
                    + demande.getStatutDossier().name());
            return false;
        }

        String sql = "DELETE FROM dossier WHERE idDossier = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, demande.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DemandeController] supprimerDemande : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  MAPPER
     * ================================================= */

    private static Demande mapperDemande(ResultSet rs) throws SQLException {
        Utilisateur auteur = new Utilisateur("", "", "", "", models.enums.Role.PSH) {
            @Override public String[] getAcces() { return new String[0]; }
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

        if (rs.getBoolean("archive")) demande.archiver();

        return demande;
    }
}