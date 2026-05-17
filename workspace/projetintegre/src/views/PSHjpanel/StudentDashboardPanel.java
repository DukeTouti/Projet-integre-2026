package projetintegre.views.PSHjpanel;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StudentDashboardPanel extends JPanel {

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("📊 Tableau de Bord Étudiant");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(createStatCard("Dossiers Acceptés", "1", new Color(0x10B981)));
        statsGrid.add(createStatCard("En Cours / Attente", "1", new Color(0xF59E0B)));
        statsGrid.add(createStatCard("Dossiers Refusés", "0", new Color(0xEF4444)));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        JPanel recentActivity = new JPanel(new BorderLayout(0, 10));
        recentActivity.setOpaque(false);

        JLabel sectionTitle = new JLabel("🔔 Activité Récente (Dernières mises à jour)");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        recentActivity.add(sectionTitle, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        listPanel.add(createDossierRow("DEMANDE", "Demande de tiers-temps pour le CF", "ACCEPTEE"));
        listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        listPanel.add(createDossierRow("RECLAMATION", "Contestation refus aménagement sur module Compilation", "EN_ATTENTE"));

        recentActivity.add(listPanel, BorderLayout.CENTER);
        centerPanel.add(recentActivity, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color badgeColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new RoundedBorder(15, new Color(0x374151)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(12, 15, 0, 15));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblValue.setForeground(badgeColor);
        lblValue.setBorder(BorderFactory.createEmptyBorder(0, 15, 12, 15));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JPanel createDossierRow(String type, String desc, String statut) {
        JPanel row = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setBorder(new RoundedBorder(12, new Color(0x374151)));

        JLabel lblInfo = new JLabel("<html><b>" + type + "</b> — " + desc + "</html>");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblStatut = new JLabel(statut + " ");
        lblStatut.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStatut.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        if (statut.equals("ACCEPTEE")) lblStatut.setForeground(new Color(0x10B981));
        else if (statut.equals("EN_ATTENTE")) lblStatut.setForeground(new Color(0xF59E0B));
        else lblStatut.setForeground(new Color(0xEF4444));

        row.add(lblInfo, BorderLayout.CENTER);
        row.add(lblStatut, BorderLayout.EAST);
        return row;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }
    }
}