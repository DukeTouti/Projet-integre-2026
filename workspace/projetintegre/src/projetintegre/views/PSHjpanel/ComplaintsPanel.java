package projetintegre.views.PSHjpanel;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ComplaintsPanel extends JPanel {
    private JComboBox<String> comboDemandesTarget;
    private JTextArea txtMotifReclamation;
    private JRadioButton rbLinked;
    private JRadioButton rbGeneral;
    private JLabel lblTarget;

    public ComplaintsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("💬 Introduire une Réclamation (Contestation de décision)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        rbGeneral = new JRadioButton("Réclamation générale (Hors dossier)", true);
        rbLinked = new JRadioButton("Réclamation liée à une de mes demandes", false);
        rbGeneral.setFont(new Font("SansSerif", Font.BOLD, 13));
        rbLinked.setFont(new Font("SansSerif", Font.BOLD, 13));
        rbGeneral.setFocusPainted(false);
        rbLinked.setFocusPainted(false);
        rbGeneral.setOpaque(false);
        rbLinked.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        group.add(rbGeneral);
        group.add(rbLinked);

        lblTarget = new JLabel("Sélectionnez la demande concernée :");
        lblTarget.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTarget.setEnabled(false);

        String[] demandesFictives = { "Sélectionnez une demande...", "Demande #1042 - Aménagement Examen Math", "Demande #1089 - Accès Ascenseur Bloc B" };
        comboDemandesTarget = new JComboBox<>(demandesFictives) {
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
        comboDemandesTarget.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        comboDemandesTarget.setEnabled(false);
        comboDemandesTarget.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboDemandesTarget.setBorder(new RoundedBorder(12, new Color(180, 180, 180)));

        rbGeneral.addActionListener(e -> {
            lblTarget.setEnabled(false);
            comboDemandesTarget.setEnabled(false);
            comboDemandesTarget.setSelectedIndex(0);
        });

        rbLinked.addActionListener(e -> {
            lblTarget.setEnabled(true);
            comboDemandesTarget.setEnabled(true);
        });

        JLabel lblMotif = new JLabel("Motif détaillé de votre réclamation :");
        lblMotif.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtMotifReclamation = new JTextArea(6, 20);
        txtMotifReclamation.setLineWrap(true);
        txtMotifReclamation.setWrapStyleWord(true);
        txtMotifReclamation.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMotifReclamation.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollMotif = new JScrollPane(txtMotifReclamation) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(txtMotifReclamation.getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        scrollMotif.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));
        scrollMotif.setBorder(new RoundedBorder(12, new Color(180, 180, 180)));
        scrollMotif.setOpaque(false);
        scrollMotif.getViewport().setOpaque(false);

        JPanel radioWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        radioWrapper.setBorder(new RoundedBorder(12, new Color(220, 220, 220)));
        radioWrapper.setMaximumSize(new Dimension(Short.MAX_VALUE, 70));

        JPanel radioInner = new JPanel();
        radioInner.setLayout(new BoxLayout(radioInner, BoxLayout.Y_AXIS));
        radioInner.setOpaque(false);
        radioInner.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        radioInner.add(rbGeneral);
        radioInner.add(Box.createRigidArea(new Dimension(0, 3)));
        radioInner.add(rbLinked);
        radioWrapper.add(radioInner);

        formPanel.add(radioWrapper);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblTarget);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(comboDemandesTarget);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblMotif);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(scrollMotif);

        add(formPanel, BorderLayout.CENTER);

        JButton btnEnvoyer = new JButton("⚖️ Envoyer la Réclamation à l'Administrateur") {
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
        btnEnvoyer.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEnvoyer.setContentAreaFilled(false);
        btnEnvoyer.setFocusPainted(false);
        btnEnvoyer.setPreferredSize(new Dimension(getWidth(), 42));

        btnEnvoyer.addActionListener(e -> {
            String motif = txtMotifReclamation.getText().trim();
            int selectedIndex = comboDemandesTarget.getSelectedIndex();

            if (motif.isEmpty() || (rbLinked.isSelected() && selectedIndex <= 0)) {
                JOptionPane.showMessageDialog(this, "Tous les champs requis n'ont pas été remplis ou aucune demande n'a été sélectionnée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                if (rbLinked.isSelected()) {
                    String item = (String) comboDemandesTarget.getSelectedItem();
                    JOptionPane.showMessageDialog(this, "Réclamation liée à la \"" + item + "\" envoyée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Réclamation générale hors dossier envoyée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
                comboDemandesTarget.setSelectedIndex(0);
                txtMotifReclamation.setText("");
            }
        });

        add(btnEnvoyer, BorderLayout.SOUTH);
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