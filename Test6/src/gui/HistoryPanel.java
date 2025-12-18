package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import user.User;
import transaction.Transaction;

public class HistoryPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    public HistoryPanel(MainFrame frame, User user) {
        this.mainFrame = frame;
        this.currentUser = user;
        
        setLayout(new BorderLayout());
        setBackground(new Color(40, 60, 80)); // Theme สีเดิม

        // --- 1. Header ---
        JLabel titleLabel = new JLabel("Transaction History (ประวัติการแลกเปลี่ยน)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- 2. Table ---
        // กำหนดคอลัมน์: วันที่, รหัสธุรกรรม, ประเภท (ซื้อ/ขาย), รายการ, ราคา, คู่ค้า
        String[] columnNames = {"Date", "Trx ID", "Type", "Item", "Price", "Trader (คู่ค้า)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30); // แถวสูงหน่อยให้อ่านง่าย
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50)); // เว้นขอบ
        tablePanel.add(scrollPane);
        add(tablePanel, BorderLayout.CENTER);

        // --- 3. Footer (Back Button) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(40, 60, 80));
        
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        backBtn.addActionListener(e -> mainFrame.showMarketPlace(currentUser));
        
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // โหลดข้อมูล
        loadHistoryData();
    }

    private void loadHistoryData() {
        tableModel.setRowCount(0); // เคลียร์ตาราง
        List<Transaction> history = mainFrame.getSystem().getUserTransactions(currentUser);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Transaction t : history) {
            boolean isBuyer = t.getBuyer().getUserId().equals(currentUser.getUserId());
            
            String type = isBuyer ? "Bought (ซื้อ)" : "Sold (ขาย)";
            String trader = isBuyer ? t.getSeller().getUsername() : t.getBuyer().getUsername();
            
            Object[] row = {
                sdf.format(t.getTransactionDate()),
                t.getTransactionId(),
                type,
                t.getListing().getItem().getTitle(),
                t.getListing().getPrice(),
                trader
            };
            tableModel.addRow(row);
        }
    }
}