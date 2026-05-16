package views.ADMINjpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminArchivesPanel extends JPanel {

    public AdminArchivesPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("🗄️ Consultation des Historiques Éteints (Classe Archive)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topBar.setOpaque(false);

        topBar.add(new JLabel("Rechercher par critère :"));
        JComboBox<String> cmbFiltre = new JComboBox<>(new String[]{
                "rechercherParStatut()",
                "rechercherParType()",
                "rechercherParAuteur()"
        });

        JTextField txtQuery = new JTextField(15);
        txtQuery.setMaximumSize(new Dimension(150, 30));
        JButton btnSearch = new JButton("🔍 Rechercher");

        topBar.add(cmbFiltre);
        topBar.add(txtQuery);
        topBar.add(btnSearch);

        add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID Dossier", "Auteur (ID)", "Type", "Date Archivage", "Statut Terminal"};
        Object[][] data = {
                {"99", "12", "MATERIEL_ADAPTE", "15/12/2025", "CLOTUREE"}
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
    }
}