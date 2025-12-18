package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import user.User;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField idField, nameField, contactField;
    private JPasswordField passField;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(null);
        setBackground(new Color(44, 62, 80)); // Dark Blue

        // --- Header ---
        JLabel titleLabel = new JLabel("สมัครสมาชิก", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, 800, 40);
        add(titleLabel);

        // --- Input Fields (Left Side) ---
        int startX = 50; 
        int startY = 110; // Moved up from 130
        int gapY = 75;    // Reduced from 85

        // 1. User ID
        createLabelAndField("รหัสผู้ใช้", startX, startY);
        idField = createTextField(startX, startY + 35);
        add(idField);

        // 2. Username
        createLabelAndField("ชื่อผู้ใช้", startX, startY + gapY);
        nameField = createTextField(startX, startY + gapY + 35);
        add(nameField);

        // 3. Password
        createLabelAndField("รหัสผ่าน", startX, startY + gapY * 2);
        passField = createPasswordField(startX, startY + gapY * 2 + 35);
        add(passField);

        // 4. Contact
        createLabelAndField("ช่องทางการติดต่อ", startX, startY + gapY * 3);
        contactField = createTextField(startX, startY + gapY * 3 + 35);
        add(contactField);

        // --- Register Button ---
        JButton regBtn = new JButton("สมัครสมาชิก");
        regBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        regBtn.setBounds(startX, 440, 300, 45); // Moved up from 480
        regBtn.setBackground(new Color(220, 220, 220));
        regBtn.setForeground(Color.BLACK);
        regBtn.setFocusPainted(false);
        regBtn.addActionListener(e -> doRegister());
        add(regBtn);

        // --- Back Button ---
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        backBtn.setBounds(20, 510, 100, 40); // Moved to 510
        backBtn.setBackground(new Color(220, 220, 220));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> mainFrame.showWelcome());
        add(backBtn);

        // --- Image (Right Side) ---
        String imagePath = "Read1.png";
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
        imgLabel.setBounds(400, 150, 350, 350); // Changed X from 420 to 400 (Right Margin 50)
        add(imgLabel);
    }

    private void createLabelAndField(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Tahoma", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 200, 30);
        add(label);
    }

    private JTextField createTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tf.setBounds(x, y, 300, 40);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return tf;
    }

    private JPasswordField createPasswordField(int x, int y) {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Tahoma", Font.PLAIN, 18));
        pf.setBounds(x, y, 300, 40);
        pf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return pf;
    }

    private void doRegister() {
        String uid = idField.getText().trim();
        String name = nameField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        String contact = contactField.getText().trim();

        if(uid.isEmpty() || name.isEmpty() || pass.isEmpty()) {
            JLabel label = new JLabel("กรุณากรอกข้อมูลให้ครบถ้วน");
            label.setFont(new Font("Tahoma", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, label, "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User newUser = new User(uid, name, pass, contact);
        boolean success = mainFrame.getSystem().registerUser(newUser);

        if(success) {
            JLabel label = new JLabel("สมัครสมาชิกสำเร็จ! กรุณาล็อกอิน");
            label.setFont(new Font("Tahoma", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, label);
            
            mainFrame.showLogin();
            
            // Clear fields
            idField.setText("");
            nameField.setText("");
            passField.setText("");
            contactField.setText("");
        } else {
            JLabel label = new JLabel("ID นี้มีผู้ใช้แล้ว");
            label.setFont(new Font("Tahoma", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}