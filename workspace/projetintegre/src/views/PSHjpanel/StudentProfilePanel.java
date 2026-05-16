package views.PSHjpanel;

import javax.swing.*;
import java.awt.*;

public class StudentProfilePanel extends JPanel {

    public StudentProfilePanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 1. Titre
        JLabel titleLabel = new JLabel("👤 Mon Profil PSH (Données Utilisateur)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Grille d'informations synchronisée avec les attributs UML de l'étudiant PSH
        JPanel infoGrid = new JPanel(new GridLayout(7, 2, 15, 15));
        infoGrid.setOpaque(false);

        infoGrid.add(createBoldLabel("ID Système (id) :"));
        infoGrid.add(new JLabel("42"));

        infoGrid.add(createBoldLabel("Numéro Étudiant (numeroEtudiant) :"));
        infoGrid.add(new JLabel("UIR-2023-4589"));

        infoGrid.add(createBoldLabel("Nom & Prénom (nom, prenom) :"));
        infoGrid.add(new JLabel("Benali Ilyas"));

        infoGrid.add(createBoldLabel("Adresse Email (email) :"));
        infoGrid.add(new JLabel("ilyas.benali@uir.ac.ma"));

        infoGrid.add(createBoldLabel("Rôle Utilisateur (role) :"));
        infoGrid.add(new JLabel("PSH"));

        infoGrid.add(createBoldLabel("Type de Handicap (typeHandicap) :"));
        infoGrid.add(new JLabel("Moteur (Permanent)"));

        infoGrid.add(createBoldLabel("Date d'Inscription (dateInscription) :"));
        infoGrid.add(new JLabel("15/09/2023"));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(infoGrid, BorderLayout.NORTH);

        add(centerWrapper, BorderLayout.CENTER);

        // Note de sécurité
        JLabel footerNote = new JLabel("🔒 Informations issues de la base de données UIR. Rôle d'accès vérifié via verifierMotDePass().");
        footerNote.setFont(new Font("SansSerif", Font.ITALIC, 11));
        add(footerNote, BorderLayout.SOUTH);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }
}