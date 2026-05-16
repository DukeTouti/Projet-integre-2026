package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import java.awt.*;

public class AdminDossiersListPanel extends JPanel {
    public AdminDossiersListPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📁 Base de données globale de tous les dossiers", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0x115E59));
        add(title, BorderLayout.CENTER);
    }
}