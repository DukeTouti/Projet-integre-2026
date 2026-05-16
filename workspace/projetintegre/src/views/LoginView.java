package views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginView extends JFrame {

    private JTextField txtIdentifiant;
    private JPasswordField txtPassword;
    private JButton btnConnexion;

    // ===== PALETTE DE COULEURS HAUTE VISIBILITE =====
    private static final Color BG_DARK       = new Color(13, 14, 16);
    private static final Color BG_CARD       = new Color(22, 24, 28);
    private static final Color ACCENT        = new Color(250, 204, 21);
    private static final Color ACCENT_HOVER  = new Color(234, 179, 8);
    private static final Color TEXT_PRIMARY  = new Color(240, 240, 245);
    private static final Color TEXT_MUTED    = new Color(140, 142, 150);
    private static final Color FIELD_BG      = new Color(30, 33, 38);
    private static final Color FIELD_BORDER  = new Color(55, 58, 65);
    private static final Color FOCUS_COLOR   = new Color(250, 204, 21);
    private static final Color ERROR_COLOR   = new Color(239, 68, 68);

    public LoginView() {
        // ===== CONFIGURATION DE LA FENETRE =====
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.max(480, (int)(screen.width  * 0.26));
        int h = Math.max(560, (int)(screen.height * 0.62));

        setTitle("Connexion - UIR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(w, h);
        int minW = (int)(screen.width * 0.22); // Environ 22% de la largeur
        int minH = (int)(screen.height * 0.55); // Environ 55% de la hauteur
        setMinimumSize(new Dimension(minW, minH));
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        int base = Math.max(13, (int)(screen.height * 0.016));

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_DARK);
        add(root);

        // ===== CARTE CENTRALE DESIGN =====
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth()-1, getHeight()-1, 18, 18));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(44, 44, 44, 44));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        // ===== LOGO ET TITRE =====
        c.gridy = 0;
        c.insets = new Insets(0, 0, 6, 0);
        JLabel icon = new JLabel("🔐", SwingConstants.CENTER);
        icon.setFont(new Font("Dialog", Font.PLAIN, base + 14));
        card.add(icon, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 6, 0);
        JLabel title = new JLabel("Connexion", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, base + 10));
        title.setForeground(TEXT_PRIMARY);
        card.add(title, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 36, 0);
        JLabel sub = new JLabel("Acces a la plateforme UIR", SwingConstants.CENTER);
        sub.setFont(new Font("Dialog", Font.PLAIN, base - 1));
        sub.setForeground(TEXT_MUTED);
        card.add(sub, c);

        // ===== CHAMPS DE SAISIE =====
        c.gridy = 3;
        c.insets = new Insets(0, 0, 6, 0);
        card.add(makeLabel("Identifiant", base), c);

        c.gridy = 4;
        c.insets = new Insets(0, 0, 20, 0);
        txtIdentifiant = new JTextField();
        styleField(txtIdentifiant, base);
        card.add(txtIdentifiant, c);

        c.gridy = 5;
        c.insets = new Insets(0, 0, 6, 0);
        card.add(makeLabel("Mot de passe", base), c);

        c.gridy = 6;
        c.insets = new Insets(0, 0, 36, 0);
        txtPassword = new JPasswordField();
        styleField(txtPassword, base);
        card.add(txtPassword, c);

        // ===== BOUTON D ACTION =====
        c.gridy = 7;
        c.insets = new Insets(0, 0, 0, 0);
        btnConnexion = makeButton("Se connecter", base);
        card.add(btnConnexion, c);

        // ===== FOOTER ET ACCESSIBILITE =====
        c.gridy = 8;
        c.insets = new Insets(20, 0, 0, 0);
        JLabel hint = new JLabel("Tab pour naviguer - Entree pour valider", SwingConstants.CENTER);
        hint.setFont(new Font("Dialog", Font.PLAIN, base - 3));
        hint.setForeground(TEXT_MUTED);
        card.add(hint, c);

        // ===== GESTION DES EVENEMENTS CLAVIER =====
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnConnexion.doClick();
            }
        });
        txtIdentifiant.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) txtPassword.requestFocus();
            }
        });

        GridBagConstraints rootC = new GridBagConstraints();
        rootC.weightx = rootC.weighty = 1.0;
        rootC.fill = GridBagConstraints.NONE;
        rootC.anchor = GridBagConstraints.CENTER;
        root.add(card, rootC);

        setVisible(true);
    }

    // ===== METHODES UTILITAIRES DE STYLE =====

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
                        new LineBorder(FOCUS_COLOR, 2, true),
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

    // ===== POINT D ENTREE PRINCIPAL =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}