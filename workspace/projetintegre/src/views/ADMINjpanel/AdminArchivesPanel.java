package views.ADMINjpanel;

import controllers.ArchiveController;
import models.Dossier;
import models.enums.StatutDossier;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminArchivesPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMNS = {"ID Dossier", "Auteur (ID)", "Type", "Date Archivage", "Statut Terminal"};

    public AdminArchivesPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("🗄️ Consultation des Historiques Éteints (Classe Archive)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

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
        JButton btnSearch     = new JButton("🔍 Rechercher");
        JButton btnActualiser = new JButton("🔃 Tout afficher");

        topBar.add(cmbFiltre);
        topBar.add(txtQuery);
        topBar.add(btnSearch);
        topBar.add(btnActualiser);

        // Regrouper titre + barre de recherche dans un seul panneau NORTH
        JPanel northPanel = new JPanel(new BorderLayout(0, 10));
        northPanel.setOpaque(false);
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(topBar, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

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
        scroll.setBorder(new RoundedBorder(12, new Color(0x374151)));
        add(scroll, BorderLayout.CENTER);

        btnActualiser.addActionListener(e -> chargerDonnees(ArchiveController.getTousArchives()));

        btnSearch.addActionListener(e -> {
            String query = txtQuery.getText().trim();
            int index = cmbFiltre.getSelectedIndex();
            List<Dossier> resultats;

            try {
                if (index == 0) {
                    // rechercherParStatut()
                    StatutDossier statut = StatutDossier.valueOf(query.toUpperCase());
                    resultats = ArchiveController.rechercherParStatut(statut);
                } else if (index == 1) {
                    // rechercherParType()
                    resultats = ArchiveController.rechercherParType(query);
                } else {
                    // rechercherParAuteur()
                    int idAuteur = Integer.parseInt(query);
                    resultats = ArchiveController.rechercherParAuteur(idAuteur);
                }
                chargerDonnees(resultats);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Valeur invalide. Exemples :\n" +
                        "- Par statut : EN_ATTENTE, ACCEPTEE, REFUSEE, CLOTUREE\n" +
                        "- Par type   : RECLAMATION ou un TypeDemande (ex. AMENAGEMENT_EXAMEN)\n" +
                        "- Par auteur : numéro entier (ID utilisateur)",
                        "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            }
        });

        chargerDonnees(ArchiveController.getTousArchives());
    }

    private void chargerDonnees(List<Dossier> dossiers) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tableModel.setRowCount(0);
        for (Dossier d : dossiers) {
            String dateArchiv = d.getDateArchivage() != null ? sdf.format(d.getDateArchivage()) : "-";
            tableModel.addRow(new Object[]{
                d.getId(),
                d.getAuteur() != null ? d.getAuteur().getId() : "-",
                d.getType(),
                dateArchiv,
                d.getStatutDossier().name()
            });
        }
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
