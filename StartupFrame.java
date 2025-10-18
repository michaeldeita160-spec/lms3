package com.lms.ui;

import com.lms.demo.DemoModeManager;
import com.lms.ui.components.RoundedButton;

import javax.swing.*;
import java.awt.*;

/**
 * Startup Frame - Choose between Demo Mode or Database Mode
 */
public class StartupFrame extends JFrame {
    
    public StartupFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("LMS - Learning Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(41, 128, 185);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Title
        JLabel lblTitle = new JLabel("Learning Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 40, 700, 40);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel("Choose how you want to run the application");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(236, 240, 241));
        lblSubtitle.setBounds(0, 85, 700, 25);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblSubtitle);
        
        // Demo Mode Panel
        JPanel demoPanel = createOptionPanel(
            "🚀 Demo Mode",
            "Run without database",
            new String[]{
                "✓ No MySQL/XAMPP required",
                "✓ Pre-loaded sample data",
                "✓ Instant start - just click!",
                "✓ Perfect for testing & exploration",
                "✗ Data not saved permanently"
            },
            new Color(46, 204, 113),
            150
        );
        mainPanel.add(demoPanel);
        
        // Database Mode Panel
        JPanel dbPanel = createOptionPanel(
            "💾 Database Mode",
            "Connect to MySQL database",
            new String[]{
                "✓ Persistent data storage",
                "✓ Full functionality",
                "✓ Multi-user support",
                "✗ Requires MySQL/XAMPP",
                "✗ Database setup needed"
            },
            new Color(155, 89, 182),
            400
        );
        mainPanel.add(dbPanel);
        
        // Demo Mode Button
        RoundedButton btnDemo = new RoundedButton("START DEMO MODE");
        btnDemo.setBounds(50, 400, 250, 50);
        btnDemo.setBackgroundColor(new Color(46, 204, 113));
        btnDemo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDemo.addActionListener(e -> startDemoMode());
        mainPanel.add(btnDemo);
        
        // Database Mode Button
        RoundedButton btnDatabase = new RoundedButton("START DATABASE MODE");
        btnDatabase.setBounds(400, 400, 250, 50);
        btnDatabase.setBackgroundColor(new Color(155, 89, 182));
        btnDatabase.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDatabase.addActionListener(e -> startDatabaseMode());
        mainPanel.add(btnDatabase);
        
        add(mainPanel);
    }
    
    private JPanel createOptionPanel(String title, String subtitle, String[] features, Color accentColor, int x) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(x, 130, 250, 250);
        panel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(accentColor);
        lblTitle.setBounds(10, 10, 230, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(127, 140, 141));
        lblSubtitle.setBounds(10, 40, 230, 20);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblSubtitle);
        
        // Features
        int y = 70;
        for (String feature : features) {
            JLabel lblFeature = new JLabel(feature);
            lblFeature.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblFeature.setForeground(new Color(52, 73, 94));
            lblFeature.setBounds(15, y, 220, 25);
            panel.add(lblFeature);
            y += 30;
        }
        
        return panel;
    }
    
    private void startDemoMode() {
        // Demo mode is always enabled in LMS-2

        // Show demo credentials
        JOptionPane.showMessageDialog(this,
            DemoModeManager.getWelcomeMessage(),
            "Welcome to LMS-2 Demo!",
            JOptionPane.INFORMATION_MESSAGE);

        // Open login frame
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }

    private void startDatabaseMode() {
        // Database mode not available in LMS-2
        
        // Show database info
        int choice = JOptionPane.showConfirmDialog(this,
            "Database Mode Requirements:\n\n" +
            "1. MySQL server must be running (XAMPP)\n" +
            "2. Database 'lms_db' must exist\n" +
            "3. Run database/lms_schema.sql first\n" +
            "4. Optionally run database/sample_data.sql\n\n" +
            "Have you completed the database setup?",
            "Database Mode - Requirements",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Open login frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Skip startup screen and go directly to login
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

