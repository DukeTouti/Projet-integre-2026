package projetintegre.views.ADMINjpanel;

import javax.swing.*;
import java.awt.*;

public class AdminArchivesPanel extends JPanel {
    public AdminArchivesPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🗄️ Archives des dossiers validés et clôturés", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0x115E59));
        add(title, BorderLayout.CENTER);
    }
}