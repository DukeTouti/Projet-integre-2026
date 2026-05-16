package projetintegre.views.PSHjpanel;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends JPanel {

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // En-tête
        JLabel welcomeLabel = new JLabel("📊 Tableau de Bord Étudiant");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        // Zone centrale : Stats + Activité récente
        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);

        // Grille des statistiques (3 colonnes calquées sur StatutDossier)
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(createStatCard("Dossiers Acceptés", "1", new Color(0x10B981)));
        statsGrid.add(createStatCard("En Cours / Attente", "1", new Color(0xF59E0B)));
        statsGrid.add(createStatCard("Dossiers Refusés", "0", new Color(0xEF4444)));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Liste des dossiers récents
        JPanel recentActivity = new JPanel(new BorderLayout(0, 10));
        recentActivity.setOpaque(false);

        JLabel sectionTitle = new JLabel("📁 Demandes et Réclamations Récentes");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        recentActivity.add(sectionTitle, BorderLayout.NORTH);

        JPanel listWrapper = new JPanel();
        listWrapper.setLayout(new BoxLayout(listWrapper, BoxLayout.Y_AXIS));
        listWrapper.setOpaque(false);

        // Simulation d'objets Demande et Réclamation réels
        listWrapper.add(createDossierRow("Dossier #1 · DEMANDE", "Tiers-temps (Examens)", "ACCEPTEE"));
        listWrapper.add(Box.createRigidArea(new Dimension(0, 8)));
        listWrapper.add(createDossierRow("Dossier #2 · RECLAMATION", "Contestation aménagement Compilation", "EN_ATTENTE"));

        JScrollPane scrollPane = new JScrollPane(listWrapper);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        recentActivity.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(recentActivity, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color badgeColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x374151), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblValue.setForeground(badgeColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JPanel createDossierRow(String type, String desc, String statut) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x374151), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblInfo = new JLabel("<html><b>" + type + "</b> — " + desc + "</html>");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel lblStatut = new JLabel(statut + " ");
        lblStatut.setFont(new Font("SansSerif", Font.BOLD, 12));

        if(statut.equals("ACCEPTEE")) lblStatut.setForeground(new Color(0x10B981));
        else lblStatut.setForeground(new Color(0xF59E0B));

        row.add(lblInfo, BorderLayout.WEST);
        row.add(lblStatut, BorderLayout.EAST);
        return row;
    }
}