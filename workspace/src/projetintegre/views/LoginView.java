package projetintegre.views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import projetintegre.controllers.VoskSpeechController;
import projetintegre.controllers.TTSController;

public class LoginView extends JFrame {

    private JTextField txtIdentifiant;
    private JPasswordField txtPassword;
    private JButton btnConnexion;
    private JButton btnMicro;
    private JButton btnMute;
    private boolean isRecording = false;
    private boolean isMuted = false;

    // ===== PALETTE HAUTE VISIBILITÉ (CONFORME WCAG) =====
    private static final Color BG_DARK       = new Color(13, 14, 16);
    private static final Color BG_CARD       = new Color(22, 24, 28);
    private static final Color ACCENT        = new Color(250, 204, 21);
    private static final Color ACCENT_HOVER  = new Color(234, 179, 8);
    private static final Color TEXT_PRIMARY  = new Color(240, 240, 245);
    private static final Color TEXT_MUTED    = new Color(140, 142, 150);
    private static final Color FIELD_BG      = new Color(30, 33, 38);
    private static final Color ERROR_COLOR   = new Color(239, 68, 68);

    public LoginView() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.max(480, (int)(screen.width  * 0.26));
        int h = Math.max(560, (int)(screen.height * 0.62));

        setTitle("Connexion - UIR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(w, h);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        int base = Math.max(13, (int)(screen.height * 0.016));

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_DARK);
        add(root);

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth()-1, getHeight()-1, 18, 18));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(44, 44, 44, 44));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        // ===== PANNEAU DE CONTRÔLE EN HAUT À DROITE (MICRO & MUTE) =====
        c.gridy = 0; c.insets = new Insets(0, 0, 10, 0);
        JPanel topControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topControlPanel.setOpaque(false);

        btnMicro = new JButton("🎙");
        styleMicroButton(btnMicro, base);

        btnMute = new JButton("🔊");
        btnMute.setFont(new Font("Dialog", Font.PLAIN, base + 2));
        btnMute.setForeground(ACCENT);
        btnMute.setBackground(FIELD_BG);
        btnMute.setBorder(new LineBorder(ACCENT, 2, true));
        btnMute.setFocusPainted(false);
        btnMute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMute.setPreferredSize(new Dimension(50, 40));

        btnMute.addActionListener(e -> {
            isMuted = !isMuted;
            if (isMuted) {
                btnMute.setText("🔇");
                btnMute.setForeground(ERROR_COLOR);
                btnMute.setBorder(new LineBorder(ERROR_COLOR, 2, true));
            } else {
                btnMute.setText("🔊");
                btnMute.setForeground(ACCENT);
                btnMute.setBorder(new LineBorder(ACCENT, 2, true));
            }
        });

        topControlPanel.add(btnMicro);
        topControlPanel.add(btnMute);
        card.add(topControlPanel, c);

        c.gridy = 1; c.insets = new Insets(0, 0, 6, 0);
        JLabel icon = new JLabel("🔐", SwingConstants.CENTER);
        icon.setFont(new Font("Dialog", Font.PLAIN, base + 14));
        card.add(icon, c);

        c.gridy = 2;
        JLabel title = new JLabel("Connexion", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, base + 10));
        title.setForeground(ACCENT);
        card.add(title, c);

        c.gridy = 3; c.insets = new Insets(0, 0, 36, 0);
        JLabel sub = new JLabel("Accès à la plateforme UIR", SwingConstants.CENTER);
        sub.setFont(new Font("Dialog", Font.PLAIN, base - 1));
        sub.setForeground(TEXT_MUTED);
        card.add(sub, c);

        // Identifiant
        c.gridy = 4; c.insets = new Insets(0, 0, 6, 0);
        JLabel lblId = makeLabel("Identifiant", base);
        card.add(lblId, c);

        c.gridy = 5; c.insets = new Insets(0, 0, 20, 0);
        txtIdentifiant = new JTextField();
        styleField(txtIdentifiant, base);
        card.add(txtIdentifiant, c);

        txtIdentifiant.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("Zone_de_saisi_de_l'identifiant.mp3");
            }
        });

        // Mot de passe
        c.gridy = 6; c.insets = new Insets(0, 0, 6, 0);
        JLabel lblPass = makeLabel("Mot de passe", base);
        card.add(lblPass, c);

        c.gridy = 7; c.insets = new Insets(0, 0, 36, 0);
        txtPassword = new JPasswordField();
        styleField(txtPassword, base);
        card.add(txtPassword, c);

        txtPassword.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("Zone_de_saisi_du_mot_de_passe.mp3");
            }
        });

        c.gridy = 8; c.insets = new Insets(0, 0, 0, 0);
        btnConnexion = makeButton("Se connecter", base);
        card.add(btnConnexion, c);

        btnConnexion.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isMuted) TTSController.playSound("Boutton_se_connecter.mp3");
            }
        });

        c.gridy = 9; c.insets = new Insets(20, 0, 0, 0);
        JLabel hint = new JLabel("Commandes : 'connect', 'password', 'button' ou 'root'", SwingConstants.CENTER);
        hint.setFont(new Font("Dialog", Font.PLAIN, base - 3));
        hint.setForeground(TEXT_MUTED);
        card.add(hint, c);

        // ===== LOGIQUE RECO VOCALE =====
        btnMicro.addActionListener(e -> {
            if (!isRecording) {
                isRecording = true;
                btnMicro.setForeground(ERROR_COLOR);
                btnMicro.setBorder(new LineBorder(ERROR_COLOR, 2, true));
                btnMicro.setText("⏹");

                VoskSpeechController.startListening(text -> {
                    SwingUtilities.invokeLater(() -> {
                        String recognized = text.toLowerCase().trim();
                        System.out.println("[VOSK DETECTED] : " + recognized);

                        if (recognized.equals("root")) {
                            VoskSpeechController.stopListening();
                            dispose();
                            return;
                        }

                        // MODIFICATION : "connect" redirige maintenant vers l'Identifiant
                        if (recognized.equals("identifiant") || recognized.equals("username") || recognized.equals("connect")) {
                            txtIdentifiant.requestFocusInWindow();
                            resetMicroUI();
                            VoskSpeechController.stopListening();
                            return;
                        }
                        if (recognized.equals("password") || recognized.equals("mot de passe")) {
                            txtPassword.requestFocusInWindow();
                            resetMicroUI();
                            VoskSpeechController.stopListening();
                            return;
                        }
                        if (recognized.equals("send")) {
                            resetMicroUI();
                            VoskSpeechController.stopListening();
                            btnConnexion.doClick();
                            return;
                        }

                        // Saisie dynamique si aucun focus
                        if (!recognized.isEmpty()) {
                            if (txtIdentifiant.isFocusOwner()) {
                                txtIdentifiant.setText(recognized);
                            } else if (txtPassword.isFocusOwner()) {
                                txtPassword.setText(recognized);
                            } else {
                                txtIdentifiant.setText(recognized);
                            }
                            resetMicroUI();
                            VoskSpeechController.stopListening();
                        }
                    });
                });
            } else {
                resetMicroUI();
                VoskSpeechController.stopListening();
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnConnexion.doClick();
            }
        });

        GridBagConstraints rootC = new GridBagConstraints();
        rootC.weightx = rootC.weighty = 1.0;
        root.add(card, rootC);
        setVisible(true);
    }

    private void resetMicroUI() {
        isRecording = false;
        btnMicro.setForeground(ACCENT);
        btnMicro.setBorder(new LineBorder(ACCENT, 2, true));
        btnMicro.setText("🎙");
    }

    private void styleMicroButton(JButton btn, int base) {
        btn.setBackground(FIELD_BG);
        btn.setForeground(ACCENT);
        btn.setFont(new Font("Dialog", Font.PLAIN, base + 5));
        btn.setBorder(new LineBorder(ACCENT, 2, true));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(50, 40));
    }

    private JLabel makeLabel(String text, int base) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Dialog", Font.BOLD, base));
        l.setForeground(ACCENT);
        return l;
    }

    private void styleField(JTextField field, int base) {
        field.setBackground(FIELD_BG);
        field.setForeground(ACCENT);
        field.setCaretColor(ACCENT);
        field.setFont(new Font("Monospaced", Font.PLAIN, base + 1));
        field.setPreferredSize(new Dimension(0, Math.max(46, base + 32)));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACCENT, 2, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
    }

    private JButton makeButton(String text, int base) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? ACCENT_HOVER : ACCENT);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
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
        btn.setPreferredSize(new Dimension(0, Math.max(50, base + 34)));
        return btn;
    }

}