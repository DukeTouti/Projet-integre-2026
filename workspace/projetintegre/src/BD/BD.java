package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BD {

    private static final String URL      = "jdbc:mysql://localhost:3306/projetintegre";
    private static final String USER     = "root";
    private static final String PASSWORD = "Root@1234";

    private static Connection conn = null;

    private BD() {}

    // =====CONNEXION =====
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion BD établie.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion BD : " + e.getMessage());
        }
        return conn;
    }

    public static void fermerConnexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connexion BD fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }

    public static boolean testConnexion() {
        return getConnection() != null;
    }
}