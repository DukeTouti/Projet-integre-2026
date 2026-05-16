package views;

import views.PSHjpanel.*;
import javax.swing.*;
import java.awt.*;

public class PshMainView extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel navbar;
    private JPanel sidebar;
    private JLabel logoLabel;
    private JLabel userLabel;

    // --- PALETTE THÈME STANDARD ---
    public static final Color PURPLE_PRIMARY = new Color(0x3C3489);
    private final Color BG_STANDARD = Color.WHITE;
    private final Color TEXT_STANDARD = new Color(0x1F2937);
    private final Color BORDER_STANDARD = new Color(0xE5E7EB);

    // --- PALETTE THÈME ACCESSIBILITÉ (WCAG AAA) ---
    private final Color BG_ACCESSIBILITY = new Color(0x111827);   // Noir profond
    private final Color TEXT_ACCESSIBILITY = new Color(0xFDE047); // Jaune vif

    // Activé par défaut dès le démarrage
    private boolean isAccessibilityMode = true;
    private JButton btnAccess;

    public PshMainView() {
        setTitle("UIR · PSH Platform (Espace Étudiant - Haute Visibilité)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Top Navbar
        navbar = createNavbar();
        add(navbar, BorderLayout.NORTH);

        // 2. Sidebar Gauche
        sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 3. Zone Centrale Dynamique (CardLayout)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_ACCESSIBILITY);

        // Ajout des cartes issues de ton package PSHjpanel
        cardPanel.add(new StudentDashboardPanel(), "DASHBOARD");
        cardPanel.add(new NewDossierPanel(), "NEW_DOSSIER");
        cardPanel.add(new ComplaintsPanel(), "COMPLAINTS");
        cardPanel.add(new MyDossiersPanel(), "MY_DOSSIERS");
        cardPanel.add(new StudentProfilePanel(), "PROFILE");

        add(cardPanel, BorderLayout.CENTER);

        // Configuration initiale de l'affichage (Force le mode ON au démarrage)
        updateApplicationTheme(true);

        setVisible(true);
    }

    private JPanel createNavbar() {
        navbar = new JPanel(new BorderLayout());
        navbar.setBackground(BG_ACCESSIBILITY);
        navbar.setPreferredSize(new Dimension(1000, 50));

        logoLabel = new JLabel(" 🎙️ UIR · PSH Platform", SwingConstants.LEFT);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoLabel.setForeground(TEXT_ACCESSIBILITY);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightPanel.setOpaque(false);

        // Bouton d'accessibilité (Configuré en ON par défaut)
        btnAccess = new JButton("👁️ Mode Accessibilité (AAA) : ON");
        btnAccess.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAccess.setBackground(Color.BLACK);
        btnAccess.setForeground(TEXT_ACCESSIBILITY);
        btnAccess.setBorder(BorderFactory.createLineBorder(TEXT_ACCESSIBILITY, 2));

        btnAccess.addActionListener(e -> {
            isAccessibilityMode = !isAccessibilityMode;
            updateApplicationTheme(false);
        });

        userLabel = new JLabel("Ilyas Benali · PSH  [IB] ");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userLabel.setForeground(TEXT_ACCESSIBILITY);

        rightPanel.add(btnAccess);
        rightPanel.add(userLabel);

        navbar.add(logoLabel, BorderLayout.WEST);
        navbar.add(rightPanel, BorderLayout.EAST);
        return navbar;
    }

    private JPanel createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_ACCESSIBILITY);

        JButton btnDashboard = createSidebarButton("📊  Tableau de bord");
        JButton btnNewDossier = createSidebarButton("📁  Nouveau dossier");
        JButton btnMyDossiers = createSidebarButton("🗂️  Mes dossiers");
        JButton btnComplaints = createSidebarButton("💬  Réclamations");
        JButton btnProfile = createSidebarButton("👤  Mon profil");

        btnDashboard.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
        btnNewDossier.addActionListener(e -> cardLayout.show(cardPanel, "NEW_DOSSIER"));
        btnComplaints.addActionListener(e -> cardLayout.show(cardPanel, "COMPLAINTS"));
        btnMyDossiers.addActionListener(e -> cardLayout.show(cardPanel, "MY_DOSSIERS"));
        btnProfile.addActionListener(e -> cardLayout.show(cardPanel, "PROFILE"));

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNewDossier);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnMyDossiers);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnComplaints);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnProfile);

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(TEXT_ACCESSIBILITY);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    // --- LOGIQUE DE GESTION DU THÈME ET DES CONTOURS ---
    private void updateApplicationTheme(boolean isInitialSetup) {
        Color currentBg = isAccessibilityMode ? BG_ACCESSIBILITY : BG_STANDARD;
        Color currentText = isAccessibilityMode ? TEXT_ACCESSIBILITY : TEXT_STANDARD;
        Color currentBorder = isAccessibilityMode ? TEXT_ACCESSIBILITY : BORDER_STANDARD;

        // 1. Recolorisation et renforcement visuel des contours des conteneurs maîtres
        navbar.setBackground(currentBg);
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, currentBorder)); // Ligne basse 2px

        sidebar.setBackground(currentBg);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, currentBorder), // Ligne droite 2px
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        cardPanel.setBackground(currentBg);

        // 2. Gestion des éléments de la Navbar
        logoLabel.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY);
        userLabel.setForeground(currentText);

        // 3. Mise à jour de l'état graphique du bouton de switch
        if (!isInitialSetup && btnAccess != null) {
            if (isAccessibilityMode) {
                btnAccess.setText("👁️ Mode Accessibilité (AAA) : ON");
                btnAccess.setBackground(Color.BLACK);
                btnAccess.setForeground(TEXT_ACCESSIBILITY);
                btnAccess.setBorder(BorderFactory.createLineBorder(TEXT_ACCESSIBILITY, 2));
            } else {
                btnAccess.setText("👁️ Mode Accessibilité (AAA) : OFF");
                btnAccess.setBackground(TEXT_ACCESSIBILITY);
                btnAccess.setForeground(Color.BLACK);
                btnAccess.setBorder(UIManager.getBorder("Button.border"));
            }
        }

        // 4. Lancement du traitement récursif sur l'arborescence des panels
        applyThemeRecursively(this, currentBg, currentText, currentBorder);

        // 5. Demande de redessin complet de l'UI
        SwingUtilities.updateComponentTreeUI(this);
    }

    // --- NETTOYAGE EN PROFONDEUR DE L'UI SWING ---
    private void applyThemeRecursively(Component comp, Color bg, Color text, Color border) {
        // 1. Gestion des conteneurs, labels, et zones de saisie de texte
        if (comp instanceof JPanel || comp instanceof JLabel || comp instanceof JTextArea || comp instanceof JTextField) {
            comp.setBackground(bg);
            comp.setForeground(text);

            if (comp instanceof JTextArea || comp instanceof JTextField) {
                ((JComponent) comp).setBorder(BorderFactory.createLineBorder(isAccessibilityMode ? text : border, 1));
                if (comp instanceof JTextArea) ((JTextArea) comp).setCaretColor(text);
                if (comp instanceof JTextField) ((JTextField) comp).setCaretColor(text);
            }
        }

        // 2. Nettoyage complet des listes déroulantes (JComboBox)
        if (comp instanceof JComboBox) {
            comp.setBackground(bg);
            comp.setForeground(text);
            ((JComboBox<?>) comp).setBorder(BorderFactory.createLineBorder(isAccessibilityMode ? text : border, 1));

            Object renderer = ((JComboBox<?>) comp).getRenderer();
            if (renderer instanceof JComponent) {
                ((JComponent) renderer).setBackground(bg);
                ((JComponent) renderer).setForeground(text);
            }
        }

        // 3. Encadrement et éradication des dégradés de l'OS sur TOUS les boutons
        if (comp instanceof JButton) {
            if (comp != btnAccess) {
                comp.setForeground(text);

                if (isAccessibilityMode) {
                    comp.setBackground(bg);
                    ((JButton) comp).setContentAreaFilled(false); // Supprime l'effet gris/blanc natif
                    ((JButton) comp).setOpaque(true);
                    ((JButton) comp).setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(text, 2), // Bordure robuste 2px
                            BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                } else {
                    comp.setBackground(UIManager.getColor("Button.background"));
                    comp.setForeground(TEXT_STANDARD);
                    ((JButton) comp).setContentAreaFilled(true);
                    ((JButton) comp).setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(border, 1),
                            BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
            }
        }

        // 4. Propagation récursive aux enfants
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyThemeRecursively(child, bg, text, border);
            }
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