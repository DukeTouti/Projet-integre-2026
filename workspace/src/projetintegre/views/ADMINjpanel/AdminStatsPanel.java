package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import java.awt.*;

public class AdminStatsPanel extends JPanel {

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

        JPanel resultsPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        resultsPanel.setOpaque(false);
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        resultsPanel.add(new JLabel("Taux d'Acceptation Global (getTauxAcceptation) :"));
        resultsPanel.add(new JLabel("78.5 %"));

        resultsPanel.add(new JLabel("Taux de Refus Global (getTauxRefus) :"));
        resultsPanel.add(new JLabel("21.5 %"));

        resultsPanel.add(new JLabel("Nombre total de Demandes (getNbDemandes) :"));
        resultsPanel.add(new JLabel("156 demandes"));

        resultsPanel.add(new JLabel("Nombre total de Réclamations (getNbReclamations) :"));
        resultsPanel.add(new JLabel("12 réclamations"));

        mainForm.add(dateFilterPanel);
        mainForm.add(Box.createRigidArea(new Dimension(0, 20)));
        mainForm.add(resultsPanel);

        add(mainForm, BorderLayout.CENTER);
    }
}