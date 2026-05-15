package views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class LoginView extends JFrame {

    private JTextField txtIdentifiant;
    private JPasswordField txtPassword;
    private JButton btnConnexion;
    private JButton btnTTS;

    private static final Color BG_DARK      = new Color(13, 14, 16);
    private static final Color BG_CARD      = new Color(22, 24, 28);
    private static final Color ACCENT       = new Color(250, 204, 21);
    private static final Color ACCENT_HOVER = new Color(234, 179, 8);
    private static final Color TEXT_PRIMARY = new Color(240, 240, 245);
    private static final Color TEXT_MUTED   = new Color(140, 142, 150);
    private static final Color FIELD_BG     = new Color(30, 33, 38);
    private static final Color FIELD_BORDER = new Color(55, 58, 65);

    private boolean ttsEnabled = true;

    public LoginView() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.max(480, (int)(screen.width  * 0.26));
        int h = Math.max(560, (int)(screen.height * 0.62));
        int base = Math.max(13, (int)(screen.height * 0.016));

        setTitle("Connexion - UIR Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(w, h);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_DARK);
        add(root);

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(44, 44, 44, 44));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        // ===== TOGGLE AUDIO =====
        btnTTS = new JButton("🔊");
        styleToggleButton(btnTTS, base);
        btnTTS.addActionListener(e -> {
            ttsEnabled = !ttsEnabled;
            btnTTS.setText(ttsEnabled ? "🔊" : "🔇");
        });
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(-30, 0, 10, -30);
        card.add(btnTTS, c);
        c.anchor = GridBagConstraints.CENTER;

        // ===== TITRE =====
        c.gridy = 1; c.insets = new Insets(0, 0, 10, 0);
        JLabel title = new JLabel("Connexion", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, base + 10));
        title.setForeground(TEXT_PRIMARY);
        card.add(title, c);

        // ===== IDENTIFIANT =====
        c.gridy = 2; c.insets = new Insets(0, 0, 5, 0);
        card.add(makeLabel("Identifiant", base), c);

        c.gridy = 3; c.insets = new Insets(0, 0, 20, 0);
        txtIdentifiant = new JTextField();
        styleField(txtIdentifiant, base);
        txtIdentifiant.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                playSound("Zone_de_saisi_de_l'identifiant.mp3");
            }
        });
        txtIdentifiant.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                playSound("Zone_de_saisi_de_l'identifiant.mp3");
            }
        });
        card.add(txtIdentifiant, c);

        // ===== MOT DE PASSE =====
        c.gridy = 4; c.insets = new Insets(0, 0, 5, 0);
        card.add(makeLabel("Mot de passe", base), c);

        c.gridy = 5; c.insets = new Insets(0, 0, 30, 0);
        txtPassword = new JPasswordField();
        styleField(txtPassword, base);
        txtPassword.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                playSound("Zone_de_saisi_du_mot_de_passe.mp3");
            }
        });
        txtPassword.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                playSound("Zone_de_saisi_du_mot_de_passe.mp3");
            }
        });
        card.add(txtPassword, c);

        // ===== BOUTON CONNEXION =====
        c.gridy = 6; c.insets = new Insets(0, 0, 0, 0);
        btnConnexion = makeButton("Se connecter", base);
        btnConnexion.addActionListener(e -> playSound("Boutton_se_connecter.mp3"));
        btnConnexion.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                playSound("Boutton_se_connecter.mp3");
            }
        });
        card.add(btnConnexion, c);

        root.add(card);

        // ===== SON D OUVERTURE =====
        addWindowListener(new WindowAdapter() {
            @Override public void windowOpened(WindowEvent e) {
                playSound("Bienvenue_sur_l'ecran de_connexion.mp3");
            }
        });

        // ===== NAVIGATION CLAVIER =====
        txtIdentifiant.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) txtPassword.requestFocus();
            }
        });
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnConnexion.doClick();
            }
        });

        setVisible(true);
    }

    private Process currentAudio = null;

    private void playSound(String fileName) {
        if (!ttsEnabled) return;
        new Thread(() -> {
            try {
                // Couper l'audio précédent
                if (currentAudio != null && currentAudio.isAlive()) {
                    currentAudio.destroy();
                }

                String base = System.getProperty("user.dir");
                String os = System.getProperty("os.name").toLowerCase();
                String path;

                if (os.contains("win")) {
                    path = base + "/src/projetintegre/ressources/audiologinwindows/" + fileName.replace(".mp3", ".wav");
                } else {
                    path = base + "/src/projetintegre/ressources/audiologin/" + fileName;
                }

                java.io.File f = new java.io.File(path);
                if (!f.exists()) {
                    System.err.println("[AUDIO] Fichier introuvable : " + path);
                    return;
                }

                if (os.contains("linux")) {
                    currentAudio = new ProcessBuilder(
                            new String[]{"mpg123", "-q", path}
                    ).inheritIO().start();
                    currentAudio.waitFor();
                } else if (os.contains("win")) {
                    currentAudio = new ProcessBuilder("powershell", "-c",
                            "(New-Object Media.SoundPlayer '" + path + "').PlaySync()"
                    ).start();
                    currentAudio.waitFor();
                } else {
                    currentAudio = new ProcessBuilder("afplay", path).start();
                    currentAudio.waitFor();
                }
            } catch (Exception e) {
                System.err.println("Erreur audio : " + e.getMessage());
            }
        }).start();
    }

    private JLabel makeLabel(String text, int base) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Dialog", Font.BOLD, base));
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
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT, 2, true),
                        new EmptyBorder(9, 13, 9, 13)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(FIELD_BORDER, 1, true),
                        new EmptyBorder(10, 14, 10, 14)
                ));
            }
        });
    }

    private JButton makeButton(String text, int base) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? ACCENT_HOVER :
                        getModel().isRollover() ? ACCENT_HOVER : ACCENT);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Dialog", Font.BOLD, base + 1));
        btn.setForeground(Color.BLACK);
        btn.setBackground(ACCENT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, Math.max(50, base + 34)));
        btn.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                btn.setBorder(new LineBorder(Color.WHITE, 2, true));
            }
            @Override public void focusLost(FocusEvent e) {
                btn.setBorder(null);
            }
        });
        return btn;
    }

    private void styleToggleButton(JButton btn, int base) {
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(ACCENT);
        btn.setFont(new Font("Dialog", Font.PLAIN, base + 4));
    }

    public static void main(String[] args) {

        // Verif et install mpg123 si Linux et conv en WAV pour windows
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) {
            try {
                // Vérifie si mpg123 est déjà installé
                Process check = new ProcessBuilder("which", "mpg123").start();
                check.waitFor();
                if (check.exitValue() != 0) {
                    System.out.println("[AUDIO] Installation de mpg123...");
                    Process install = new ProcessBuilder(
                            "bash", "-c", "sudo apt install -y mpg123"
                    ).inheritIO().start();
                    install.waitFor();
                    System.out.println("[AUDIO] mpg123 installé.");
                }
            } catch (Exception e) {
                System.err.println("[AUDIO] Impossible d'installer mpg123 : " + e.getMessage());
            }
        }
        SwingUtilities.invokeLater(LoginView::new);
    }
}