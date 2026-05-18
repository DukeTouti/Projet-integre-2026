package views.PSHjpanel;

import controllers.DemandeController;
import controllers.ReclamationController;
import models.Demande;
import models.Reclamation;
import models.Utilisateur;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyDossiersPanel extends JPanel {

    public MyDossiersPanel(Utilisateur user) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("🗂️ Historique Global de mes Dossiers (Demandes & Réclamations)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID Dossier", "Type de Dossier", "Description", "Date Création", "Date MAJ", "Statut"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<Demande> demandes = DemandeController.getMesDemandes(user.getId());
        for (Demande d : demandes) {
            model.addRow(new Object[]{
                d.getId(),
                "DEMANDE (" + d.getTypeDemande().name() + ")",
                d.getDescription(),
                d.getDateCreation() != null ? sdf.format(d.getDateCreation()) : "-",
                d.getDateMaj()      != null ? sdf.format(d.getDateMaj())      : "-",
                d.getStatutDossier().name()
            });
        }

        List<Reclamation> reclamations = ReclamationController.getMesReclamations(user.getId());
        for (Reclamation r : reclamations) {
            model.addRow(new Object[]{
                r.getId(),
                "RECLAMATION",
                r.getDescription(),
                r.getDateCreation() != null ? sdf.format(r.getDateCreation()) : "-",
                r.getDateMaj()      != null ? sdf.format(r.getDateMaj())      : "-",
                r.getStatutDossier().name()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(table.getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        scrollPane.setBorder(new RoundedBorder(12, new Color(0x374151)));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton btnVoirDetails = new JButton("🔍 Voir Pièces & Détails") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnVoirDetails.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVoirDetails.setContentAreaFilled(false);
        btnVoirDetails.setFocusPainted(false);
        btnVoirDetails.setBorder(new RoundedBorder(12, Color.GRAY));
        btnVoirDetails.setPreferredSize(new Dimension(160, 35));

        btnVoirDetails.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Object id = table.getValueAt(row, 0);
                JOptionPane.showMessageDialog(this, "Chargement des détails de l'objet Dossier ID #" + id + "\nFichiers binaires rattachés récupérés.", "Détails Dossier", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un dossier dans le tableau.", "Sélection requise", JOptionPane.WARNING_MESSAGE);
            }
        });

        actionPanel.add(btnVoirDetails);
        add(actionPanel, BorderLayout.SOUTH);
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
