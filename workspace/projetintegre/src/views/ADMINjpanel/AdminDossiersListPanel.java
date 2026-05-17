package views.ADMINjpanel;

import controllers.DossierController;
import models.Demande;
import models.enums.StatutDossier;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;


public class AdminDossiersListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<Demande> demandes; // référence pour retrouver l'objet depuis la ligne sélectionnée

    private static final String[] COLUMNS = {"ID", "Étudiant (ID)", "Type d'Aménagement", "Date Dépôt", "Statut"};

    public AdminDossiersListPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("📁 Instruction et Décision sur les Demandes PSH");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // --- TABLE ---
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(0x374151));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new RoundedBorder(12, new Color(0x374151)));
        add(scroll, BorderLayout.CENTER);

        // --- BOUTONS ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setOpaque(false);

        JButton btnAccepter  = new JButton("✅ Accepter");
        JButton btnRefuser   = new JButton("❌ Refuser");
        JButton btnEnCours   = new JButton("🔄 Mettre EN_COURS");
        JButton btnActualiser = new JButton("🔃 Actualiser");

        controlPanel.add(btnActualiser);
        controlPanel.add(btnEnCours);
        controlPanel.add(btnAccepter);
        controlPanel.add(btnRefuser);
        add(controlPanel, BorderLayout.SOUTH);

        // --- ACTIONS ---
        btnAccepter.addActionListener(e -> changerStatut(StatutDossier.ACCEPTEE));
        btnRefuser.addActionListener(e  -> changerStatut(StatutDossier.REFUSEE));
        btnEnCours.addActionListener(e  -> changerStatut(StatutDossier.EN_COURS));
        btnActualiser.addActionListener(e -> chargerDonnees());

        // Chargement initial
        chargerDonnees();
    }

    // ================================================================
    // CHARGEMENT DEPUIS BD
    // ================================================================

    private void chargerDonnees() {
        demandes = DossierController.getAllDemandes();
        tableModel.setRowCount(0); // vider la table

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Demande d : demandes) {
            tableModel.addRow(new Object[]{
                    d.getId(),
                    d.getAuteur().getId(), // ID auteur — les vues admin voient l'id
                    d.getTypeDemande().name(),
                    sdf.format(d.getDateCreation()),
                    d.getStatut()
            });
        }
    }

    // ================================================================
    // CHANGEMENT DE STATUT
    // ================================================================

    private void changerStatut(StatutDossier nouveauStatut) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Sélectionnez un dossier dans la liste.",
                    "Sélection requise", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Demande demande = demandes.get(row);

        boolean ok = DossierController.majStatut(demande, nouveauStatut);

        if (ok) {
            tableModel.setValueAt(nouveauStatut.name(), row, 4);
            JOptionPane.showMessageDialog(this,
                    "Statut mis à jour : " + nouveauStatut.name(),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la mise à jour.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================================================================
    // BORDER
    // ================================================================

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