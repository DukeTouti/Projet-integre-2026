package com.uir.projet.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

//C'est une fenetre donc on extend
public class LoginView extends JFrame {

    private JTextField txtIdentifiant; //
    private JPasswordField txtPassword;
    private JButton btnConnexion;

    /*
    Couleurs pour l'accessibilité (norme WCAG AAA)
    WCAG AAA : C'est une reference a l'accessibilite. (contraste entre le texte (clair)
    et le fond (sombre) est suffisamment high)
    */
    //Des couleurs constantes donc au final ce sont des private static final de type Color
    private static final Color BG_DARK       = new Color(13, 14, 16);
    private static final Color BG_CARD       = new Color(22, 24, 28);
    private static final Color ACCENT        = new Color(250, 204, 21);
    private static final Color ACCENT_HOVER  = new Color(234, 179, 8);
    private static final Color TEXT_PRIMARY  = new Color(240, 240, 245);
    private static final Color TEXT_MUTED    = new Color(140, 142, 150);
    private static final Color FIELD_BG      = new Color(30, 33, 38);
    private static final Color FIELD_BORDER  = new Color(55, 58, 65);
    private static final Color FOCUS_COLOR   = new Color(250, 204, 21);


    public LoginView() {
        //On garde tous compatible avec tout les ratio et resolution d'ecran
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int weight = Math.max(480, (int)(screen.width  * 0.26));
        int height = Math.max(560, (int)(screen.height * 0.62));

        setTitle("Connexion : Plateforme d'aide a l'accessibilite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Ferme quand on xlique sur la croix
        setSize(weight, height);//Propotion respecte par rapport a l'ecran
        int minW = (int)(screen.width * 0.20);
        int minH = (int)(screen.height * 0.45);
        setMinimumSize(new Dimension(minW, minH));//On ne peut pas dimensionne (reduire) en dessous e ca
        setLocationRelativeTo(null);//centrage automatique
        setResizable(false);//pas possible de l'etirer (conserve le login type ecran smartphone)
        getContentPane().setBackground(BG_DARK);//Couleur de fond

        int base = Math.max(13, (int)(screen.height * 0.016));

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_DARK);
        add(root);

        // Zone centrale (le vrai nom est carte centrale)
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();//On utilise la convention de nommage g2
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth()-1, getHeight()-1, 18, 18));
                g2.dispose();
            }
        };
        card.setOpaque(false);//opacity
        card.setBorder(new EmptyBorder(44, 44, 44, 44));//ne colle pas aux bords

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        // Logo
        c.gridy = 0; c.insets = new Insets(0, 0, 6, 0);
        JLabel icon = new JLabel("🔐", SwingConstants.CENTER);
        icon.setFont(new Font("Dialog", Font.PLAIN, base + 14));
        card.add(icon, c);

        // Titre
        c.gridy = 1; c.insets = new Insets(0, 0, 6, 0);
        JLabel title = new JLabel("Connexion", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, base + 10));
        title.setForeground(TEXT_PRIMARY);
        card.add(title, c);

        // Sous-titre
        c.gridy = 2; c.insets = new Insets(0, 0, 36, 0);
        JLabel sub = new JLabel("Accès à la plateforme UIR", SwingConstants.CENTER);
        sub.setFont(new Font("Dialog", Font.PLAIN, base - 1));
        sub.setForeground(TEXT_MUTED);
        card.add(sub, c);

        // ====  Zone Identifiant ====
        c.gridy = 3; c.insets = new Insets(0, 0, 6, 0);
        JLabel lblId = makeLabel("Identifiant", base);
        card.add(lblId, c);

        c.gridy = 4; c.insets = new Insets(0, 0, 20, 0);
        txtIdentifiant = new JTextField();
        styleField(txtIdentifiant, base, "Identifiant", lblId);
        card.add(txtIdentifiant, c);

        // ==== Zone Mot de passe ====
        c.gridy = 5; c.insets = new Insets(0, 0, 6, 0);
        JLabel lblPw = makeLabel("Mot de passe", base);
        card.add(lblPw, c);

        c.gridy = 6; c.insets = new Insets(0, 0, 36, 0);
        txtPassword = new JPasswordField();
        styleField(txtPassword, base, "Mot de passe", lblPw);
        card.add(txtPassword, c);

        // ====  Bouton ====
        c.gridy = 7; c.insets = new Insets(0, 0, 0, 0);
        btnConnexion = makeButton("Se connecter", base, "Valider les identifiants");
        card.add(btnConnexion, c);

        // Aide en bas de la zone (indication pour user)
        c.gridy = 8; c.insets = new Insets(20, 0, 0, 0);
        JLabel hint = new JLabel("Tab : naviguer | Entree : valider", SwingConstants.CENTER);
        hint.setFont(new Font("Dialog", Font.PLAIN, base - 3));
        hint.setForeground(TEXT_MUTED);
        card.add(hint, c);

        // ================EVENT DU CLAVIER ============

        /*
        KeyAdapter demande d'override qu'une methode, alros que KeyListener oblige a ovveride 3 methodes (keyTyped,keyPressed et keyReleased)
         */

        //saute automatiquement dans le champ du mot de passe SI L'utilisateur appuie sur la touche Entree alors qu'il ecrit son identifiant.
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnConnexion.doClick();
            }
        });

        //simule un clic sur le bouton de connexion SI L'utilisateur appuie sur la touche Entree alors qu'il ecrit son mot de passe.
        txtIdentifiant.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) txtPassword.requestFocus();
            }
        });



        GridBagConstraints rootC = new GridBagConstraints();
        rootC.weightx = rootC.weighty = 1.0;
        rootC.anchor = GridBagConstraints.CENTER;
        root.add(card, rootC);

        setVisible(true);
    }

    // ====  Helpers====

    private JLabel makeLabel(String text, int base) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Dialog", Font.BOLD, base));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    private void styleField(JTextField field, int base, String vocalName, JLabel label) {
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setFont(new Font("Monospaced", Font.PLAIN, base + 1));
        field.setPreferredSize(new Dimension(0, Math.max(46, base + 32)));

        // Accessibilité : Lie le label au champ et donne un nom au contexte
        label.setLabelFor(field);
        field.getAccessibleContext().setAccessibleName(vocalName);

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

    private JButton makeButton(String text, int base, String vocalDesc) {
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

        // Accessibilite en bas
        btn.getAccessibleContext().setAccessibleName(text);
        btn.getAccessibleContext().setAccessibleDescription(vocalDesc);

        btn.setFont(new Font("Dialog", Font.BOLD, base + 1));
        btn.setForeground(Color.BLACK);
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

}