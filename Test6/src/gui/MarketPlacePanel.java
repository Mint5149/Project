package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import user.User;

public class MarketPlacePanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;

    public MarketPlacePanel(MainFrame frame, User user) {
        this.mainFrame = frame;
        this.currentUser = user;
        
        setLayout(new BorderLayout());
        setBackground(new Color(40, 60, 80)); // สีพื้นหลัง Dark Blue

        // --- 1. Header (ส่วนหัว) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel titleLabel = new JLabel("STUDENT BOOK EXCHANGE SYSTEM");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userLabel = new JLabel("User: " + user.getUsername());
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        userLabel.setForeground(Color.YELLOW);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(userLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Menu Grid (เมนูตรงกลาง) ---
        JPanel menuPanel = new JPanel(new GridLayout(2, 3, 20, 20)); // 2 แถว 3 คอลัมน์
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(20, 50, 50, 50));

        // เพิ่มปุ่มเมนูต่างๆ
        menuPanel.add(createMenuButton("🔍", "Search Books", "ค้นหาหนังสือ/ชีทสรุป"));
        menuPanel.add(createMenuButton("➕", "Post New Item", "ลงประกาศขาย/แจก")); 
        menuPanel.add(createMenuButton("📝", "Manage Listings", "จัดการประกาศของฉัน")); 
        menuPanel.add(createMenuButton("🕒", "History", "ประวัติการแลกเปลี่ยน"));
        
        // ปุ่ม Logout (แยกมาใส่สีแดง)
        JButton logoutBtn = createMenuButton("🚪", "Logout", "ออกจากระบบ");
        logoutBtn.setBackground(new Color(200, 80, 80)); // สีแดง
        menuPanel.add(logoutBtn);

        menuPanel.add(new JLabel("")); // ใส่ช่องว่างเพื่อให้ Grid เรียงสวย
        add(menuPanel, BorderLayout.CENTER);
    }

    // ฟังก์ชันสร้างปุ่มเมนู และใส่ Logic การเปลี่ยนหน้า
    private JButton createMenuButton(String icon, String title, String subtitle) {
        String html = "<html><center><font size='5'>" + icon + "</font><b>" + title + "</b></center></html>";
        JButton btn = new JButton(html);
        btn.setBackground(new Color(176, 196, 222)); // สีฟ้าอ่อน Default
        
        btn.addActionListener(e -> {
            try {
                // --- Logic การเปลี่ยนหน้าทั้งหมด ---
                if (title.contains("Search")) {
                    mainFrame.showSearch(currentUser); // ไปหน้าค้นหา (SearchPanel)
                
                } else if (title.contains("Post")) {
                    mainFrame.showPostItem(currentUser); // ไปหน้าลงขาย (PostItemPanel)
                
                } else if (title.contains("Manage")) {
                    mainFrame.showManageListings(currentUser); // ไปหน้าจัดการของฉัน (ManageListingsPanel)
                
                } else if (title.contains("History")) {
                    mainFrame.showHistory(currentUser); // ไปหน้าประวัติ (HistoryPanel)
                
                } else if (title.contains("Logout")) {
                    mainFrame.showLogin(); // กลับหน้า Login
                
                } else {
                    JOptionPane.showMessageDialog(this, "Coming Soon");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        return btn;
    }
}