package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import java.awt.*;

public class AdminComplaintsPanel extends JPanel {
    public AdminComplaintsPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("💬 Boîte de réception des Réclamations Étudiants", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0x115E59));
        add(title, BorderLayout.CENTER);
    }
}