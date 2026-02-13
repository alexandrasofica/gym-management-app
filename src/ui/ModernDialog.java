package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernDialog {

    private static JDialog baseDialog(Window parent, String title) {
        JDialog d = new JDialog(parent);
        d.setModal(true);
        d.setUndecorated(true);
        d.setAlwaysOnTop(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 1));
        root.setBackground(new Color(20, 20, 20, 235));

        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setBorder(new EmptyBorder(14, 16, 10, 16));

        root.add(t, BorderLayout.NORTH);
        d.setContentPane(root);
        return d;
    }

    private static void show(Window parent, String title, String message, String buttonText) {
        JDialog d = baseDialog(parent, title);
        JPanel root = (JPanel) d.getContentPane();

        JLabel msg = new JLabel(
                "<html><div style='width:360px;'>" + escapeHtml(message) + "</div></html>"
        );
        msg.setForeground(new Color(235, 235, 235));
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setBorder(new EmptyBorder(0, 16, 14, 16));

        JButton ok = new JButton(buttonText);
        styleWhiteButton(ok);
        ok.addActionListener(e -> d.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        bottom.setOpaque(false);
        bottom.add(ok);

        root.add(msg, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }

    public static void info(Component parent, String title, String message) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        show(w, title, message, "OK");
    }

    public static void error(Component parent, String title, String message) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        show(w, title, message, "OK");
    }
    
    public static boolean confirm(Component parent, String title, String message) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        JDialog d = baseDialog(w, title);
        JPanel root = (JPanel) d.getContentPane();

        JLabel msg = new JLabel(
                "<html><div style='width:360px;'>" + escapeHtml(message) + "</div></html>"
        );
        msg.setForeground(new Color(235, 235, 235));
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setBorder(new EmptyBorder(0, 16, 14, 16));

        final boolean[] result = { false };

        JButton yes = new JButton("Da");
        JButton no  = new JButton("Nu");
        styleWhiteButton(yes);
        styleWhiteButton(no);

        yes.addActionListener(e -> { result[0] = true; d.dispose(); });
        no.addActionListener(e -> { result[0] = false; d.dispose(); });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setOpaque(false);
        bottom.add(yes);
        bottom.add(no);

        root.add(msg, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(w);
        d.setVisible(true);

        return result[0];
    }

    public static String prompt(Component parent, String title, String message) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        JDialog d = baseDialog(w, title);
        JPanel root = (JPanel) d.getContentPane();

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(0, 16, 10, 16));

        JLabel msg = new JLabel(
                "<html><div style='width:360px;'>" + escapeHtml(message) + "</div></html>"
        );
        msg.setForeground(new Color(235, 235, 235));
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        center.add(msg);
        center.add(Box.createVerticalStrut(10));
        center.add(field);

        final String[] result = { null };

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        styleWhiteButton(ok);
        styleWhiteButton(cancel);

        ok.addActionListener(e -> {
            result[0] = field.getText();
            d.dispose();
        });

        cancel.addActionListener(e -> {
            result[0] = null;
            d.dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setOpaque(false);
        bottom.add(ok);
        bottom.add(cancel);

        root.add(center, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(w);
        SwingUtilities.invokeLater(field::requestFocusInWindow);
        d.setVisible(true);

        return result[0];
    }
    
    private static void styleWhiteButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
