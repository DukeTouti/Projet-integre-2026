package views;

import views.PSHjpanel.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PshMainView extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel navbar;
    private JPanel sidebar;
    private JLabel logoLabel;
    private JLabel userLabel;
    private JPanel contentWrapper;

    // --- CORRECTION DU THÈME STANDARD (HAUT CONTRASTE CLAIR) ---
    public static final Color PURPLE_PRIMARY = new Color(0x3C3489);
    private final Color BG_STANDARD = new Color(0xF9FAFB);      // Gris ultra-clair pour le relief
    private final Color TEXT_STANDARD = new Color(0x000000);    // Noir absolu pour le texte
    private final Color BORDER_STANDARD = new Color(0x111827);  // Contour sombre
    private final Color BTN_BG_STANDARD = new Color(0xE5E7EB);  // Fond bouton standard sécurisé

    // --- PALETTE THÈME ACCESSIBILITÉ (HAUT CONTRASTE SOMBRE) ---
    private final Color BG_ACCESSIBILITY = new Color(0x111827);   // Noir profond
    private final Color TEXT_ACCESSIBILITY = new Color(0xFDE047); // Jaune vif

    private boolean isAccessibilityMode = false;
    private JButton btnAccess;

    public PshMainView() {
        setTitle("UIR · PSH Platform (Espace Étudiant - Haute Visibilité)");
        setSize(1050, 750); // 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        navbar = createNavbar();
        add(navbar, BorderLayout.NORTH);

        sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Chargement sécurisé des sous-panneaux
        try {
            cardPanel.add(new views.PSHjpanel.StudentDashboardPanel(), "DASHBOARD");
            cardPanel.add(new views.PSHjpanel.NewDossierPanel(), "NEW_DOSSIER");
            cardPanel.add(new views.PSHjpanel.MyDossiersPanel(), "MY_DOSSIERS");
            cardPanel.add(new views.PSHjpanel.ComplaintsPanel(), "COMPLAINTS");
            cardPanel.add(new views.PSHjpanel.StudentProfilePanel(), "PROFILE");
        } catch (Exception e) {
            System.err.println("Note : Certains sous-panels graphiques ne sont pas encore instanciés : " + e.getMessage());
        }

        contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.add(cardPanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);

        // Application initiale du thème
        toggleTheme();
        
        setVisible(true);
    }

    private JPanel createNavbar() {
        navbar = new JPanel(new BorderLayout(20, 0));
        navbar.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        logoLabel = new JLabel("🎓 UIR ACCESSIBILITÉ");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        navbar.add(logoLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        userLabel = new JLabel("👤 Étudiant : Ilyas Jidal (3A Cybersécurité)");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        rightPanel.add(userLabel);

        btnAccess = new JButton("👁️ Mode Contraste Élevé") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isAccessibilityMode) {
                    g2.setColor(getModel().isRollover() ? new Color(0x1F2937) : Color.BLACK);
                } else {
                    g2.setColor(getModel().isRollover() ? new Color(0xD1D5DB) : Color.WHITE);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAccess.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAccess.setContentAreaFilled(false);
        btnAccess.setFocusPainted(false);
        btnAccess.setBorderPainted(false);
        btnAccess.addActionListener(e -> toggleTheme());
        rightPanel.add(btnAccess);

        navbar.add(rightPanel, BorderLayout.EAST);
        return navbar;
    }

    private JPanel createSidebar() {
        sidebar = new JPanel();
        
        // Liste enrichie avec l'action de déconnexion
        String[] labels = {"📊 Tableau de bord", "📁 Nouvelle Demande", "🗂️ Mes Dossiers", "💬 Réclamations", "👤 Mon Profil", "🚪 Se déconnecter"};
        String[] actions = {"DASHBOARD", "NEW_DOSSIER", "MY_DOSSIERS", "COMPLAINTS", "PROFILE", "LOGOUT"};

        // Utilisation de GridLayout pour forcer le partage équitable de l'espace vertical disponible
        sidebar.setLayout(new GridLayout(labels.length, 1, 0, 12));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(230, 0)); // Fixe la largeur de la sidebar

        for (int i = 0; i < labels.length; i++) {
            final String target = actions[i];
            JButton btn = new JButton(labels[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (isAccessibilityMode) {
                        g2.setColor(getModel().isRollover() ? new Color(0x1F2937) : Color.BLACK);
                    } else {
                        g2.setColor(getModel().isRollover() ? new Color(0xD1D5DB) : Color.WHITE);
                    }
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            
            // Gestion spécifique pour le bouton se déconnecter
            if ("LOGOUT".equals(target)) {
                btn.addActionListener(e -> {
                    int option = JOptionPane.showConfirmDialog(
                            this, 
                            "Voulez-vous vraiment vous déconnecter de la plateforme PSH ?", 
                            "Confirmation de déconnexion", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (option == JOptionPane.YES_OPTION) {
                        this.dispose(); // Ferme la vue principale
                        new LoginView(); // Redirection vers la page de login
                    }
                });
            } else {
                btn.addActionListener(e -> cardLayout.show(cardPanel, target));
            }

            sidebar.add(btn);
        }
        return sidebar;
    }

    private void toggleTheme() {
        isAccessibilityMode = !isAccessibilityMode;

        Color currentBg = isAccessibilityMode ? BG_ACCESSIBILITY : BG_STANDARD;
        Color currentText = isAccessibilityMode ? TEXT_ACCESSIBILITY : TEXT_STANDARD;
        Color currentBorder = isAccessibilityMode ? TEXT_ACCESSIBILITY : BORDER_STANDARD;
        Color separatorColor = isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY;

        getContentPane().setBackground(currentBg);

        if (navbar != null) navbar.setBackground(currentBg);
        if (sidebar != null) sidebar.setBackground(currentBg);

        if (contentWrapper != null) {
            contentWrapper.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(2, 2, 0, 0, separatorColor),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        }

        if (btnAccess != null) {
            btnAccess.setText(isAccessibilityMode ? "👁️ Mode Contraste Élevé : ON" : "👁️ Mode Contraste Élevé : OFF");
        }

        applyThemeRecursively(this, currentBg, currentText, currentBorder);
        
        this.revalidate();
        this.repaint();
    }

    private void applyThemeRecursively(Component comp, Color bg, Color text, Color border) {
        if (comp instanceof JPanel || comp instanceof JLabel || comp instanceof JRadioButton || comp instanceof JCheckBox) {
            comp.setBackground(bg);
            comp.setForeground(text);
        }

        if (comp instanceof JTextArea || comp instanceof JTextField) {
            comp.setBackground(bg);
            comp.setForeground(text);
            ((JComponent) comp).setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(10, isAccessibilityMode ? text : border),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            if (comp instanceof JTextArea) ((JTextArea) comp).setCaretColor(text);
            if (comp instanceof JTextField) ((JTextField) comp).setCaretColor(text);
        }

        if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            table.setBackground(bg);
            table.setForeground(text);
            table.setGridColor(border);
            table.setSelectionBackground(isAccessibilityMode ? new Color(0x374151) : PURPLE_PRIMARY);
            table.setSelectionForeground(isAccessibilityMode ? text : Color.WHITE);

            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.setBackground(isAccessibilityMode ? new Color(0x1F2937) : border);
                header.setForeground(text);
            }
        }

        if (comp instanceof JScrollPane) {
            ((JScrollPane) comp).setBorder(new RoundedBorder(12, border));
            ((JScrollPane) comp).getViewport().setBackground(bg);
        }

        if (comp instanceof JComboBox) {
            comp.setBackground(bg);
            comp.setForeground(text);
            ((JComboBox<?>) comp).setBorder(new RoundedBorder(10, isAccessibilityMode ? text : border));

            Object renderer = ((JComboBox<?>) comp).getRenderer();
            if (renderer instanceof JComponent) {
                ((JComponent) renderer).setBackground(bg);
                ((JComponent) renderer).setForeground(text);
            }
        }

        if (comp instanceof JButton) {
            JButton b = (JButton) comp;
            b.setForeground(text);
            
            if (b == btnAccess || b.getParent() == sidebar) {
                b.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(12, border),
                        BorderFactory.createEmptyBorder(0, 15, 0, 0)
                ));
            } else {
                b.setContentAreaFilled(false);
                b.setOpaque(false);
                b.setBorderPainted(false);
                b.setFocusPainted(false);

                b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                    @Override
                    public void paint(Graphics g, JComponent jc) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (isAccessibilityMode) {
                            g2.setColor(b.getModel().isRollover() ? new Color(0x1F2937) : Color.BLACK);
                            g2.fill(new RoundRectangle2D.Float(0, 0, b.getWidth(), b.getHeight(), 12, 12));
                            g2.setColor(text);
                            g2.setStroke(new BasicStroke(2.0f));
                            g2.draw(new RoundRectangle2D.Float(1, 1, b.getWidth() - 2, b.getHeight() - 2, 12, 12));
                        } else {
                            g2.setColor(b.getModel().isRollover() ? BTN_BG_STANDARD.darker() : BTN_BG_STANDARD);
                            g2.fill(new RoundRectangle2D.Float(0, 0, b.getWidth(), b.getHeight(), 12, 12));
                            g2.setColor(border);
                            g2.setStroke(new BasicStroke(1.0f));
                            g2.draw(new RoundRectangle2D.Float(0, 0, b.getWidth() - 1, b.getHeight() - 1, 12, 12));
                        }
                        g2.dispose();
                        super.paint(g, jc);
                    }
                });
                b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            }
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyThemeRecursively(child, bg, text, border);
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
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Reste silencieux si y'as échec
            }
            new PshMainView();
        });
    }
}