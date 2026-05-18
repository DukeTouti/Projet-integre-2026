package views;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import controllers.AuthController;
import controllers.TTSController;
import models.PSH;

public class RegisterPage extends JPanel {

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField numeroEtudiantField;
    private JTextField typeHandicapField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton uploadButton;
    private JLabel fileStatusLabel;
    private JButton registerButton;
    private JButton switchToLoginButton;

    private File selectedPDF;
    private CardLayout cardLayout;
    private JPanel container;

    private static final Color FORM_BG_DARK       = new Color(13, 14, 16);
    private static final Color PANEL_INSIDE_BG    = new Color(22, 24, 28);
    private static final Color UIR_YELLOW_BRAND   = new Color(250, 204, 21);
    private static final Color UIR_YELLOW_PRESSED = new Color(234, 179, 8);
    private static final Color MAIN_WHITE_TEXT    = new Color(240, 240, 245);
    private static final Color DISCRETE_GRAY      = new Color(140, 142, 150);
    private static final Color COMP_INPUT_BG      = new Color(30, 33, 38);
    private static final Color CONFIRM_GREEN      = new Color(34, 197, 94);
    private static final Color ERROR_RED          = new Color(239, 68, 68);

    private static final String AUDIO_DIR = "audioRegisterPage";

    public RegisterPage(JPanel container, CardLayout cardLayout) {
        this.container  = container;
        this.cardLayout = cardLayout;

        setLayout(new GridBagLayout());
        setBackground(FORM_BG_DARK);

        Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();
        int base  = Math.max(13, (int)(monitor.height * 0.016));
        int cardW = Math.max(520, (int)(monitor.width  * 0.32));
        int cardH = Math.max(700, (int)(monitor.height * 0.85));

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_INSIDE_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(UIR_YELLOW_BRAND);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(cardW, cardH));

        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        // --- TITRE ---
        c.gridy = 0; c.gridwidth = 2; c.insets = new Insets(0, 0, 5, 0);
        JLabel icon = new JLabel("📝", SwingConstants.CENTER);
        icon.setFont(new Font("Dialog", Font.PLAIN, base + 14));
        card.add(icon, c);

        c.gridy = 1; c.insets = new Insets(0, 0, 4, 0);
        JLabel title = new JLabel("Créer un compte PSH", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, base + 6));
        title.setForeground(UIR_YELLOW_BRAND);
        card.add(title, c);

        c.gridy = 2; c.insets = new Insets(0, 0, 20, 0);
        JLabel sub = new JLabel("Votre compte sera activé après validation par l'administration.", SwingConstants.CENTER);
        sub.setFont(new Font("Dialog", Font.PLAIN, base - 2));
        sub.setForeground(DISCRETE_GRAY);
        card.add(sub, c);

        // --- CHAMPS ---
        c.gridwidth = 1;

        int row = 3;
        row = addField(card, c, base, row, "Nom",               nomField           = new JTextField());
        row = addField(card, c, base, row, "Prénom",            prenomField        = new JTextField());
        row = addField(card, c, base, row, "Email",             emailField         = new JTextField());
        row = addField(card, c, base, row, "N° Étudiant",       numeroEtudiantField= new JTextField());
        row = addField(card, c, base, row, "Type de handicap",  typeHandicapField  = new JTextField());
        row = addField(card, c, base, row, "Mot de passe",      passwordField      = new JPasswordField());
        row = addField(card, c, base, row, "Confirmer le MDP",  confirmPasswordField= new JPasswordField());

        // --- UPLOAD PDF ---
        c.gridy = row; c.gridx = 0; c.insets = new Insets(0, 4, 6, 8);
        card.add(makeLabel("Justificatif (PDF)", base), c);

        c.gridx = 1; c.insets = new Insets(0, 0, 4, 0);
        uploadButton = makeSecondaryButton("Choisir un fichier...", base);
        card.add(uploadButton, c);

        row++;
        c.gridy = row; c.gridx = 1; c.insets = new Insets(0, 6, 16, 0);
        fileStatusLabel = new JLabel("Aucun fichier sélectionné");
        fileStatusLabel.setFont(new Font("Dialog", Font.ITALIC, base - 2));
        fileStatusLabel.setForeground(DISCRETE_GRAY);
        card.add(fileStatusLabel, c);

        // --- BOUTON S'INSCRIRE ---
        row++;
        c.gridy = row; c.gridx = 0; c.gridwidth = 2; c.insets = new Insets(6, 0, 0, 0);
        registerButton = makePrimaryButton("S'inscrire", base);
        card.add(registerButton, c);

        // --- LIEN CONNEXION ---
        row++;
        c.gridy = row; c.insets = new Insets(12, 0, 0, 0);
        switchToLoginButton = new JButton("Déjà un compte ? Se connecter");
        switchToLoginButton.setFont(new Font("Dialog", Font.PLAIN, base - 1));
        switchToLoginButton.setForeground(UIR_YELLOW_BRAND);
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setFocusPainted(false);
        switchToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(switchToLoginButton, c);

        // ================================================================
        // HOVER AUDIO
        // ================================================================

        nomField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_nom.mp3"); }
        });
        prenomField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_prenom.mp3"); }
        });
        emailField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_email.mp3"); }
        });
        numeroEtudiantField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_id.mp3"); }
        });
        typeHandicapField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_handicap.mp3"); }
        });
        passwordField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_mdp1.mp3"); }
        });
        confirmPasswordField.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "zone_mdp2.mp3"); }
        });
        uploadButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "btn_justificatif.mp3"); }
        });
        registerButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "btn_sinscrire.mp3"); }
        });
        switchToLoginButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { TTSController.playSound(AUDIO_DIR, "btn_dejacompte.mp3"); }
        });

        // ================================================================
        // ACTIONS
        // ================================================================

        uploadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Documents PDF (*.pdf)", "pdf"));
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(RegisterPage.this) == JFileChooser.APPROVE_OPTION) {
                selectedPDF = chooser.getSelectedFile();
                fileStatusLabel.setText("✔ " + selectedPDF.getName());
                fileStatusLabel.setForeground(CONFIRM_GREEN);
            }
        });

        registerButton.addActionListener(e -> handleInscription());

        switchToLoginButton.addActionListener(e -> {
            cardLayout.show(container, "LOGIN_PANEL");
        });

        GridBagConstraints root = new GridBagConstraints();
        root.weightx = root.weighty = 1.0;

        JScrollPane scroll = new JScrollPane(card,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(scroll, root);
    }

    // ================================================================
    // LOGIQUE D'INSCRIPTION
    // ================================================================

    private void handleInscription() {
        String nom              = nomField.getText().trim();
        String prenom           = prenomField.getText().trim();
        String email            = emailField.getText().trim();
        String numeroEtudiant   = numeroEtudiantField.getText().trim();
        String typeHandicap     = typeHandicapField.getText().trim();
        String password         = new String(passwordField.getPassword());
        String confirm          = new String(confirmPasswordField.getPassword());

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() ||
                numeroEtudiant.isEmpty() || typeHandicap.isEmpty() ||
                password.isEmpty() || confirm.isEmpty()) {
            showError("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Email invalide", "Veuillez saisir une adresse email valide.");
            return;
        }

        if (!password.equals(confirm)) {
            showError("Mots de passe différents", "Les deux mots de passe ne correspondent pas.");
            confirmPasswordField.setText("");
            return;
        }

        if (selectedPDF == null) {
            showError("Document manquant", "Veuillez joindre un justificatif médical (PDF).");
            return;
        }

        PSH psh = AuthController.inscription(nom, prenom, email, password, numeroEtudiant, typeHandicap);

        if (psh == null) {
            showError("Inscription échouée", "Cet email est déjà utilisé ou une erreur s'est produite.");
            return;
        }

        showSuccess("Votre compte a été créé.\nL'administration doit l'activer avant que vous puissiez vous connecter.");

        resetForm();
        cardLayout.show(container, "LOGIN_PANEL");
    }

    // ================================================================
    // HELPERS
    // ================================================================

    private int addField(JPanel card, GridBagConstraints c, int base, int row, String label, JTextField field) {
        c.gridy = row; c.gridx = 0; c.insets = new Insets(0, 4, 4, 8);
        card.add(makeLabel(label, base), c);

        c.gridx = 1; c.insets = new Insets(0, 0, 10, 0);
        styleField(field, base);
        card.add(field, c);

        return row + 1;
    }

    private void showError(String titre, String message) {
        JOptionPane.showMessageDialog(this,
                "<html><body style='width:240px;padding:6px;color:#EF4444;'>" +
                        "<b>" + titre + "</b><br>" + message + "</body></html>",
                "Erreur", JOptionPane.PLAIN_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><body style='width:240px;padding:6px;color:#22C55E;'>" +
                        "<b>Inscription réussie ✔</b><br>" + message + "</body></html>",
                "Succès", JOptionPane.PLAIN_MESSAGE);
    }

    private void resetForm() {
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        numeroEtudiantField.setText("");
        typeHandicapField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        selectedPDF = null;
        fileStatusLabel.setText("Aucun fichier sélectionné");
        fileStatusLabel.setForeground(DISCRETE_GRAY);
    }

    private JLabel makeLabel(String text, int base) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Dialog", Font.BOLD, base));
        l.setForeground(UIR_YELLOW_BRAND);
        return l;
    }

    private void styleField(JTextField field, int base) {
        field.setBackground(COMP_INPUT_BG);
        field.setForeground(UIR_YELLOW_BRAND);
        field.setCaretColor(UIR_YELLOW_BRAND);
        field.setFont(new Font("Monospaced", Font.PLAIN, base + 1));
        field.setPreferredSize(new Dimension(0, Math.max(38, base + 22)));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIR_YELLOW_BRAND, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
    }

    private JButton makePrimaryButton(String text, int base) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? UIR_YELLOW_PRESSED : UIR_YELLOW_BRAND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Dialog", Font.BOLD, base + 1));
        btn.setForeground(Color.BLACK);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, Math.max(46, base + 30)));
        return btn;
    }

    private JButton makeSecondaryButton(String text, int base) {
        JButton btn = new JButton(text);
        btn.setBackground(COMP_INPUT_BG);
        btn.setForeground(MAIN_WHITE_TEXT);
        btn.setFont(new Font("Dialog", Font.PLAIN, base));
        btn.setBorder(new LineBorder(UIR_YELLOW_BRAND, 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, Math.max(38, base + 22)));
        return btn;
    }
}