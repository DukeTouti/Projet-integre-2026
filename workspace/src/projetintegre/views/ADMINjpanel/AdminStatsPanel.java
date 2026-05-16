package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import java.awt.*;

public class AdminStatsPanel extends JPanel {
    public AdminStatsPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📈 Statistiques de répartition des aménagements PSH", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0x115E59));
        add(title, BorderLayout.CENTER);
    }
}