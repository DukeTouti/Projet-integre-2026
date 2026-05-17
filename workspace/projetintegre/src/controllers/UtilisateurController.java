package controllers;

import BD.BD;
import models.Administrateur;
import models.PSH;
import models.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurController {

    private UtilisateurController() {}

    /* ===================================================
     *  StudentProfilePanel — profil du PSH connecté
     * ================================================= */

    public static PSH getPSHParId(int id) {
        Connection conn = BD.getConnection();
        if (conn == null) return null;

        String sql = "SELECT u.*, p.typeHandicap, p.numeroEtudiant, p.dateInscription " +
                     "FROM utilisateur u JOIN psh p ON u.idUtilisateur = p.idUtilisateur " +
                     "WHERE u.idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapperPSH(rs);
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] getPSHParId : " + e.getMessage());
        }
        return null;
    }

    /* ===================================================
     *  AdminDashboardPanel — profil de l'admin connecté
     * ================================================= */

    public static Administrateur getAdminParId(int id) {
        Connection conn = BD.getConnection();
        if (conn == null) return null;

        String sql = "SELECT u.*, a.matricule, a.departement " +
                     "FROM utilisateur u JOIN administrateur a ON u.idUtilisateur = a.idUtilisateur " +
                     "WHERE u.idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapperAdministrateur(rs);
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] getAdminParId : " + e.getMessage());
        }
        return null;
    }

    /* ===================================================
     *  AdminDashboardPanel — liste tous les PSH
     * ================================================= */

    public static List<PSH> listerPSH() {
        List<PSH> liste = new ArrayList<>();
        Connection conn = BD.getConnection();
        if (conn == null) return liste;

        String sql = "SELECT u.*, p.typeHandicap, p.numeroEtudiant, p.dateInscription " +
                     "FROM utilisateur u JOIN psh p ON u.idUtilisateur = p.idUtilisateur";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) liste.add(mapperPSH(rs));
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] listerPSH : " + e.getMessage());
        }
        return liste;
    }

    /* ===================================================
     *  AdminDashboardPanel — PSH en attente de validation
     * ================================================= */

    public static List<PSH> listerPSHEnAttente() {
        List<PSH> liste = new ArrayList<>();
        Connection conn = BD.getConnection();
        if (conn == null) return liste;

        String sql = "SELECT u.*, p.typeHandicap, p.numeroEtudiant, p.dateInscription " +
                     "FROM utilisateur u JOIN psh p ON u.idUtilisateur = p.idUtilisateur " +
                     "WHERE u.actif = FALSE";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) liste.add(mapperPSH(rs));
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] listerPSHEnAttente : " + e.getMessage());
        }
        return liste;
    }

    /* ===================================================
     *  AdminDashboardPanel — activer un compte
     * ================================================= */

    public static boolean activerCompte(Utilisateur utilisateur) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        utilisateur.setActif(true);

        String sql = "UPDATE utilisateur SET actif = ? WHERE idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, utilisateur.isActif());
            stmt.setInt(2, utilisateur.getId());
            System.out.println("[UtilisateurController] Compte activé : " + utilisateur.getNomComplet());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] activerCompte : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  AdminDashboardPanel — désactiver un compte
     * ================================================= */

    public static boolean desactiverCompte(Utilisateur utilisateur) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        utilisateur.setActif(false);

        String sql = "UPDATE utilisateur SET actif = ? WHERE idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, utilisateur.isActif());
            stmt.setInt(2, utilisateur.getId());
            System.out.println("[UtilisateurController] Compte désactivé : " + utilisateur.getNomComplet());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] desactiverCompte : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  AdminDashboardPanel — supprimer un compte
     * ================================================= */

    public static boolean supprimerCompte(int idUtilisateur) {
        Connection conn = BD.getConnection();
        if (conn == null) return false;

        String sql = "DELETE FROM utilisateur WHERE idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            System.out.println("[UtilisateurController] Compte #" + idUtilisateur + " supprimé.");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UtilisateurController] supprimerCompte : " + e.getMessage());
            return false;
        }
    }

    /* ===================================================
     *  MAPPERS
     * ================================================= */

    private static PSH mapperPSH(ResultSet rs) throws SQLException {
        PSH psh = new PSH(
            rs.getString("nom"), rs.getString("prenom"),
            rs.getString("email"), rs.getString("motDePasse"),
            rs.getString("typeHandicap"), rs.getString("numeroEtudiant")
        );
        psh.setId(rs.getInt("idUtilisateur"));
        psh.setActif(rs.getBoolean("actif"));
        return psh;
    }

    private static Administrateur mapperAdministrateur(ResultSet rs) throws SQLException {
        Administrateur admin = new Administrateur(
            rs.getString("nom"), rs.getString("prenom"),
            rs.getString("email"), rs.getString("motDePasse"),
            rs.getString("matricule")
        );
        admin.setDepartement(rs.getString("departement"));
        admin.setId(rs.getInt("idUtilisateur"));
        admin.setActif(rs.getBoolean("actif"));
        return admin;
    }
}