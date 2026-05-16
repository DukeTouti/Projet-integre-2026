package views.PSHjpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyDossiersPanel extends JPanel {

    public MyDossiersPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 1. En-tête du Panel
        JLabel titleLabel = new JLabel("🗂️ Historique Global de mes Dossiers (Demandes & Réclamations)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Colonnes calquées sur la classe Dossier, Demande et StatutDossier
        String[] columnNames = {"ID Dossier", "Type de Dossier", "Description", "Date Création", "Date MAJ", "Statut"};

        // Simulation de données respectant l'énumération StatutDossier
        Object[][] data = {
                {1, "DEMANDE (AMENAGEMENT_EXAMEN)", "Demande de tiers-temps pour le CF", "12/05/2026", "15/05/2026", "ACCEPTEE"},
                {2, "RECLAMATION", "Contestation refus aménagement sur module Compilation", "14/05/2026", "14/05/2026", "EN_ATTENTE"},
                {3, "DEMANDE (ACCESSIBILITE)", "Relocalisation des cours de HPC au RDC", "10/09/2025", "12/09/2025", "CLOTUREE"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x374151), 1));

        add(scrollPane, BorderLayout.CENTER);

        // 3. Boutons d'action
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton btnVoirDetails = new JButton("👁️ Consulter le dossier");
        JButton btnPieces = new JButton("📎 Voir les Pièces Justificatives"); // Lié à la classe PieceJustificative

        actionPanel.add(btnVoirDetails);
        actionPanel.add(btnPieces);

        add(actionPanel, BorderLayout.SOUTH);
    }
}