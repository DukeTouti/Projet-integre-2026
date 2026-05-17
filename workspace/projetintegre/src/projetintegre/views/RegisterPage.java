package projetintegre.views;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class RegisterPage extends JPanel {
    private JTextField emailField;
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
    private static final Color ERROR_RED_PANIC    = new Color(239, 68, 68);
    private static final Color CONFIRM_GREEN      = new Color(34, 197, 94);

    public RegisterPage(JPanel container, CardLayout cardLayout) {
        this.container = container;
        this.cardLayout = cardLayout;

        setLayout(new GridBagLayout());
        setBackground(FORM_BG_DARK);

        Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();
        int base = Math.max(13, (int)(monitor.height * 0.016));
        int cardW = Math.max(520, (int)(monitor.width * 0.32));
        int cardH = Math.max(620, (int)(monitor.height * 0.70));

        JPanel card = new JPanel(new GridBagLayout()) {
            private final RenderingHints antiAliasingMap = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );

            @Override
            protected void paintComponent(Graphics graphicsContext) {
                Graphics2D brush = (Graphics2D) graphicsContext.create();
                brush.setRenderingHints(antiAliasingMap);

                brush.setColor(PANEL_INSIDE_BG);
                brush.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                brush.setColor(UIR_YELLOW_BRAND);
                brush.setStroke(new BasicStroke(1.5f));
                brush.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

                brush.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(cardW, cardH));

        GridBagConstraints layoutSpecs = new GridBagConstraints();
        layoutSpecs.fill = GridBagConstraints.HORIZONTAL;
        layoutSpecs.weightx = 1.0;

        layoutSpecs.gridy = 0;
        layoutSpecs.gridwidth = 2;
        layoutSpecs.insets = new Insets(0, 0, 12, 0);
        JLabel icon = new JLabel("📝", SwingConstants.CENTER);
        icon.setFont(new Font("Dialog", Font.PLAIN, base + 18));
        card.add(icon, layoutSpecs);

        layoutSpecs.gridy = 1;
        layoutSpecs.insets = new Insets(0, 0, 5, 0);
        JLabel titleLabel = new JLabel("Créer un compte PSH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, base + 8));
        titleLabel.setForeground(UIR_YELLOW_BRAND);
        card.add(titleLabel, layoutSpecs);

        layoutSpecs.gridy = 2;
        layoutSpecs.insets = new Insets(0, 0, 22, 0);
        JLabel sub = new JLabel("Formulaire d'inscription sécurisé", SwingConstants.CENTER);
        sub.setFont(new Font("Dialog", Font.PLAIN, base - 1));
        sub.setForeground(DISCRETE_GRAY);
        card.add(sub, layoutSpecs);

        layoutSpecs.gridwidth = 1;

        layoutSpecs.gridy = 3;
        layoutSpecs.gridx = 0;
        layoutSpecs.insets = new Insets(0, 4, 6, 8);
        card.add(makeLabel("Email", base), layoutSpecs);

        layoutSpecs.gridx = 1;
        layoutSpecs.insets = new Insets(0, 0, 14, 0);
        emailField = new JTextField();
        styleField(emailField, base);
        card.add(emailField, layoutSpecs);

        layoutSpecs.gridy = 4;
        layoutSpecs.gridx = 0;
        layoutSpecs.insets = new Insets(0, 4, 6, 8);
        card.add(makeLabel("Mot de passe", base), layoutSpecs);

        layoutSpecs.gridx = 1;
        layoutSpecs.insets = new Insets(0, 0, 14, 0);
        passwordField = new JPasswordField();
        styleField(passwordField, base);
        card.add(passwordField, layoutSpecs);

        layoutSpecs.gridy = 5;
        layoutSpecs.gridx = 0;
        layoutSpecs.insets = new Insets(0, 4, 6, 8);
        card.add(makeLabel("Confirmer le MDP", base), layoutSpecs);

        layoutSpecs.gridx = 1;
        layoutSpecs.insets = new Insets(0, 0, 18, 0);
        confirmPasswordField = new JPasswordField();
        styleField(confirmPasswordField, base);
        card.add(confirmPasswordField, layoutSpecs);

        layoutSpecs.gridy = 6;
        layoutSpecs.gridx = 0;
        layoutSpecs.insets = new Insets(0, 4, 6, 8);
        card.add(makeLabel("Justificatif (PDF)", base), layoutSpecs);

        layoutSpecs.gridx = 1;
        layoutSpecs.insets = new Insets(0, 0, 5, 0);
        uploadButton = makeSecondaryButton("Choisir un fichier...", base);
        card.add(uploadButton, layoutSpecs);

        layoutSpecs.gridy = 7;
        layoutSpecs.gridx = 1;
        layoutSpecs.insets = new Insets(0, 6, 22, 0);
        fileStatusLabel = new JLabel("Aucun fichier sélectionné", SwingConstants.LEFT);
        fileStatusLabel.setFont(new Font("Dialog", Font.ITALIC, base - 2));
        fileStatusLabel.setForeground(DISCRETE_GRAY);
        card.add(fileStatusLabel, layoutSpecs);

        layoutSpecs.gridx = 0;
        layoutSpecs.gridy = 8;
        layoutSpecs.gridwidth = 2;
        layoutSpecs.insets = new Insets(5, 0, 0, 0);
        registerButton = makePrimaryButton("S'inscrire", base);
        card.add(registerButton, layoutSpecs);

        layoutSpecs.gridy = 9;
        layoutSpecs.insets = new Insets(14, 0, 0, 0);
        switchToLoginButton = new JButton("Déjà un compte ? Se connecter");
        switchToLoginButton.setFont(new Font("Dialog", Font.PLAIN, base - 1));
        switchToLoginButton.setForeground(UIR_YELLOW_BRAND);
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setFocusPainted(false);
        switchToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(switchToLoginButton, layoutSpecs);

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Documents PDF (*.pdf)", "pdf");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);

                int returnValue = fileChooser.showOpenDialog(RegisterPage.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedPDF = fileChooser.getSelectedFile();
                    fileStatusLabel.setText("✔ " + selectedPDF.getName());
                    fileStatusLabel.setForeground(CONFIRM_GREEN);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int popupWidth = Math.max(260, (int)(screenSize.width * 0.18));

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    UIManager.put("OptionPane.background", FORM_BG_DARK);
                    UIManager.put("Panel.background", FORM_BG_DARK);

                    String errorMessage = "<html><body style='width: " + popupWidth + "px; padding: 10px; font-family: Dialog; color: #EF4444;'>"
                            + "<h3 style='color: #EF4444; margin: 0 0 8px 0;'>Champs manquants</h3>"
                            + "Veuillez renseigner tous les champs d'identification."
                            + "</body></html>";

                    JOptionPane.showMessageDialog(
                            RegisterPage.this,
                            errorMessage,
                            "Erreur",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    UIManager.put("OptionPane.background", FORM_BG_DARK);
                    UIManager.put("Panel.background", FORM_BG_DARK);

                    String errorMessage = "<html><body style='width: " + popupWidth + "px; padding: 10px; font-family: Dialog; color: #EF4444;'>"
                            + "<h3 style='color: #EF4444; margin: 0 0 8px 0;'>Erreur de validation</h3>"
                            + "Les mots de passe ne correspondent pas !"
                            + "</body></html>";

                    JOptionPane.showMessageDialog(
                            RegisterPage.this,
                            errorMessage,
                            "Erreur",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    confirmPasswordField.setText("");
                    return;
                }

                if (selectedPDF == null) {
                    UIManager.put("OptionPane.background", FORM_BG_DARK);
                    UIManager.put("Panel.background", FORM_BG_DARK);

                    String errorMessage = "<html><body style='width: " + popupWidth + "px; padding: 10px; font-family: Dialog; color: #EF4444;'>"
                            + "<h3 style='color: #EF4444; margin: 0 0 8px 0;'>Document manquant</h3>"
                            + "Veuillez téléverser une pièce justificative au format PDF."
                            + "</body></html>";

                    JOptionPane.showMessageDialog(
                            RegisterPage.this,
                            errorMessage,
                            "Erreur",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    return;
                }

                UIManager.put("OptionPane.background", FORM_BG_DARK);
                UIManager.put("Panel.background", FORM_BG_DARK);

                String successMessage = "<html><body style='width: " + popupWidth + "px; padding: 10px; font-family: Dialog; color: #22C55E;'>"
                        + "<h3 style='color: #22C55E; margin: 0 0 8px 0;'>Succès</h3>"
                        + "Compte créé avec succès !"
                        + "</body></html>";

                JOptionPane.showMessageDialog(
                        RegisterPage.this,
                        successMessage,
                        "Succès",
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        });

        switchToLoginButton.addActionListener(e -> {
            new LoginView().setVisible(true);
            Component topLevel = SwingUtilities.getWindowAncestor(RegisterPage.this);
            if (topLevel instanceof Window) {
                ((Window) topLevel).dispose();
            }
        });

        GridBagConstraints rootSpecs = new GridBagConstraints();
        rootSpecs.weightx = rootSpecs.weighty = 1.0;
        add(card, rootSpecs);
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
        field.setPreferredSize(new Dimension(0, Math.max(40, base + 24)));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIR_YELLOW_BRAND, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private JButton makePrimaryButton(String text, int base) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D clickCanvas = (Graphics2D) g.create();
                clickCanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                clickCanvas.setColor(getModel().isRollover() ? UIR_YELLOW_PRESSED : UIR_YELLOW_BRAND);
                clickCanvas.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                clickCanvas.dispose();
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