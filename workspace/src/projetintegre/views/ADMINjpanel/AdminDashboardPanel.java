package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {
    public AdminDashboardPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📊 Table des dossiers récents à traiter");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0x115E59));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        String[] columnNames = {"Étudiant", "Type d'Aménagement", "Date", "Statut"};
        Object[][] data = {
                {"Karim M.", "AMENAGEMENT_EXAMEN", "15 Jan", "EN ATTENTE"},
                {"Sara B.", "ACCESSIBILITE", "14 Jan", "EN COURS"},
                {"Yassine A.", "ACCOMPAGNEMENT", "13 Jan", "EN ATTENTE"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(32);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}