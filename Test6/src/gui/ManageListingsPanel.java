package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import user.User;
import listing.Listing;
import book.Book;

public class ManageListingsPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField;

    public ManageListingsPanel(MainFrame frame, User user) {
        this.mainFrame = frame;
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(new Color(40, 60, 80)); // พื้นหลังสี Dark Blue

        // --- 1. Header ---
        JLabel titleLabel = new JLabel("Manage My Listings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- 2. Table (ตารางแสดงข้อมูล) ---
        // กำหนดหัวตาราง: ID, Title, Course Code, Price
        String[] columnNames = {"ID", "Title", "Course Code", "Price"};
        // Make table non-editable
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        // Center alignment for all columns
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add Mouse Listener to populate ID field
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                 int row = table.getSelectedRow();
                 if (row >= 0) {
                     String id = table.getValueAt(row, 0).toString();
                     idField.setText(id);
                 }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        // เว้นขอบซ้ายขวาหน่อยจะได้ไม่ชิดจอเกินไป
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));
        tablePanel.add(scrollPane);
        add(tablePanel, BorderLayout.CENTER);

        // --- 3. Control Panel (ส่วนล่าง: ช่องกรอก ID และปุ่ม) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(new Color(40, 60, 80));

        // Label & TextField
        JLabel inputLabel = new JLabel("กรอก ID ที่ต้องการจัดการ:");
        inputLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        inputLabel.setForeground(Color.WHITE);
        bottomPanel.add(inputLabel);

        idField = new JTextField(10);
        idField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        bottomPanel.add(idField);

        // Buttons
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(100, 35));
        backBtn.addActionListener(e -> mainFrame.showMarketPlace(currentUser));
        bottomPanel.add(backBtn);

        JButton soldBtn = new JButton("Mark as Sold");
        soldBtn.setBackground(new Color(50, 205, 50)); // สีเขียว Lime Green
        soldBtn.setForeground(Color.BLACK);
        soldBtn.setPreferredSize(new Dimension(120, 35));
        soldBtn.addActionListener(e -> markAsSoldAction());
        bottomPanel.add(soldBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setPreferredSize(new Dimension(100, 35));
        deleteBtn.addActionListener(e -> deleteAction());
        bottomPanel.add(deleteBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // โหลดข้อมูลใส่ตารางทันทีที่เปิดหน้านี้
        loadData();
    }

    // ฟังก์ชันดึงข้อมูลลงตาราง
    private void loadData() {
        tableModel.setRowCount(0); // ล้างตารางเก่า
        List<Listing> allListings = mainFrame.getSystem().getAllListings();

        for (Listing l : allListings) {
            // กรอง: เอาเฉพาะของ User คนนี้ และยังไม่ขาย (isAvailable)
            if (l.getOwner().getUserId().equals(currentUser.getUserId()) && l.isAvailable()) {
                
                String code = "-";
                // เช็คว่าเป็น Book หรือ StudtMaterial เพื่อดึง Course Code
                if (l.getItem() instanceof Book) {
                    code = ((Book) l.getItem()).getCourseCode();
                } else if (l.getItem() instanceof studymaterial.StudyMaterial) {
                    String c = ((studymaterial.StudyMaterial) l.getItem()).getCourseCode();
                    code = (c != null) ? c : "-";
                }

                Object[] row = {
                    l.getListingId(),
                    l.getItem().getTitle(),
                    code,
                    l.getPrice()
                };
                tableModel.addRow(row);
            }
        }
    }

    // ฟังก์ชันปุ่ม Mark as Sold
    private void markAsSoldAction() {
        String idText = idField.getText();
        if (idText.isEmpty()) return;

        try {
            int id = Integer.parseInt(idText);
            List<Listing> list = mainFrame.getSystem().getAllListings();
            Listing foundListing = null;

            for (Listing l : list) {
                if (l.getListingId() == id && l.getOwner().getUserId().equals(currentUser.getUserId())) {
                    foundListing = l;
                    break;
                }
            }

            if (foundListing != null) {
                // Prompt for Buyer Name
                String buyerName = JOptionPane.showInputDialog(this, "Enter Buyer Name (ชื่อผู้ซื้อ):");
                if (buyerName == null || buyerName.trim().isEmpty()) {
                    return; // ยกเลิกถ้าไม่กรอกชื่อ
                }
                
                // Create Dummy Buyer & Transaction
                User buyer = new User("guest_" + System.currentTimeMillis(), buyerName, "", "");
                transaction.Transaction trx = new transaction.Transaction("TRX-" + System.currentTimeMillis(), buyer, currentUser, foundListing);
                
                // Record Transaction
                mainFrame.getSystem().recordTransaction(trx);
                
                // Mark item as sold (soft delete)
                foundListing.markAsSold();

                JOptionPane.showMessageDialog(this, "Item Sold and Removed from Listings!");
                loadData(); // รีเฟรชตาราง
                idField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "ไม่พบ ID หรือสินค้านี้ไม่ใช่ของคุณ");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "กรุณากรอก ID เป็นตัวเลข");
        }
    }

    // ฟังก์ชันปุ่ม Delete
    private void deleteAction() {
        String idText = idField.getText();
        if (idText.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "ยืนยันการลบรายการนี้?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(idText);
            // เรียกใช้ฟังก์ชัน removeListing ที่เราเพิ่งเพิ่มใน System
            boolean success = mainFrame.getSystem().removeListing(id);

            if (success) {
                JOptionPane.showMessageDialog(this, "ลบรายการสำเร็จ");
                loadData(); // รีเฟรชตาราง
                idField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "ไม่พบ ID นี้");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "กรุณากรอก ID เป็นตัวเลข");
        }
    }
}