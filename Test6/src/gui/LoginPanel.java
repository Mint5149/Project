package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import user.User;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField userField;
    private JPasswordField passField;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(null);
        setBackground(new Color(44, 62, 80)); // Dark Blue

        // --- Header ---
        JLabel titleLabel = new JLabel("เข้าสู่ระบบ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, 800, 40);
        add(titleLabel);

        // --- Input Fields (Left Side) ---
        int startX = 50; // Changed from 80
        int startY = 150;
        int gapY = 90;

        // User Label & Field
        JLabel userLabel = new JLabel("ชื่อผู้ใช้");
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(startX, startY, 150, 30);
        add(userLabel);

        userField = new JTextField();
        userField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        userField.setBounds(startX, startY + 35, 300, 40);
        userField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(userField);

        // Pass Label & Field
        JLabel passLabel = new JLabel("รหัสผ่าน");
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(startX, startY + gapY, 150, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passField.setBounds(startX, startY + gapY + 35, 300, 40);
        passField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(passField);

        // --- Login Button ---
        JButton loginBtn = new JButton("เข้าสู่ระบบ");
        loginBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        loginBtn.setBounds(startX, 350, 300, 45);
        loginBtn.setBackground(new Color(220, 220, 220)); 
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(e -> doLogin());
        add(loginBtn);

        // --- Back Button ---
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        backBtn.setBounds(20, 500, 100, 40);
        backBtn.setBackground(new Color(220, 220, 220));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> mainFrame.showWelcome());
        add(backBtn);

        // --- Image (Right Side) ---
        String imagePath = "children-reading-book-png_100049-removebg-preview.png";
        JLabel imgLabel = new JLabel();
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("Image not found: " + imagePath);
            imgLabel.setForeground(Color.RED);
        }
        imgLabel.setBounds(400, 150, 350, 350); // Changed X from 420 to 400
        add(imgLabel);
    }

    private void doLogin() {
        String id = userField.getText();
        String pass = new String(passField.getPassword());

        Object result = mainFrame.getSystem().login(id, pass);

        if (result instanceof User) {
            User user = (User) result;
            JLabel label = new JLabel("Login สำเร็จ! ยินดีต้อนรับ: " + user.getUsername());
            label.setFont(new Font("Tahoma", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, label);
            
            // Go to Market/Main Menu
            mainFrame.showMarketPlace(user); 
            
            userField.setText("");
            passField.setText("");
        } else {
            JLabel label = new JLabel("ID หรือ Password ผิด! (Login Failed)");
            label.setFont(new Font("Tahoma", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, label, "แจ้งเตือน", JOptionPane.ERROR_MESSAGE);
        }
    }
}