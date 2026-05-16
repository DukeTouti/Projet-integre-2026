package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    public AdminDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("📊 Tableau de Bord - Administration Globale");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(createSummaryCard("Total Demandes Nues", "24", new Color(0x3B82F6)));
        statsGrid.add(createSummaryCard("Réclamations Actives", "5", new Color(0xEF4444)));
        statsGrid.add(createSummaryCard("Dossiers Archivés", "142", new Color(0x10B981)));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0x374151)), "Profil de session connecté"));

        JPanel profileGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        profileGrid.setOpaque(false);
        profileGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profileGrid.add(new JLabel("Matricule Admin :"));
        profileGrid.add(new JLabel("ADM-2026-XYZ"));
        profileGrid.add(new JLabel("Département d'Attache :"));
        profileGrid.add(new JLabel("Scolarité & Inclusion Architecture"));

        infoPanel.add(profileGrid, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createSummaryCard(String title, String count, Color labelColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x374151), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel lblC = new JLabel(count);
        lblC.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblC.setForeground(labelColor);
        card.add(lblT, BorderLayout.NORTH);
        card.add(lblC, BorderLayout.CENTER);
        return card;
    }
}