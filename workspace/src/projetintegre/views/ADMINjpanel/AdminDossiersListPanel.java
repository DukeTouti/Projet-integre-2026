package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminDossiersListPanel extends JPanel {

    public AdminDossiersListPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("📁 Instruction et Décision sur les Demandes PSH");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID Dossier", "Étudiant", "Type d'Aménagement", "Date Dépôt", "Statut Actuel"};
        Object[][] data = {
                {"1", "Ilyas Benali", "AMENAGEMENT_EXAMEN", "12/05/2026", "EN_ATTENTE"},
                {"3", "Mehdy Alami", "ACCESSIBILITE", "10/09/2025", "EN_COURS"}
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

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setOpaque(false);

        JButton btnAccepter = new JButton("✅ Accepter (ACCEPTEE)");
        JButton btnRefuser = new JButton("❌ Refuser (REFUSEE)");
        JButton btnCommenter = new JButton("📝 Ajouter Commentaire Admin");

        controlPanel.add(btnCommenter);
        controlPanel.add(btnAccepter);
        controlPanel.add(btnRefuser);
        add(controlPanel, BorderLayout.SOUTH);
    }
}