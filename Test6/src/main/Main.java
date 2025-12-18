package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Run as GUI
        SwingUtilities.invokeLater(() -> {
            try {
                // Set System LookAndFeel for better component appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Launch MainFrame which now starts with WelcomePanel
            new MainFrame().setVisible(true);
        });
    }
}