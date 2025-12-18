package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import listing.Listing;
import book.Book;
import user.User;

public class SearchPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    
    // UI Components
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Checkboxes (ตัวเลือกค้นหา & เรียงลำดับ)
    private JCheckBox cbByCode, cbByTitle;
    private JCheckBox cbSortPrice, cbSortTitle;

    public SearchPanel(MainFrame frame, User user) {
        this.mainFrame = frame;
        this.currentUser = user;
        setLayout(null); // ใช้ Null Layout เพื่อจัดตำแหน่งตามภาพ
        setBackground(new Color(40, 60, 80)); // สี Dark Blue Theme

        // --- 1. Header ---
        JLabel titleLabel = new JLabel("SEARCH RESULTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 20, 800, 40);
        add(titleLabel);

        // --- 2. Filter Options (Checkbox) ---
        JLabel filterLabel = new JLabel("ประเภทการค้นหา:");
        filterLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setBounds(50, 70, 150, 30);
        add(filterLabel);

        cbByCode = createCheckBox("ค้นหาตามรหัสวิชา", 180, 70);
        cbByTitle = createCheckBox("ค้นหาตามชื่อหนังสือ", 320, 70);
        cbByTitle.setSelected(true); // Default ให้ค้นตามชื่อ

        cbSortPrice = createCheckBox("เรียงลำดับตามราคา", 480, 70);
        cbSortTitle = createCheckBox("เรียงลำดับตามชื่อ", 630, 70);
        
        // Grouping Checkbox (ถ้าอยากให้เลือกได้อย่างใดอย่างหนึ่ง)
        // แต่ในที่นี้แยกอิสระเพื่อให้ User ติ๊กผสมได้

        // --- 3. Search Bar ---
        searchField = new JTextField();
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        searchField.setBounds(50, 110, 550, 40);
        add(searchField);

        JButton searchBtn = new JButton("ค้นหา");
        searchBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        searchBtn.setBounds(620, 110, 130, 40);
        searchBtn.addActionListener(e -> doSearch());
        add(searchBtn);

        // --- 4. Table ---
        String[] columnNames = {"ID", "Title", "Price", "Seller"};
        
        // Make table non-editable
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        
        // Center alignment for all columns
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Add Mouse Listener for Popup
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                 int row = table.rowAtPoint(evt.getPoint());
                 if (row >= 0) {
                     // Get ID from first column
                     int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                     showItemDetails(id);
                 }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 170, 700, 350);
        add(scrollPane);

        // --- 5. Back Button ---
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        backBtn.setBounds(650, 530, 100, 35);
        backBtn.addActionListener(e -> mainFrame.showMarketPlace(currentUser));
        add(backBtn);

        // โหลดข้อมูลทั้งหมดมาแสดงก่อน (Show All)
        doSearch();
    }

    private void showItemDetails(int listingId) {
        Listing listing = null;
        for (Listing l : mainFrame.getSystem().getAllListings()) {
            if (l.getListingId() == listingId) {
                listing = l;
                break;
            }
        }
        
        if (listing == null) return;
        
        item.Item item = listing.getItem();
        String type = (item instanceof Book) ? "Book" : "Study Material";
        String extraInfo = "";
        
        if (item instanceof Book) {
            Book b = (Book) item;
            extraInfo = "Author : " + (b.getAuthor() != null ? b.getAuthor() : "-") + "\n" +
                        "Course Code : " + (b.getCourseCode() != null ? b.getCourseCode() : "-");
        } else if (item instanceof studymaterial.StudyMaterial) {
            studymaterial.StudyMaterial sm = (studymaterial.StudyMaterial) item;
            extraInfo = "Subject : " + (sm.getSubject() != null ? sm.getSubject() : "-") + "\n" +
                        "Course Code : " + (sm.getCourseCode() != null ? sm.getCourseCode() : "-");
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateStr = sdf.format(listing.getListingDate());
        
        String message = "ID : " + listing.getListingId() + "\n" +
                         "Type : " + type + "\n" +
                         "Title : " + item.getTitle() + "\n" +
                         extraInfo + "\n" +
                         "Description : " + item.getDescription() + "\n" +
                         "Price : " + listing.getPrice() + " บาท\n" +
                         "Seller : " + listing.getOwner().getUsername() + "\n" +
                         "Contact : " + listing.getOwner().getContactInfo() + "\n" +
                         "Date Listed : " + dateStr;
                         
        JOptionPane.showMessageDialog(this, message, "Item Detail - " + item.getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    private JCheckBox createCheckBox(String text, int x, int y) {
        JCheckBox cb = new JCheckBox(text);
        cb.setFont(new Font("Tahoma", Font.BOLD, 12));
        cb.setForeground(Color.WHITE);
        cb.setBackground(new Color(40, 60, 80)); // กลมกลืนกับพื้นหลัง
        cb.setBounds(x, y, 150, 30);
        add(cb);
        return cb;
    }

    // --- Logic การค้นหาและเรียงลำดับ ---
    private void doSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        tableModel.setRowCount(0); // ล้างตารางเก่า
        
        List<Listing> allListings = mainFrame.getSystem().getAllListings();
        List<Listing> resultList = new ArrayList<>();

        // 1. Filter (กรองข้อมูล)
        for (Listing l : allListings) {
            // ต้องเป็นของที่ยังไม่ขาย (Available)
            if (!l.isAvailable()) continue;

            boolean match = false;
            String title = l.getItem().getTitle().toLowerCase();
            String code = ""; 
            
            // พยายามดึง Course Code (ถ้าเป็น Book)
            if (l.getItem() instanceof Book) {
                code = ((Book) l.getItem()).getCourseCode().toLowerCase();
            } else if (l.getItem() instanceof studymaterial.StudyMaterial) {
                String c = ((studymaterial.StudyMaterial) l.getItem()).getCourseCode();
                code = (c != null) ? c.toLowerCase() : "";
            }

            // ถ้าไม่มี Keyword ให้แสดงทั้งหมด
            if (keyword.isEmpty()) {
                match = true;
            } else {
                // เช็คเงื่อนไขตาม Checkbox
                if (cbByTitle.isSelected() && title.contains(keyword)) match = true;
                if (cbByCode.isSelected() && code.contains(keyword)) match = true;
            }

            if (match) {
                resultList.add(l);
            }
        }

        // 2. Sort (เรียงลำดับ)
        if (cbSortPrice.isSelected()) {
            resultList.sort(Comparator.comparingDouble(Listing::getPrice));
        } else if (cbSortTitle.isSelected()) {
            resultList.sort((l1, l2) -> l1.getItem().getTitle().compareToIgnoreCase(l2.getItem().getTitle()));
        }

        // 3. Add to Table (แสดงผล)
        for (Listing l : resultList) {
            Object[] row = {
                l.getListingId(),
                l.getItem().getTitle(),
                l.getPrice(),
                l.getOwner().getUsername()
            };
            tableModel.addRow(row);
        }
    }
}