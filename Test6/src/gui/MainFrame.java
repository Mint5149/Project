package gui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;
import bookexchange.BookExchangeSystem;
import user.User;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BookExchangeSystem system;
    private User currentUser;

    public MainFrame() {
        // 1. ตั้งค่าหน้าต่างหลัก
        setTitle("Student Book Exchange System ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // จัดให้อยู่กลางจอ

        // 2. เริ่มต้นระบบ Logic หลังบ้าน
        system = new BookExchangeSystem();

        // 3. ตั้งค่า Layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 4. สร้างหน้าจอต่างๆ
        mainPanel.add(new WelcomePanel(this), "Welcome");
        mainPanel.add(new LoginPanel(this), "Login");
        mainPanel.add(new RegisterPanel(this), "Register");
        
        add(mainPanel);
        showWelcome();
    }

    // --- Navigation Methods ---
    public void showWelcome() {
        cardLayout.show(mainPanel, "Welcome");
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "Login");
        this.currentUser = null;
    }

    public void showRegister() {
        cardLayout.show(mainPanel, "Register");
    }

    public void showMarketPlace(User user) {
        this.currentUser = user;
        mainPanel.add(new MarketPlacePanel(this, user), "Market");
        cardLayout.show(mainPanel, "Market");
    }
    public void showPostItem(User user) {
        // สร้างหน้า PostItem ใหม่ทุกครั้งที่กดเข้ามา (จะได้หน้าโล่งๆ)
        mainPanel.add(new PostItemPanel(this, user), "PostItem");
        cardLayout.show(mainPanel, "PostItem");
    }
    
    public void showManageListings(User user) {
        // สร้างหน้า ManageListingsPanel ใหม่ทุกครั้งเพื่อรีเฟรชข้อมูล
        mainPanel.add(new ManageListingsPanel(this, user), "ManageListings");
        cardLayout.show(mainPanel, "ManageListings");
    }

    public BookExchangeSystem getSystem() {
        return system;
    }

    // --- [สำคัญมาก] ฟังก์ชันเปลี่ยนฟอนต์ทั้งโปรแกรม ---
    private static void setupGlobalFont(String fontName) {
        FontUIResource fontRes = new FontUIResource(new Font(fontName, Font.PLAIN, 14));
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    // --- Main Method ---
    public static void main(String[] args) {
        // 1. เรียกใช้ฟังก์ชันเปลี่ยนฟอนต์ *ก่อน* สร้างหน้าต่าง
        // ใช้ "Tahoma" หรือ "SansSerif" ซึ่งรองรับไทยแน่นอน
        setupGlobalFont("Tahoma"); 

        // 2. รันโปรแกรม
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
    public void showHistory(User user) {
        mainPanel.add(new HistoryPanel(this, user), "History");
        cardLayout.show(mainPanel, "History");
    }
 // เพิ่มใน MainFrame.java
    public void showSearch(User user) {
        mainPanel.add(new SearchPanel(this, user), "Search");
        cardLayout.show(mainPanel, "Search");
    }
}