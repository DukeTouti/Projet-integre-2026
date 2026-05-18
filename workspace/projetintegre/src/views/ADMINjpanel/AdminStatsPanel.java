package views.ADMINjpanel;

import controllers.StatistiqueController;
import models.Statistique;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminStatsPanel extends JPanel {

    private JLabel lblTauxAccept;
    private JLabel lblTauxRefus;
    private JLabel lblNbDemandes;
    private JLabel lblNbReclamations;

    public AdminStatsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("📈 Calculs et Métriques de la Commission (Classe Statistique)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainForm = new JPanel();
        mainForm.setLayout(new BoxLayout(mainForm, BoxLayout.Y_AXIS));
        mainForm.setOpaque(false);

        JPanel dateFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dateFilterPanel.setOpaque(false);
        dateFilterPanel.add(new JLabel("Date Début :"));

        JTextField txtDebut = new JTextField("01/01/2026", 10);
        txtDebut.setMaximumSize(new Dimension(120, 30));
        dateFilterPanel.add(txtDebut);

        dateFilterPanel.add(new JLabel("Date Fin :"));

        JTextField txtFin = new JTextField("31/12/2026", 10);
        txtFin.setMaximumSize(new Dimension(120, 30));
        dateFilterPanel.add(txtFin);

        JButton btnCalculer = new JButton("⚙️ Exécuter filtrerParPeriode()");
        dateFilterPanel.add(btnCalculer);

        // Labels dynamiques
        lblTauxAccept      = new JLabel("...");
        lblTauxRefus       = new JLabel("...");
        lblNbDemandes      = new JLabel("...");
        lblNbReclamations  = new JLabel("...");

        JPanel resultsPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        resultsPanel.setOpaque(false);
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        resultsPanel.add(new JLabel("Taux d'Acceptation Global (getTauxAcceptation) :"));
        resultsPanel.add(lblTauxAccept);

        resultsPanel.add(new JLabel("Taux de Refus Global (getTauxRefus) :"));
        resultsPanel.add(lblTauxRefus);

        resultsPanel.add(new JLabel("Nombre total de Demandes (getNbDemandes) :"));
        resultsPanel.add(lblNbDemandes);

        resultsPanel.add(new JLabel("Nombre total de Réclamations (getNbReclamations) :"));
        resultsPanel.add(lblNbReclamations);

        mainForm.add(dateFilterPanel);
        mainForm.add(Box.createRigidArea(new Dimension(0, 20)));
        mainForm.add(resultsPanel);

        add(mainForm, BorderLayout.CENTER);

        btnCalculer.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date debut = sdf.parse(txtDebut.getText().trim());
                // Fin = fin de journée
                Date fin = new Date(sdf.parse(txtFin.getText().trim()).getTime() + 86399999L);
                afficherStats(StatistiqueController.getStatistiques(debut, fin));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Format de date invalide. Utilisez JJ/MM/AAAA.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Chargement initial — toutes périodes confondues
        afficherStats(StatistiqueController.getStatistiquesGlobales());
    }

    private void afficherStats(Statistique stat) {
        lblTauxAccept.setText(String.format("%.1f %%", stat.getTauxAcceptation()));
        lblTauxRefus.setText(String.format("%.1f %%", stat.getTauxRefus()));
        lblNbDemandes.setText(stat.getNbDemandes() + " demandes");
        lblNbReclamations.setText(stat.getNbReclamations() + " réclamations");
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
