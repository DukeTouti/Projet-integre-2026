package controllers;

import BD.BD;
import models.Administrateur;
import models.PSH;
import models.Utilisateur;
import models.enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthController {

    private static Utilisateur utilisateurConnecte = null;

    private AuthController() {}

    // ================================================================
    // CONNEXION
    // ================================================================

    public static Utilisateur connexion(String email, String motDePasse) {
        Connection conn = BD.getConnection();
        if (conn == null) return null;

        String sql = "SELECT idUtilisateur, nom, prenom, email, motDePasse, role " +
                "FROM utilisateur WHERE email = ? AND actif = TRUE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("[AUTH] Compte introuvable ou désactivé.");
                    return null;
                }

                String hashBD = rs.getString("motDePasse");
                if (!BCrypt.checkpw(motDePasse, hashBD)) {
                    System.out.println("[AUTH] Mot de passe incorrect.");
                    return null;
                }

                int    idUser = rs.getInt("idUtilisateur");
                String nom    = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String role   = rs.getString("role");

                if (role.equals(Role.PSH.name())) {
                    utilisateurConnecte = chargerPSH(conn, idUser, nom, prenom, email, hashBD);
                } else if (role.equals(Role.ADMINISTRATEUR.name())) {
                    utilisateurConnecte = chargerAdmin(conn, idUser, nom, prenom, email, hashBD);
                } else {
                    System.out.println("[AUTH] Rôle inconnu : " + role);
                    return null;
                }

                if (utilisateurConnecte != null) {
                    System.out.println("[AUTH] Connexion réussie : " + utilisateurConnecte.getNomComplet());
                }

                return utilisateurConnecte;
            }

        } catch (SQLException e) {
            System.err.println("[AUTH] Erreur SQL connexion : " + e.getMessage());
            return null;
        }
    }

    // ================================================================
    // INSCRIPTION — crée un compte PSH en attente de validation admin
    // ================================================================

    // Retourne le PSH créé si ok, null si l'email existe déjà ou erreur BD.
    public static PSH inscription(String nom, String prenom, String email, String motDePasse,
                                  String numeroEtudiant, String typeHandicap) {

        Connection conn = BD.getConnection();
        if (conn == null) return null;

        // Vérifier que l'email n'est pas déjà pris
        if (emailExiste(conn, email)) {
            System.out.println("[AUTH] Email déjà utilisé : " + email);
            return null;
        }

        // Hash du mot de passe
        String hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());

        try {
            conn.setAutoCommit(false); // transaction — les deux inserts sont atomiques

            // 1. INSERT dans utilisateur
            String sqlUser = "INSERT INTO utilisateur (nom, prenom, email, motDePasse, role, actif) " +
                    "VALUES (?, ?, ?, ?, ?, FALSE)"; // actif=FALSE : attend validation admin

            int idGenere;
            try (PreparedStatement stmt = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                stmt.setString(3, email);
                stmt.setString(4, hash);
                stmt.setString(5, Role.PSH.name());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return null;
                    }
                    idGenere = keys.getInt(1);
                }
            }

            // 2. INSERT dans psh
            String sqlPsh = "INSERT INTO psh (idUtilisateur, typeHandicap, numeroEtudiant) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sqlPsh)) {
                stmt.setInt(1, idGenere);
                stmt.setString(2, typeHandicap);
                stmt.setString(3, numeroEtudiant);
                stmt.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);

            // Construire et retourner l'objet PSH (non connecté — il doit attendre validation)
            PSH psh = new PSH(nom, prenom, email, hash, typeHandicap, numeroEtudiant);
            psh.setId(idGenere);
            psh.setActif(false);

            System.out.println("[AUTH] Inscription réussie pour : " + email);
            return psh;

        } catch (SQLException e) {
            System.err.println("[AUTH] Erreur inscription : " + e.getMessage());
            try { conn.rollback(); conn.setAutoCommit(true); } catch (SQLException ignored) {}
            return null;
        }
    }

    // ================================================================
    // DÉCONNEXION
    // ================================================================

    public static void deconnexion() {
        if (utilisateurConnecte != null) {
            System.out.println("[AUTH] Déconnexion : " + utilisateurConnecte.getNomComplet());
        }
        utilisateurConnecte = null;
    }

    // ================================================================
    // GETTERS SESSION
    // ================================================================

    public static Utilisateur getUtilisateurConnecte() { return utilisateurConnecte; }

    public static Role getRole() {
        if (utilisateurConnecte == null) return null;
        return utilisateurConnecte.getRole();
    }

    public static boolean estConnecte() { return utilisateurConnecte != null; }

    // ================================================================
    // PRIVÉ
    // ================================================================

    private static boolean emailExiste(Connection conn, String email) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM utilisateur WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[AUTH] emailExiste : " + e.getMessage());
            return false;
        }
    }

    private static PSH chargerPSH(Connection conn, int idUser, String nom, String prenom,
                                  String email, String motDePasse) throws SQLException {
        String sql = "SELECT typeHandicap, numeroEtudiant, dateInscription FROM psh WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("PSH introuvable pour id=" + idUser);
                PSH psh = new PSH(nom, prenom, email, motDePasse,
                        rs.getString("typeHandicap"), rs.getString("numeroEtudiant"));
                psh.setId(idUser);
                psh.setDateInscription(rs.getDate("dateInscription"));
                return psh;
            }
        }
    }

    private static Administrateur chargerAdmin(Connection conn, int idUser, String nom, String prenom,
                                               String email, String motDePasse) throws SQLException {
        String sql = "SELECT matricule, departement FROM administrateur WHERE idUtilisateur = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("Admin introuvable pour id=" + idUser);
                Administrateur admin = new Administrateur(nom, prenom, email, motDePasse,
                        rs.getString("matricule"));
                admin.setId(idUser);
                admin.setDepartement(rs.getString("departement"));
                return admin;
            }
        }
    }
}