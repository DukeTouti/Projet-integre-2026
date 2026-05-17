package views.PSHjpanel;

import controllers.AuthController;
import controllers.DossierController;
import models.Dossier;
import models.enums.StatutDossier;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;

public class StudentDashboardPanel extends JPanel {

    private JLabel lblAccepte;
    private JLabel lblEnAttente;
    private JLabel lblRefuse;
    private JPanel listPanel;

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("📊 Tableau de Bord Étudiant");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);

        // --- CARTES STATS ---
        lblAccepte  = new JLabel("...");
        lblEnAttente = new JLabel("...");
        lblRefuse   = new JLabel("...");

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(createStatCard("Dossiers Acceptés",   lblAccepte,   new Color(0x10B981)));
        statsGrid.add(createStatCard("En Cours / Attente",  lblEnAttente, new Color(0xF59E0B)));
        statsGrid.add(createStatCard("Dossiers Refusés",    lblRefuse,    new Color(0xEF4444)));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // --- ACTIVITÉ RÉCENTE ---
        JPanel recentActivity = new JPanel(new BorderLayout(0, 10));
        recentActivity.setOpaque(false);

        JLabel sectionTitle = new JLabel("🔔 Activité Récente (Dernières mises à jour)");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        recentActivity.add(sectionTitle, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        recentActivity.add(listPanel, BorderLayout.CENTER);

        centerPanel.add(recentActivity, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Charger les données réelles
        chargerDonnees();
    }

    // ================================================================
    // CHARGEMENT DEPUIS BD
    // ================================================================

    private void chargerDonnees() {
        int idAuteur = AuthController.getUtilisateurConnecte().getId();

        // 3 compteurs
        int acceptes  = DossierController.compterParStatutEtAuteur(idAuteur, StatutDossier.ACCEPTEE);
        int enAttente = DossierController.compterParStatutEtAuteur(idAuteur, StatutDossier.EN_ATTENTE)
                + DossierController.compterParStatutEtAuteur(idAuteur, StatutDossier.EN_COURS);
        int refuses   = DossierController.compterParStatutEtAuteur(idAuteur, StatutDossier.REFUSEE);

        lblAccepte.setText(String.valueOf(acceptes));
        lblEnAttente.setText(String.valueOf(enAttente));
        lblRefuse.setText(String.valueOf(refuses));

        // Activité récente — 5 derniers dossiers
        List<Dossier> dossiers = DossierController.getDerniersParAuteur(idAuteur, 5);

        listPanel.removeAll();
        if (dossiers.isEmpty()) {
            JLabel vide = new JLabel("Aucune activité récente.");
            vide.setFont(new Font("SansSerif", Font.ITALIC, 13));
            listPanel.add(vide);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Dossier d : dossiers) {
                listPanel.add(createDossierRow(
                        d.getType(),
                        d.getDescription(),
                        d.getStatut(),
                        sdf.format(d.getDateMaj())
                ));
                listPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ================================================================
    // COMPOSANTS UI
    // ================================================================

    private JPanel createStatCard(String title, JLabel lblValue, Color badgeColor) {
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

        lblValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblValue.setForeground(badgeColor);
        lblValue.setBorder(BorderFactory.createEmptyBorder(0, 15, 12, 15));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JPanel createDossierRow(String type, String desc, String statut, String dateMaj) {
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

        // Tronquer la description si trop longue
        String descAffichee = desc.length() > 60 ? desc.substring(0, 60) + "..." : desc;

        JLabel lblInfo = new JLabel("<html><b>" + type + "</b> — " + descAffichee + "</html>");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.setOpaque(false);

        JLabel lblStatut = new JLabel(statut, SwingConstants.RIGHT);
        lblStatut.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStatut.setBorder(BorderFactory.createEmptyBorder(8, 15, 2, 15));

        JLabel lblDate = new JLabel(dateMaj, SwingConstants.RIGHT);
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDate.setBorder(BorderFactory.createEmptyBorder(2, 15, 8, 15));

        switch (statut) {
            case "ACCEPTEE"  -> lblStatut.setForeground(new Color(0x10B981));
            case "EN_ATTENTE", "EN_COURS" -> lblStatut.setForeground(new Color(0xF59E0B));
            default          -> lblStatut.setForeground(new Color(0xEF4444));
        }

        rightPanel.add(lblStatut);
        rightPanel.add(lblDate);

        row.add(lblInfo, BorderLayout.CENTER);
        row.add(rightPanel, BorderLayout.EAST);
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