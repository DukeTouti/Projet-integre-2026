package projetintegre.views;

import projetintegre.views.ADMINjpanel.*;
import javax.swing.*;
import java.awt.*;

public class AdminMainView extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AdminMainView() {
        setTitle("Espace Administration — Gestion PSH");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barre supérieure (Navbar Admin)
        add(createHeader(), BorderLayout.NORTH);

        // 2. Barre latérale gauche (Sidebar à 5 boutons)
        add(createSidebar(), BorderLayout.WEST);

        // 3. Zone centrale dynamique (CardLayout)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Enregistrement des 5 cartes dans la pile
        cardPanel.add(new AdminDashboardPanel(), "ADMIN_DASHBOARD");
        cardPanel.add(new AdminDossiersListPanel(), "ADMIN_DOSSIERS");
        cardPanel.add(new AdminComplaintsPanel(), "ADMIN_COMPLAINTS");
        cardPanel.add(new AdminArchivesPanel(), "ADMIN_ARCHIVES");
        cardPanel.add(new AdminStatsPanel(), "ADMIN_STATS");

        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x115E59)); // Couleur Teal Admin
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("PORTAIL ADMINISTRATEUR — UIR PSH Platform");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Admin · Scolarité  [AD] 👤");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userLabel.setForeground(Color.WHITE);
        header.add(userLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xE5E7EB)),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        // Création des 5 boutons basés sur la maquette d'administration
        JButton btnDashboard = createSidebarButton("📊  Dashboard");
        JButton btnDossiers = createSidebarButton("📁  Dossiers");
        JButton btnComplaints = createSidebarButton("💬  Réclamations");
        JButton btnArchives = createSidebarButton("🗄️  Archives");
        JButton btnStats = createSidebarButton("📈  Statistiques");

        // Configuration des événements de clic pour chaque carte
        btnDashboard.addActionListener(e -> cardLayout.show(cardPanel, "ADMIN_DASHBOARD"));
        btnDossiers.addActionListener(e -> cardLayout.show(cardPanel, "ADMIN_DOSSIERS"));
        btnComplaints.addActionListener(e -> cardLayout.show(cardPanel, "ADMIN_COMPLAINTS"));
        btnArchives.addActionListener(e -> cardLayout.show(cardPanel, "ADMIN_ARCHIVES"));
        btnStats.addActionListener(e -> cardLayout.show(cardPanel, "ADMIN_STATS"));

        // Alignement vertical dans la sidebar
        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnDossiers);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnComplaints);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnArchives);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnStats);

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AdminMainView();
        });
    }
}