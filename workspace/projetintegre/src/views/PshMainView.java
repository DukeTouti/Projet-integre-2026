package views;

import views.PSHjpanel.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
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
    private final Color BORDER_STANDARD = new Color(0x111827);  // Noir/Anthracite profond pour les contours et séparations

    // --- PALETTE THÈME ACCESSIBILITÉ (HAUT CONTRASTE SOMBRE) ---
    private final Color BG_ACCESSIBILITY = new Color(0x111827);   // Noir profond
    private final Color TEXT_ACCESSIBILITY = new Color(0xFDE047); // Jaune vif

    private boolean isAccessibilityMode = true;
    private JButton btnAccess;

    public PshMainView() {
        setTitle("UIR · PSH Platform (Espace Étudiant - Haute Visibilité)");
        setSize(1000, 700);
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

        cardPanel.add(new views.PSHjpanel.StudentDashboardPanel(), "DASHBOARD");
        cardPanel.add(new views.PSHjpanel.NewDossierPanel(), "NEW_DOSSIER");
        cardPanel.add(new views.PSHjpanel.MyDossiersPanel(), "MY_DOSSIERS");
        cardPanel.add(new projetintegre.views.PSHjpanel.ComplaintsPanel(), "COMPLAINTS");
        cardPanel.add(new projetintegre.views.PSHjpanel.StudentProfilePanel(), "PROFILE");

        contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.add(cardPanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);

        // Initialisation du thème par défaut
        toggleTheme();
        setVisible(true);
    }

    private JPanel createNavbar() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        logoLabel = new JLabel("🎓 UIR ACCESSIBILITÉ");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(logoLabel, BorderLayout.WEST);

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
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAccess.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAccess.setContentAreaFilled(false);
        btnAccess.setFocusPainted(false);
        btnAccess.addActionListener(e -> toggleTheme());
        rightPanel.add(btnAccess);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));

        String[] labels = {"📊 Tableau de bord", "📁 Nouvelle Demande", "🗂️ Mes Dossiers", "💬 Réclamations", "👤 Mon Profil"};
        String[] actions = {"DASHBOARD", "NEW_DOSSIER", "MY_DOSSIERS", "COMPLAINTS", "PROFILE"};

        for (int i = 0; i < labels.length; i++) {
            final String target = actions[i];
            JButton btn = new JButton(labels[i]) {
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
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> cardLayout.show(cardPanel, target));

            panel.add(btn);
            if (i < labels.length - 1) {
                panel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }
        return panel;
    }

    private void toggleTheme() {
        isAccessibilityMode = !isAccessibilityMode;

        Color currentBg = isAccessibilityMode ? BG_ACCESSIBILITY : BG_STANDARD;
        Color currentText = isAccessibilityMode ? TEXT_ACCESSIBILITY : TEXT_STANDARD;
        Color currentBorder = isAccessibilityMode ? TEXT_ACCESSIBILITY : BORDER_STANDARD;

        // La ligne de démarcation devient le Violet UIR en mode clair pour trancher magnifiquement
        Color separatorColor = isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY;

        getContentPane().setBackground(currentBg);

        // Application de la bordure de séparation nette (2px d'épaisseur)
        if (contentWrapper != null) {
            contentWrapper.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(2, 2, 0, 0, separatorColor),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        }

        applyThemeRecursively(this, currentBg, currentText, currentBorder);
        repaint();
    }

    private void applyThemeRecursively(Component comp, Color bg, Color text, Color border) {
        if (comp instanceof JPanel) {
            comp.setBackground(bg);
        } else if (comp instanceof JLabel) {
            comp.setForeground(text);
        } else if (comp instanceof JRadioButton) {
            comp.setForeground(text);
        } else if (comp instanceof JButton) {
            comp.setBackground(bg);
            comp.setForeground(text);
            JButton b = (JButton) comp;
            if (b == btnAccess) {
                b.setBorder(new RoundedBorder(12, border));
            } else if (b.getParent() == sidebar) {
                b.setBorder(new RoundedBorder(12, border));
            } else {
                b.setBorder(new RoundedBorder(15, border));
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
            g2.setStroke(new BasicStroke(1.5f)); // Épaisseur de trait augmentée pour accentuer les contours
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PshMainView();
        });
    }
}