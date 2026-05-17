package views.PSHjpanel;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StudentProfilePanel extends JPanel {

    public StudentProfilePanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("👤 Mon Profil PSH (Données Utilisateur)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel infoGrid = new JPanel(new GridLayout(7, 2, 15, 15));
        infoGrid.setOpaque(false);

        infoGrid.add(createBoldLabel("ID Système (id) :"));
        infoGrid.add(new JLabel("42"));

        infoGrid.add(createBoldLabel("Numéro Étudiant (numeroEtudiant) :"));
        infoGrid.add(new JLabel("UIR-2023-4589"));

        infoGrid.add(createBoldLabel("Nom & Prénom (nom, prenom) :"));
        infoGrid.add(new JLabel("Jidal Ilyas"));

        infoGrid.add(createBoldLabel("Adresse Email (email) :"));
        infoGrid.add(new JLabel("ilyas.jidal@uir.ac.ma"));

        infoGrid.add(createBoldLabel("Rôle Utilisateur (role) :"));
        infoGrid.add(new JLabel("PSH"));

        infoGrid.add(createBoldLabel("Type de Handicap (typeHandicap) :"));
        infoGrid.add(new JLabel("Moteur (Permanent)"));

        infoGrid.add(createBoldLabel("Date d'Inscription (dateInscription) :"));
        infoGrid.add(new JLabel("15/09/2023"));

        JPanel cardWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        cardWrapper.setOpaque(false);
        cardWrapper.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(0x374151)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardWrapper.add(infoGrid, BorderLayout.NORTH);

        add(cardWrapper, BorderLayout.CENTER);

        JLabel footerNote = new JLabel("🔒 Informations issues de la base de données UIR. Rôle d'accès vérifié via verifierMotDePass().");
        footerNote.setFont(new Font("SansSerif", Font.ITALIC, 11));
        add(footerNote, BorderLayout.SOUTH);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
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