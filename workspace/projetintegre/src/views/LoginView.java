package views;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import controllers.AuthController;
import controllers.VoskSpeechController;
import controllers.TTSController;
import models.Utilisateur;


public class LoginView extends JFrame {
    private JPanel mainCardPanel;
    private CardLayout cardLayout;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnNoAccount;
    private JButton btnAccess;

    // boutons micro et mute — ajoutés pour Vosk et TTS
    private JButton btnMicro;
    private JButton btnMute;
    private boolean isRecording = false;
    private boolean isMuted = false;

    public static final Color PURPLE_PRIMARY = new Color(0x3C3489);
    private final Color BG_STANDARD = Color.WHITE;
    private final Color TEXT_STANDARD = new Color(0x1F2937);
    private final Color BORDER_STANDARD = new Color(0xE5E7EB);
    private final Color BG_ACCESSIBILITY = new Color(0x111827);
    private final Color TEXT_ACCESSIBILITY = new Color(0xFDE047);
    private final Color ERROR_COLOR = new Color(239, 68, 68);
    private final Color FIELD_BG = new Color(30, 33, 38);

    private boolean isAccessibilityMode = false;

    public LoginView() {
        setTitle("UIR · PSH Platform (Connexion)");
        setSize(480, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        mainCardPanel.add(createLoginPanel(), "LOGIN_PANEL");
        mainCardPanel.add(new RegisterPage(mainCardPanel, cardLayout), "REGISTER_PANEL");

        add(mainCardPanel);
        updateTheme();
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_STANDARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 25, 12, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // boutons micro + mute en haut à droite
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topBar.setOpaque(false);

        btnMicro = new JButton("🎙");
        btnMicro.setFont(new Font("Dialog", Font.PLAIN, 16));
        btnMicro.setForeground(PURPLE_PRIMARY);
        btnMicro.setBackground(new Color(0xF3F4F6));
        btnMicro.setBorder(BorderFactory.createLineBorder(PURPLE_PRIMARY, 2, true));
        btnMicro.setFocusPainted(false);
        btnMicro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMicro.setPreferredSize(new Dimension(46, 36));

        btnMute = new JButton("🔊");
        btnMute.setFont(new Font("Dialog", Font.PLAIN, 16));
        btnMute.setForeground(PURPLE_PRIMARY);
        btnMute.setBackground(new Color(0xF3F4F6));
        btnMute.setBorder(BorderFactory.createLineBorder(PURPLE_PRIMARY, 2, true));
        btnMute.setFocusPainted(false);
        btnMute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMute.setPreferredSize(new Dimension(46, 36));

        btnMute.addActionListener(e -> {
            isMuted = !isMuted;
            TTSController.toggleTTS();
            btnMute.setText(isMuted ? "🔇" : "🔊");
            btnMute.setForeground(isMuted ? ERROR_COLOR : PURPLE_PRIMARY);
            btnMute.setBorder(BorderFactory.createLineBorder(isMuted ? ERROR_COLOR : PURPLE_PRIMARY, 2, true));
        });

        topBar.add(btnMicro);
        topBar.add(btnMute);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 25, 0, 10);
        panel.add(topBar, gbc);

        // titre
        JLabel lblTitle = new JLabel("🎙️ Connexion", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(PURPLE_PRIMARY);
        gbc.gridy = 1; gbc.insets = new Insets(10, 25, 25, 25);
        panel.add(lblTitle, gbc);

        // identifiant
        gbc.insets = new Insets(8, 25, 4, 25);
        JLabel lblUser = new JLabel("Identifiant :");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(lblUser, gbc);

        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("zone_identifiant.mp3");
            }
        });
        gbc.gridy = 3;
        panel.add(txtUsername, gbc);

        // mot de passe
        JLabel lblPass = new JLabel("Mot de passe :");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = 4;
        panel.add(lblPass, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("zone_mdp.mp3");
            }
        });
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnLogin.doClick();
            }
        });
        gbc.gridy = 5;
        panel.add(txtPassword, gbc);

        // bouton connexion
        btnLogin = createCustomButton("Se connecter", true);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("btn_seconnecter.mp3");
            }
        });
        btnLogin.addActionListener(e -> {
            String email = txtUsername.getText().trim();
            String mdp = new String(txtPassword.getPassword());

            if (email.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(LoginView.this,
                        "Veuillez remplir tous les champs.",
                        "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Utilisateur u = AuthController.connexion(email, mdp);

            if (u == null) {
                JOptionPane.showMessageDialog(LoginView.this,
                        "Email ou mot de passe incorrect.",
                        "Échec de connexion", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            switch (u.getRole()) {
                case PSH:
                    new PshMainView();
                    break;
                case ADMINISTRATEUR:
                    new AdminMainView();
                    break;
            }
        });
        gbc.gridy = 6; gbc.insets = new Insets(30, 25, 10, 25);
        panel.add(btnLogin, gbc);

        // bouton inscription
        btnNoAccount = createCustomButton("Pas de compte ? Créer un compte", false);
        btnNoAccount.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnNoAccount.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("btn_creercompte.mp3");
            }
        });
        btnNoAccount.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            cardLayout.show(mainCardPanel, "REGISTER_PANEL");
        });
        gbc.gridy = 7; gbc.insets = new Insets(5, 25, 10, 25);
        panel.add(btnNoAccount, gbc);

        // bouton accessibilité
        btnAccess = createCustomButton("Mode Accessibilité : OFF", false);
        btnAccess.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAccess.addActionListener(e -> {
            isAccessibilityMode = !isAccessibilityMode;
            updateTheme();
        });
        gbc.gridy = 8; gbc.insets = new Insets(25, 25, 15, 25);
        panel.add(btnAccess, gbc);

        // logique Vosk
        btnMicro.addActionListener(e -> {
            if (!isRecording) {
                isRecording = true;
                btnMicro.setText("⏹");
                btnMicro.setForeground(ERROR_COLOR);
                btnMicro.setBorder(BorderFactory.createLineBorder(ERROR_COLOR, 2, true));

                VoskSpeechController.startListening(text -> SwingUtilities.invokeLater(() -> {
                    String recognized = text.toLowerCase().trim();
                    System.out.println("[VOSK] : " + recognized);

                    if (recognized.equals("root")) {
                        VoskSpeechController.stopListening();
                        dispose();
                        return;
                    }
                    if (recognized.equals("connect") || recognized.equals("username")) {
                        txtUsername.requestFocusInWindow();
                        resetMicroUI();
                        VoskSpeechController.stopListening();
                        return;
                    }
                    if (recognized.equals("password")) {
                        txtPassword.requestFocusInWindow();
                        resetMicroUI();
                        VoskSpeechController.stopListening();
                        return;
                    }
                    if (recognized.equals("send")) {
                        resetMicroUI();
                        VoskSpeechController.stopListening();
                        btnLogin.doClick();
                        return;
                    }
                    if (!recognized.isEmpty()) {
                        if (txtUsername.isFocusOwner()) txtUsername.setText(recognized);
                        else if (txtPassword.isFocusOwner()) txtPassword.setText(recognized);
                        else txtUsername.setText(recognized);
                        resetMicroUI();
                        VoskSpeechController.stopListening();
                    }
                }));
            } else {
                resetMicroUI();
                VoskSpeechController.stopListening();
            }
        });

        return panel;
    }

    private void resetMicroUI() {
        isRecording = false;
        btnMicro.setText("🎙");
        btnMicro.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY);
        btnMicro.setBorder(BorderFactory.createLineBorder(
                isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY, 2, true));
    }

    private JButton createCustomButton(String text, boolean isPrimary) {
        JButton button = new JButton(text) {
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
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 42));
        return button;
    }

    private void updateTheme() {
        Color currentBg   = isAccessibilityMode ? BG_ACCESSIBILITY : BG_STANDARD;
        Color currentText = isAccessibilityMode ? TEXT_ACCESSIBILITY : TEXT_STANDARD;
        Color currentBorder = isAccessibilityMode ? TEXT_ACCESSIBILITY : BORDER_STANDARD;

        mainCardPanel.setBackground(currentBg);

        btnAccess.setText(isAccessibilityMode ? "👁️ Mode Accessibilité (AAA) : ON" : "👁️ Mode Accessibilité (AAA) : OFF");
        btnAccess.setBackground(isAccessibilityMode ? Color.BLACK : new Color(0xF3F4F6));
        btnAccess.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY);
        btnAccess.setBorder(new RoundedBorder(12, isAccessibilityMode ? currentBorder : new Color(0xD1D5DB)));

        btnLogin.setBackground(isAccessibilityMode ? Color.BLACK : PURPLE_PRIMARY);
        btnLogin.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : Color.WHITE);
        btnLogin.setBorder(new RoundedBorder(12, currentBorder));

        btnNoAccount.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY);

        if (btnMicro != null) {
            btnMicro.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY);
            btnMicro.setBackground(isAccessibilityMode ? FIELD_BG : new Color(0xF3F4F6));
            btnMicro.setBorder(BorderFactory.createLineBorder(
                    isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY, 2, true));
        }
        if (btnMute != null) {
            btnMute.setForeground(isMuted ? ERROR_COLOR : (isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY));
            btnMute.setBackground(isAccessibilityMode ? FIELD_BG : new Color(0xF3F4F6));
            btnMute.setBorder(BorderFactory.createLineBorder(
                    isMuted ? ERROR_COLOR : (isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY), 2, true));
        }

        applyThemeRecursively(mainCardPanel, currentBg, currentText, currentBorder);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void applyThemeRecursively(Component comp, Color bg, Color text, Color border) {
        if (comp instanceof JPanel || comp instanceof JLabel) {
            comp.setBackground(bg);
            if (comp instanceof JLabel && !isAccessibilityMode && comp.getFont().getSize() == 24) {
                comp.setForeground(PURPLE_PRIMARY);
            } else {
                comp.setForeground(text);
            }
        }
        if (comp instanceof JTextField) {
            comp.setBackground(bg);
            comp.setForeground(text);
            ((JComponent) comp).setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(10, isAccessibilityMode ? text : border),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            ((JTextField) comp).setCaretColor(text);
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                if (!(child instanceof RegisterPage)) {
                    applyThemeRecursively(child, bg, text, border);
                }
            }
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