package controllers;

import BD.BD;
import models.Administrateur;
import models.PSH;
import models.Utilisateur;
import models.enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    //app desktop mono-utilisateur, une seule session à la fois
    private static Utilisateur utilisateurConnecte = null;

    private AuthController() {}

    // connexion — retourne l'objet utilisateur si ok, null sinon
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

                // vérification du mot de passe via BCrypt
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
            System.err.println("[AUTH] Erreur SQL : " + e.getMessage());
            return null;
        }
    }

    // chargement PSH — jointure vers la table psh
    private static PSH chargerPSH(Connection conn, int idUser, String nom, String prenom,
                                  String email, String motDePasse) throws SQLException {

        String sql = "SELECT typeHandicap, numeroEtudiant, dateInscription " +
                "FROM psh WHERE idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("PSH introuvable pour id=" + idUser);

                PSH psh = new PSH(nom, prenom, email, motDePasse,
                        rs.getString("typeHandicap"),
                        rs.getString("numeroEtudiant"));
                psh.setId(idUser);
                psh.setDateInscription(rs.getDate("dateInscription"));
                return psh;
            }
        }
    }

    // chargement Admin — jointure vers la table administrateur
    private static Administrateur chargerAdmin(Connection conn, int idUser, String nom, String prenom,
                                               String email, String motDePasse) throws SQLException {

        String sql = "SELECT matricule, departement FROM administrateur WHERE idUtilisateur = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("Administrateur introuvable pour id=" + idUser);

                Administrateur admin = new Administrateur(nom, prenom, email, motDePasse,
                        rs.getString("matricule"));
                admin.setId(idUser);
                admin.setDepartement(rs.getString("departement"));
                return admin;
            }
        }
    }

    // déconnexion
    public static void deconnexion() {
        if (utilisateurConnecte != null) {
            System.out.println("[AUTH] Déconnexion : " + utilisateurConnecte.getNomComplet());
        }
        utilisateurConnecte = null;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static Role getRole() {
        if (utilisateurConnecte == null) return null;
        return utilisateurConnecte.getRole();
    }

    public static boolean estConnecte() {
        return utilisateurConnecte != null;
    }
}