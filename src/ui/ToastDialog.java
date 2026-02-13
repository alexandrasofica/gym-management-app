package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToastDialog extends JDialog {

    public ToastDialog(Window owner, String titleText, String messageText) {
        super(owner, titleText, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0, 0, 0, 160));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(new Color(0, 0, 0, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
                new EmptyBorder(18, 22, 18, 22)
        ));

        JLabel title = new JLabel(titleText);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel msg = new JLabel("<html><body style='width: 320px;'>" + 
                escape(messageText).replace("\n", "<br>") + 
                "</body></html>");
        msg.setForeground(new Color(230, 230, 230));
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton ok = new JButton("OK");
        styleWhiteButton(ok);
        ok.addActionListener(e -> dispose());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btns.setOpaque(false);
        btns.add(ok);

        card.add(title, BorderLayout.NORTH);
        card.add(msg, BorderLayout.CENTER);
        card.add(btns, BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);
        setContentPane(root);

        pack();
        setSize(420, getHeight());
        setLocationRelativeTo(owner);
    }

    private static void styleWhiteButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void show(Window owner, String title, String message) {
        new ToastDialog(owner, title, message).setVisible(true);
    }
}
