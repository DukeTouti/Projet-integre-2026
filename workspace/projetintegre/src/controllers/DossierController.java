package controllers;

import BD.BD;
import models.Demande;
import models.Dossier;
import models.PSH;
import models.Reclamation;
import models.Utilisateur;
import models.enums.StatutDossier;
import models.enums.TypeDemande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DossierController — opérations sur la table parente `dossier`.
 * Contient aussi les lectures qui combinent dossier + demande/reclamation
 * nécessaires aux vues (dashboard, liste admin).
 */
public class DossierController {

    private DossierController() {}

    // ================================================================
    // INSERT
    // ================================================================

    public static int insertDossier(Dossier dossier) {
        Connection conn = BD.getConnection();
        if (conn == null) return -1;

        String sql =
                "INSERT INTO dossier (description, statut, typeDossier, archive, idAuteur, dateCreation, dateMaj) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dossier.getDescription());
            stmt.setString(2, dossier.getStatut());
            stmt.setString(3, dossier.getType());
            stmt.setBoolean(4, dossier.isArchive());
            stmt.setInt(5, dossier.getAuteur().getId());
            stmt.setTimestamp(6, new Timestamp(dossier.getDateCreation().getTime()));
            stmt.setTimestamp(7, new Timestamp(dossier.getDateMaj().getTime()));

            if (stmt.executeUpdate() > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        dossier.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[DOSSIER] insertDossier : " + e.getMessage());
        }
        return -1;
    }

    // ================================================================
    // LECTURE
    // ================================================================

    // Toutes les demandes actives — AdminDossiersListPanel
    public static List<Demande> getAllDemandes() {
        List<Demande> liste = new ArrayList<>();
        Connection conn = BD.getConnection();
        if (conn == null) return liste;

        String sql =
                "SELECT d.idDossier, d.description, d.dateCreation, d.dateMaj, " +
                        "d.statut, d.archive, d.dateArchivage, d.idAuteur, " +
                        "dm.typeDemande, dm.commentaire " +
                        "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier " +
                        "WHERE d.archive = FALSE ORDER BY d.dateCreation DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) liste.add(hydrateDemande(rs, rs.getInt("idAuteur")));
        } catch (SQLException e) {
            System.err.println("[DOSSIER] getAllDemandes : " + e.getMessage());
        }
        return liste;
    }

    // N derniers dossiers d'un auteur (demandes + réclamations mélangés) — StudentDashboardPanel
    public static List<Dossier> getDerniersParAuteur(int idAuteur, int limite) {
        List<Dossier> liste = new ArrayList<>();
        Connection conn = BD.getConnection();
        if (conn == null) return liste;

        String sqlDm =
                "SELECT d.idDossier, d.description, d.dateCreation, d.dateMaj, " +
                        "d.statut, d.archive, d.dateArchivage, " +
                        "dm.typeDemande, dm.commentaire " +
                        "FROM dossier d JOIN demande dm ON d.idDossier = dm.idDossier " +
                        "WHERE d.idAuteur = ? ORDER BY d.dateMaj DESC LIMIT ?";

        String sqlRcl =
                "SELECT d.idDossier, d.description, d.dateCreation, d.dateMaj, " +
                        "d.statut, d.archive, d.dateArchivage, " +
                        "r.motif, r.reponse, r.idDemande " +
                        "FROM dossier d JOIN reclamation r ON d.idDossier = r.idDossier " +
                        "WHERE d.idAuteur = ? ORDER BY d.dateMaj DESC LIMIT ?";

        try (PreparedStatement s1 = conn.prepareStatement(sqlDm)) {
            s1.setInt(1, idAuteur);
            s1.setInt(2, limite);
            try (ResultSet rs = s1.executeQuery()) {
                while (rs.next()) liste.add(hydrateDemande(rs, idAuteur));
            }
        } catch (SQLException e) {
            System.err.println("[DOSSIER] getDerniersParAuteur (demandes) : " + e.getMessage());
        }

        try (PreparedStatement s2 = conn.prepareStatement(sqlRcl)) {
            s2.setInt(1, idAuteur);
            s2.setInt(2, limite);
            try (ResultSet rs = s2.executeQuery()) {
                while (rs.next()) liste.add(hydrateReclamation(rs, idAuteur));
            }
        } catch (SQLException e) {
            System.err.println("[DOSSIER] getDerniersParAuteur (reclamations) : " + e.getMessage());
        }

        // Trier par dateMaj desc et garder les `limite` premiers
        liste.sort((a, b) -> b.getDateMaj().compareTo(a.getDateMaj()));
        if (liste.size() > limite) liste = liste.subList(0, limite);

        return liste;
    }

    // ================================================================
    // MISE À JOUR
    // ================================================================

    public static boolean majStatut(Dossier dossier, StatutDossier statut) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        dossier.majStatut(statut);

        String sql = "UPDATE dossier SET statut = ?, dateMaj = ? WHERE idDossier = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dossier.getStatut());
            stmt.setTimestamp(2, new Timestamp(dossier.getDateMaj().getTime()));
            stmt.setInt(3, dossier.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DOSSIER] majStatut : " + e.getMessage());
        }
        return false;
    }

    public static boolean archiverDossier(Dossier dossier) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        dossier.archiver();

        String sql =
                "UPDATE dossier SET archive = TRUE, statut = 'CLOTUREE', dateArchivage = ?, dateMaj = ? " +
                        "WHERE idDossier = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(dossier.getDateArchivage().getTime()));
            stmt.setTimestamp(2, new Timestamp(dossier.getDateMaj().getTime()));
            stmt.setInt(3, dossier.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DOSSIER] archiverDossier : " + e.getMessage());
        }
        return false;
    }

    public static boolean supprimerDossier(int idDossier) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM dossier WHERE idDossier = ?")) {
            stmt.setInt(1, idDossier);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DOSSIER] supprimerDossier : " + e.getMessage());
        }
        return false;
    }

    // ================================================================
    // SÉCURITÉ
    // ================================================================

    public static boolean appartientA(int idDossier, int idUtilisateur) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT idAuteur FROM dossier WHERE idDossier = ?")) {
            stmt.setInt(1, idDossier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("idAuteur") == idUtilisateur;
            }
        } catch (SQLException e) {
            System.err.println("[DOSSIER] appartientA : " + e.getMessage());
        }
        return false;
    }

    // ================================================================
    // COMPTEURS — StudentDashboardPanel
    // ================================================================

    public static int compterParStatutEtAuteur(int idAuteur, StatutDossier statut) {
        Connection conn = BD.getConnection();
        if (conn == null) return 0;

        String sql = "SELECT COUNT(*) FROM dossier WHERE idAuteur = ? AND statut = ? AND archive = FALSE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAuteur);
            stmt.setString(2, statut.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[DOSSIER] compterParStatutEtAuteur : " + e.getMessage());
        }
        return 0;
    }

    // ================================================================
    // HYDRATATION — private
    // ================================================================

    private static Demande hydrateDemande(ResultSet rs, int idAuteur) throws SQLException {
        Utilisateur proxy = buildProxy(idAuteur);
        Demande d = new Demande(
                rs.getString("description"),
                proxy,
                TypeDemande.valueOf(rs.getString("typeDemande"))
        );
        d.setId(rs.getInt("idDossier"));
        d.setStatut(StatutDossier.valueOf(rs.getString("statut")));
        d.setCommentaire(rs.getString("commentaire"));
        return d;
    }

    private static Reclamation hydrateReclamation(ResultSet rs, int idAuteur) throws SQLException {
        Utilisateur proxy = buildProxy(idAuteur);
        Reclamation r = new Reclamation(
                rs.getString("description"),
                proxy,
                rs.getString("motif")
        );
        r.setId(rs.getInt("idDossier"));
        r.setStatut(StatutDossier.valueOf(rs.getString("statut")));
        r.setReponse(rs.getString("reponse"));
        return r;
    }

    private static Utilisateur buildProxy(int id) {
        PSH proxy = new PSH("", "", "", "", "", "") {
            @Override public String[] getAcces() { return new String[0]; }
        };
        proxy.setId(id);
        return proxy;
    }
}