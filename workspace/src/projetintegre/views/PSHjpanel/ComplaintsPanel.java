package projetintegre.views.PSHjpanel;

import javax.swing.*;
import java.awt.*;

public class ComplaintsPanel extends JPanel {
    private JTextField txtIdDossierTarget;
    private JTextArea txtMotifReclamation;

    public ComplaintsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 1. Titre
        JLabel titleLabel = new JLabel("💬 Introduire une Réclamation (Contestation de décision)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Formulaire de réclamation
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Lien vers l'ID du dossier d'aménagement initial
        JLabel lblTarget = new JLabel("ID du Dossier d'aménagement concerné (Héritage Dossier) :");
        lblTarget.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtIdDossierTarget = new JTextField();
        txtIdDossierTarget.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));

        // Motif de la contestation (Dossier.description)
        JLabel lblMotif = new JLabel("Motif détaillé de votre réclamation :");
        lblMotif.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtMotifReclamation = new JTextArea(6, 20);
        txtMotifReclamation.setLineWrap(true);
        txtMotifReclamation.setWrapStyleWord(true);
        JScrollPane scrollMotif = new JScrollPane(txtMotifReclamation);
        scrollMotif.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));

        formPanel.add(lblTarget);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtIdDossierTarget);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblMotif);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(scrollMotif);

        add(formPanel, BorderLayout.CENTER);

        // 3. Bouton Envoi
        JButton btnEnvoyer = new JButton("⚖️ Envoyer la Réclamation à l'Administrateur");
        btnEnvoyer.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnEnvoyer.addActionListener(e -> {
            String idTarget = txtIdDossierTarget.getText().trim();
            String motif = txtMotifReclamation.getText().trim();

            if(idTarget.isEmpty() || motif.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont requis pour instancier la Réclamation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Réclamation liée au Dossier #" + idTarget + " envoyée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                txtIdDossierTarget.setText("");
                txtMotifReclamation.setText("");
            }
        });

        add(btnEnvoyer, BorderLayout.SOUTH);
    }
}