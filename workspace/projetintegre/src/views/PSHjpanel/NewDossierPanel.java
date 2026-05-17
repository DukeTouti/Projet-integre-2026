package views.PSHjpanel;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class NewDossierPanel extends JPanel {
    private JTextArea txtDescription;
    private JComboBox<String> cmbTypeAmenagement;
    private JLabel lblFilePath;
    private File selectedFile;

    public NewDossierPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("📁 Déposer une Nouvelle Demande d'Aménagement");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel lblType = new JLabel("Type d'aménagement sollicité :");
        lblType.setFont(new Font("SansSerif", Font.BOLD, 13));

        cmbTypeAmenagement = new JComboBox<>(new String[]{
                "AMENAGEMENT_EXAMEN (Tiers-temps)",
                "ACCESSIBILITE_LOCAUX (Salles RDC)",
                "MATERIEL_ADAPTE (Prêt d'ordinateur)",
                "ACCOMPAGNEMENT_HUMAIN"
        }) {
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
        cmbTypeAmenagement.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        cmbTypeAmenagement.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbTypeAmenagement.setBorder(new RoundedBorder(12, new Color(180, 180, 180)));

        JLabel lblDesc = new JLabel("Description / Justification détaillée de votre besoin :");
        lblDesc.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDescription.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollDesc = new JScrollPane(txtDescription) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(txtDescription.getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        scrollDesc.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        scrollDesc.setBorder(new RoundedBorder(12, new Color(180, 180, 180)));
        scrollDesc.setOpaque(false);
        scrollDesc.getViewport().setOpaque(false);

        JLabel lblPiece = new JLabel("Téléverser un justificatif médical (PDF, PNG, JPG) :");
        lblPiece.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        uploadPanel.setOpaque(false);
        uploadPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        uploadPanel.setBorder(new RoundedBorder(12, new Color(200, 200, 200)));

        JButton btnBrowse = new JButton("📁 Parcourir...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnBrowse.setContentAreaFilled(false);
        btnBrowse.setFocusPainted(false);
        btnBrowse.setBorder(new RoundedBorder(10, Color.GRAY));

        lblFilePath = new JLabel("Aucun fichier sélectionné");
        lblFilePath.setFont(new Font("SansSerif", Font.ITALIC, 12));

        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                lblFilePath.setText(selectedFile.getName());
            }
        });

        uploadPanel.add(btnBrowse);
        uploadPanel.add(lblFilePath);

        formPanel.add(lblType);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(cmbTypeAmenagement);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblDesc);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(scrollDesc);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblPiece);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(uploadPanel);

        add(formPanel, BorderLayout.CENTER);

        JButton btnSoumettre = new JButton("🚀 Soumettre la Demande à la Commission") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g2);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.GRAY);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                g2.dispose();
            }
        };
        btnSoumettre.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSoumettre.setContentAreaFilled(false);
        btnSoumettre.setFocusPainted(false);
        btnSoumettre.setPreferredSize(new Dimension(getWidth(), 42));

        btnSoumettre.addActionListener(e -> {
            if (txtDescription.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La description est obligatoire (Attribut Dossier.description)", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Dossier enregistré ! Objet Dossier instancié avec sa PieceJustificative.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                txtDescription.setText("");
                lblFilePath.setText("Aucun fichier sélectionné");
                selectedFile = null;
            }
        });

        add(btnSoumettre, BorderLayout.SOUTH);
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