package views.ADMINjpanel;

import controllers.ReclamationController;
import models.Reclamation;
import models.enums.StatutDossier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminComplaintsPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<Reclamation> reclamations;

    private static final String[] COLUMNS = {"ID Réc", "ID Dossier Cible", "Description Contestation", "Statut"};

    public AdminComplaintsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("💬 Traitement des Réclamations Reçues");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(0x374151));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x374151)));
        add(scroll, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton btnActualiser = new JButton("🔃 Actualiser");
        JButton btnRepondre   = new JButton("✍️ Enregistrer Réponse Administrative");

        actionPanel.add(btnActualiser);
        actionPanel.add(btnRepondre);
        add(actionPanel, BorderLayout.SOUTH);

        btnActualiser.addActionListener(e -> chargerDonnees());

        btnRepondre.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une réclamation dans la liste.",
                        "Sélection requise", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Reclamation r = reclamations.get(row);
            if (r.getStatutDossier() == StatutDossier.CLOTUREE) {
                JOptionPane.showMessageDialog(this, "Cette réclamation est déjà clôturée.",
                        "Impossible", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String reponse = JOptionPane.showInputDialog(this,
                    "Saisissez la réponse administrative :", "Réponse", JOptionPane.PLAIN_MESSAGE);
            if (reponse != null && !reponse.trim().isEmpty()) {
                boolean ok = ReclamationController.repondre(r, reponse.trim());
                if (ok) {
                    tableModel.setValueAt(StatutDossier.CLOTUREE.name(), row, 3);
                    JOptionPane.showMessageDialog(this, "Réponse enregistrée, réclamation clôturée.",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        chargerDonnees();
    }

    private void chargerDonnees() {
        reclamations = ReclamationController.getToutesReclamations();
        tableModel.setRowCount(0);
        for (Reclamation r : reclamations) {
            String idDossierCible = r.isLieeADemande() ? String.valueOf(r.getDemande().getId()) : "-";
            tableModel.addRow(new Object[]{
                r.getId(),
                idDossierCible,
                r.getMotif(),
                r.getStatutDossier().name()
            });
        }
    }
}
