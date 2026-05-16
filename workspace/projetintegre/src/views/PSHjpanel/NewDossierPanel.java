package views.PSHjpanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class NewDossierPanel extends JPanel {
    private JTextArea txtDescription;
    private JComboBox<String> cmbTypeAmenagement;
    private JLabel lblFilePath;
    private File selectedFile;

    public NewDossierPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 1. Titre
        JLabel titleLabel = new JLabel("📁 Déposer une Nouvelle Demande d'Aménagement");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Champ Type d'aménagement (Lié à la logique de ta classe Demande)
        JLabel lblType = new JLabel("Type d'aménagement sollicité :");
        lblType.setFont(new Font("SansSerif", Font.BOLD, 13));
        cmbTypeAmenagement = new JComboBox<>(new String[]{
                "AMENAGEMENT_EXAMEN (Tiers-temps)",
                "ACCESSIBILITE_LOCAUX (Salles RDC)",
                "MATERIEL_ADAPTE (Prêt d'ordinateur)",
                "ACCOMPAGNEMENT_HUMAIN"
        });
        cmbTypeAmenagement.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));

        // Champ Description (Attribut #description de la classe Dossier)
        JLabel lblDesc = new JLabel("Description détaillée des besoins (justification) :");
        lblDesc.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtDescription = new JTextArea(6, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));

        // Zone PieceJustificative (Classe PieceJustificative de ton UML)
        JLabel lblPiece = new JLabel("Pièce Justificative (Certificat Médical, MDPH...) :");
        lblPiece.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        uploadPanel.setOpaque(false);
        JButton btnUpload = new JButton("📎 Sélectionner un fichier (PDF, PNG)");
        lblFilePath = new JLabel("Aucun fichier sélectionné (Instance de PieceJustificative)");
        lblFilePath.setFont(new Font("SansSerif", Font.ITALIC, 12));

        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                lblFilePath.setText("Fichier prêt : " + selectedFile.getName());
            }
        });
        uploadPanel.add(btnUpload);
        uploadPanel.add(lblFilePath);
        uploadPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        // Assemblage du formulaire avec espaces
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

        // 3. Bouton Soumettre en bas (Appellera creerDossier())
        JButton btnSoumettre = new JButton("🚀 Soumettre la Demande à la Commission");
        btnSoumettre.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnSoumettre.addActionListener(e -> {
            if(txtDescription.getText().trim().isEmpty()) {
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
}