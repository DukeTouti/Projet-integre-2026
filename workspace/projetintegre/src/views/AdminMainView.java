package views;

import views.ADMINjpanel.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminMainView extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel navbar;
    private JPanel sidebar;
    private JLabel logoLabel;
    private JLabel userLabel;

    public static final Color PURPLE_PRIMARY = new Color(0x3C3489);
    private final Color BG_STANDARD = Color.WHITE;
    private final Color TEXT_STANDARD = new Color(0x1F2937);
    private final Color BORDER_STANDARD = new Color(0xE5E7EB);

    private final Color BG_ACCESSIBILITY = new Color(0x111827);
    private final Color TEXT_ACCESSIBILITY = new Color(0xFDE047);

    private boolean isAccessibilityMode = true;
    private JButton btnAccess;

    public AdminMainView() {
        setTitle("UIR · PSH Platform (Console Administrateur - Haute Visibilité)");
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
        cardPanel.setBackground(BG_ACCESSIBILITY);

        cardPanel.add(new AdminDashboardPanel(), "DASHBOARD");
        cardPanel.add(new AdminDossiersListPanel(), "DOSSIERS_LIST");
        cardPanel.add(new AdminComplaintsPanel(), "COMPLAINTS");
        cardPanel.add(new AdminStatsPanel(), "STATS");
        cardPanel.add(new AdminArchivesPanel(), "ARCHIVES");

        add(cardPanel, BorderLayout.CENTER);

        updateApplicationTheme(true);

        setVisible(true);
    }

    private JPanel createNavbar() {
        navbar = new JPanel(new BorderLayout());
        navbar.setBackground(BG_ACCESSIBILITY);
        navbar.setPreferredSize(new Dimension(1000, 50));

        logoLabel = new JLabel(" 🎙️ UIR · PSH Platform (Console Admin)", SwingConstants.LEFT);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoLabel.setForeground(TEXT_ACCESSIBILITY);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightPanel.setOpaque(false);

        btnAccess = new JButton("Mode Accessibilité (AAA) : ON") {
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

        btnAccess.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAccess.setBackground(Color.BLACK);
        btnAccess.setForeground(TEXT_ACCESSIBILITY);
        btnAccess.setContentAreaFilled(false);
        btnAccess.setFocusPainted(false);

        btnAccess.addActionListener(e -> {
            isAccessibilityMode = !isAccessibilityMode;
            updateApplicationTheme(false);
        });

        userLabel = new JLabel("Scolarité Centrale · Admin [Dept: Direction] ");
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

        JButton btnDashboard = createSidebarButton("📊  Vue d'ensemble");
        JButton btnDossiers = createSidebarButton("📁  Traiter les dossiers");
        JButton btnComplaints = createSidebarButton("💬  Gérer réclamations");
        JButton btnStats = createSidebarButton("📈  Statistiques globales");
        JButton btnArchives = createSidebarButton("🗄️  Archives du système");

        btnDashboard.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
        btnDossiers.addActionListener(e -> cardLayout.show(cardPanel, "DOSSIERS_LIST"));
        btnComplaints.addActionListener(e -> cardLayout.show(cardPanel, "COMPLAINTS"));
        btnStats.addActionListener(e -> cardLayout.show(cardPanel, "STATS"));
        btnArchives.addActionListener(e -> cardLayout.show(cardPanel, "ARCHIVES"));

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnDossiers);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnComplaints);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnStats);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnArchives);

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createSidebarButton(String text) {
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
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(TEXT_ACCESSIBILITY);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    private void updateApplicationTheme(boolean isInitialSetup) {
        Color currentBg = isAccessibilityMode ? BG_ACCESSIBILITY : BG_STANDARD;
        Color currentText = isAccessibilityMode ? TEXT_ACCESSIBILITY : TEXT_STANDARD;
        Color currentBorder = isAccessibilityMode ? TEXT_ACCESSIBILITY : BORDER_STANDARD;

        navbar.setBackground(currentBg);
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, currentBorder));

        sidebar.setBackground(currentBg);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, currentBorder),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        cardPanel.setBackground(currentBg);

        logoLabel.setForeground(isAccessibilityMode ? TEXT_ACCESSIBILITY : PURPLE_PRIMARY);
        userLabel.setForeground(currentText);

        if (!isInitialSetup && btnAccess != null) {
            if (isAccessibilityMode) {
                btnAccess.setText("👁️ Mode Accessibilité (AAA) : ON");
                btnAccess.setBackground(Color.BLACK);
                btnAccess.setForeground(TEXT_ACCESSIBILITY);
                btnAccess.setBorder(new RoundedBorder(12, TEXT_ACCESSIBILITY));
            } else {
                btnAccess.setText("👁️ Mode Accessibilité (AAA) : OFF");
                btnAccess.setBackground(TEXT_ACCESSIBILITY);
                btnAccess.setForeground(Color.BLACK);
                btnAccess.setBorder(new RoundedBorder(12, BORDER_STANDARD));
            }
        } else if (btnAccess != null) {
            btnAccess.setBorder(new RoundedBorder(12, TEXT_ACCESSIBILITY));
        }

        applyThemeRecursively(this, currentBg, currentText, currentBorder);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void applyThemeRecursively(Component comp, Color bg, Color text, Color border) {
        if (comp instanceof JPanel || comp instanceof JLabel) {
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
            table.setSelectionBackground(isAccessibilityMode ? new Color(0x374151) : UIManager.getColor("Table.selectionBackground"));
            table.setSelectionForeground(text);

            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.setBackground(isAccessibilityMode ? new Color(0x1F2937) : UIManager.getColor("TableHeader.background"));
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
            if (comp != btnAccess && comp.getParent() != sidebar) {
                comp.setForeground(text);
                JButton btn = (JButton) comp;
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);

                btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                    @Override
                    public void paint(Graphics g, JComponent c) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (isAccessibilityMode) {
                            g2.setColor(btn.getModel().isRollover() ? bg.brighter() : bg);
                            g2.fill(new RoundRectangle2D.Float(0, 0, btn.getWidth(), btn.getHeight(), 12, 12));
                            g2.setColor(text);
                            g2.setStroke(new BasicStroke(2.0f));
                            g2.draw(new RoundRectangle2D.Float(1, 1, btn.getWidth() - 2, btn.getHeight() - 2, 12, 12));
                        } else {
                            g2.setColor(btn.getModel().isRollover() ? UIManager.getColor("Button.background").darker() : UIManager.getColor("Button.background"));
                            g2.fill(new RoundRectangle2D.Float(0, 0, btn.getWidth(), btn.getHeight(), 12, 12));
                            g2.setColor(border);
                            g2.setStroke(new BasicStroke(1.0f));
                            g2.draw(new RoundRectangle2D.Float(0, 0, btn.getWidth() - 1, btn.getHeight() - 1, 12, 12));
                        }
                        g2.dispose();
                        super.paint(g, c);
                    }
                });
                btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
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
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminMainView();
        });
    }
}