package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminDashboardPanel extends JPanel {

    public AdminDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("📊 Tableau de Bord - Administration Globale");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);

        // Grille des statistiques (3 colonnes)
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(createSummaryCard("Total Demandes Nues", "24", new Color(0x3B82F6)));
        statsGrid.add(createSummaryCard("Réclamations Actives", "5", new Color(0xEF4444)));
        statsGrid.add(createSummaryCard("Dossiers Archivés", "142", new Color(0x10B981)));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Zone Profil Session customisée avec angles arrondis (remplace le TitledBorder natif)
        JPanel infoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(0x374151)); // Bordure grise foncée
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 16, 16));
                g2.dispose();
            }
        };
        infoPanel.setOpaque(false);
        infoPanel.setBackground(new Color(0x1F2937)); // Fond légèrement plus clair pour détacher le profil
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblSectionTitle = new JLabel("👤 Profil de session connecté");
        lblSectionTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSectionTitle.setForeground(Color.WHITE);
        infoPanel.add(lblSectionTitle, BorderLayout.NORTH);

        JPanel profileGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        profileGrid.setOpaque(false);
        profileGrid.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        profileGrid.add(createLabel("Matricule Admin :", true));
        profileGrid.add(createLabel("ADM-2026-XYZ", false));
        profileGrid.add(createLabel("Département d'Attache :", true));
        profileGrid.add(createLabel("Scolarité & Inclusion Architecture", false));

        infoPanel.add(profileGrid, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text, boolean isBold) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", isBold ? Font.BOLD : Font.PLAIN, 13));
        l.setForeground(Color.WHITE);
        return l;
    }

    private JPanel createSummaryCard(String title, String count, Color labelColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(0x374151));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(new Color(0x111827)); // Assorti au noir profond
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblT.setForeground(new Color(0x9CA3AF)); // Gris clair text muted

        JLabel lblC = new JLabel(count);
        lblC.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblC.setForeground(labelColor);

        card.add(lblT, BorderLayout.NORTH);
        card.add(lblC, BorderLayout.CENTER);
        return card;
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