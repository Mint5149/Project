package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class WelcomePanel extends JPanel {
    private MainFrame mainFrame;

    public WelcomePanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(null);
        setBackground(new Color(44, 62, 80)); // Dark Blue Background

        // --- Title ---
        JLabel titleLabel = new JLabel("STUDENT BOOK EXCHANGE SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, 800, 40);
        add(titleLabel);

        JLabel subLabel = new JLabel("ยินดีต้อนรับเข้าสู่ระบบแลกเปลี่ยนหนังสือสำหรับนักศึกษา", SwingConstants.CENTER);
        subLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        subLabel.setForeground(Color.WHITE);
        subLabel.setBounds(0, 100, 800, 30);
        add(subLabel);

        // --- Image (Left) ---
        String imagePath = "books.png";
        JLabel imgLabel = new JLabel();
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(350, 300, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("Image not found: books.png");
            imgLabel.setForeground(Color.WHITE);
        }
        imgLabel.setBounds(50, 180, 350, 300);
        add(imgLabel);

        // --- Buttons (Right) ---
        int btnX = 480;
        int btnY = 220;
        int btnW = 200;
        int btnH = 50;
        int gap = 70;

        JButton btnLogin = createStyledButton("เข้าสู่ระบบ");
        btnLogin.setBounds(btnX, btnY, btnW, btnH);
        btnLogin.addActionListener(e -> mainFrame.showLogin());
        add(btnLogin);

        JButton btnRegister = createStyledButton("สมัครสมาชิก");
        btnRegister.setBounds(btnX, btnY + gap, btnW, btnH);
        btnRegister.addActionListener(e -> mainFrame.showRegister());
        add(btnRegister);

        JButton btnExit = createStyledButton("ออกจากโปรแกรม");
        btnExit.setBounds(btnX, btnY + (gap * 2), btnW, btnH);
        btnExit.addActionListener(e -> System.exit(0));
        add(btnExit);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(220, 220, 220)); 
        btn.setForeground(Color.BLACK);
        return btn;
    }
}
