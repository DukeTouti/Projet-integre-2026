package views.ADMINjpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminComplaintsPanel extends JPanel {

    public AdminComplaintsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("💬 Traitement des Réclamations Reçues");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID Réc", "ID Dossier Cible", "Description Contestation", "Statut"};
        Object[][] data = {
                {"8", "2", "Contestation refus aménagement sur module Compilation", "EN_ATTENTE"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
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

        JButton btnRepondre = new JButton("✍️ Enregistrer Réponse Administrative");
        actionPanel.add(btnRepondre);
        add(actionPanel, BorderLayout.SOUTH);
    }
}