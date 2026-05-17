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
    private JButton btnRegisterLink;
    private JButton btnMicro;
    private JButton btnMute;
    private boolean isRecording = false;
    private boolean isMuted = false;

    // --- CONFIGURATION DESIGN STYLE HAUT CONTRASTE ---
    public static final Color PURPLE_PRIMARY = new Color(0x3C3489);
    private static final Color BG_DARK       = new Color(0xF9FAFB);
    private static final Color BG_CARD       = Color.WHITE;
    private static final Color ACCENT        = new Color(0x111827);
    private static final Color ACCENT_HOVER  = new Color(0x374151);
    private static final Color TEXT_PRIMARY  = new Color(0x000000);
    private static final Color TEXT_MUTED    = new Color(0x4B5563);
    private static final Color FIELD_BG      = Color.WHITE;
    private static final Color ERROR_COLOR   = new Color(0xEF4444);

    public LoginView() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.max(480, (int)(screen.width  * 0.26));
        int h = Math.max(620, (int)(screen.height * 0.70));

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
                g2.setColor(PURPLE_PRIMARY);
                g2.setStroke(new BasicStroke(2.0f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth()-2, getHeight()-2, 18, 18));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 40, 25, 40));
        card.setPreferredSize(new Dimension(w - 60, h - 80));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;

        // Top Control Panel
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

        // --- SÉCURISATION DU CHARGEMENT DU LOGO ---
        c.gridy = 1; c.insets = new Insets(5, 0, 10, 0);
        JComponent logoComp = getUirLogoComponent(base);
        card.add(logoComp, c);

        c.gridy = 2; c.insets = new Insets(0, 0, 4, 0);
        JLabel title = new JLabel("Connexion", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, base + 10));
        title.setForeground(PURPLE_PRIMARY);
        card.add(title, c);

        c.gridy = 3; c.insets = new Insets(0, 0, 20, 0);
        JLabel sub = new JLabel("Accès à la plateforme UIR", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, base - 1));
        sub.setForeground(TEXT_MUTED);
        card.add(sub, c);

        c.gridy = 4; c.insets = new Insets(0, 0, 6, 0);
        JLabel lblId = makeLabel("Identifiant", base);
        card.add(lblId, c);

        c.gridy = 5; c.insets = new Insets(0, 0, 15, 0);
        txtIdentifiant = new JTextField();
        styleField(txtIdentifiant, base);
        card.add(txtIdentifiant, c);

        c.gridy = 6; c.insets = new Insets(0, 0, 6, 0);
        JLabel lblPass = makeLabel("Mot de passe", base);
        card.add(lblPass, c);

        c.gridy = 7; c.insets = new Insets(0, 0, 25, 0);
        txtPassword = new JPasswordField();
        styleField(txtPassword, base);
        card.add(txtPassword, c);

        c.gridy = 8; c.insets = new Insets(0, 0, 0, 0);
        btnConnexion = makeButton("Se connecter", base);
        card.add(btnConnexion, c);

        c.gridy = 9; c.insets = new Insets(12, 0, 0, 0);
        btnRegisterLink = new JButton("Pas de compte ? Créer un compte PSH");
        btnRegisterLink.setFont(new Font("SansSerif", Font.BOLD, base - 1));
        btnRegisterLink.setForeground(PURPLE_PRIMARY);
        btnRegisterLink.setBorderPainted(false);
        btnRegisterLink.setContentAreaFilled(false);
        btnRegisterLink.setFocusPainted(false);
        btnRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(btnRegisterLink, c);

        c.gridy = 10; c.insets = new Insets(15, 0, 0, 0);
        JLabel hint = new JLabel("Commandes vocales activées", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.PLAIN, base - 3));
        hint.setForeground(TEXT_MUTED);
        card.add(hint, c);

        GridBagConstraints rootC = new GridBagConstraints();
        rootC.weightx = rootC.weighty = 1.0;
        root.add(card, rootC);

        // Listeners Clavier
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnConnexion.doClick();
            }
        });

        setVisible(true);
    }

    // Méthode de secours double-chemin pour éviter que l'absence du fichier ne casse l'UI
    private JComponent getUirLogoComponent(int base) {
        String[] paths = {
                "/uir_logo.png",
                "/projetintegre/resources/uir_logo.png"
        };

        for (String path : paths) {
            try {
                java.net.URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    ImageIcon originalIcon = new ImageIcon(imgURL);
                    int targetHeight = 55;
                    int targetWidth = (originalIcon.getIconWidth() * targetHeight) / originalIcon.getIconHeight();
                    Image scaledImg = originalIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                    return new JLabel(new ImageIcon(scaledImg));
                }
            } catch (Exception ignored) {}
        }

        // Si l'image reste introuvable, on génère un bloc de secours propre pour ne rien faire crasher
        JLabel fallback = new JLabel("🎓 UIR ACCESSIBILITÉ", SwingConstants.CENTER);
        fallback.setFont(new Font("SansSerif", Font.BOLD, base + 4));
        fallback.setForeground(PURPLE_PRIMARY);
        return fallback;
    }

    private void styleMicroButton(JButton btn, int base) {
        btn.setBackground(FIELD_BG);
        btn.setForeground(ACCENT);
        btn.setFont(new Font("Dialog", Font.PLAIN, base + 5));
        btn.setBorder(new LineBorder(ACCENT, 2, true));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(50, 40));
    }

    private JLabel makeLabel(String text, int base) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, base));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    private void styleField(JTextField field, int base) {
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
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
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, base + 2));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, Math.max(50, base + 34)));
        return btn;
    }
}