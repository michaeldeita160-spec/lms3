package com.lms.ui;

import com.lms.dao.UserDAO;
import com.lms.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Login Frame - Redesigned to match SimpleLoginFrame style
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("LMS-2 Login");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with solid blue background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(41, 128, 185));
        mainPanel.setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Learning Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(25, 50, 400, 35);
        mainPanel.add(lblTitle);

        // Demo subtitle
        JLabel lblDemo = new JLabel("Demo: demo_student / password");
        lblDemo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblDemo.setForeground(Color.WHITE);
        lblDemo.setHorizontalAlignment(SwingConstants.CENTER);
        lblDemo.setBounds(25, 90, 400, 20);
        mainPanel.add(lblDemo);

        // Username label
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(75, 140, 300, 20);
        mainPanel.add(lblUsername);

        // Username field
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(75, 165, 300, 35);
        mainPanel.add(txtUsername);

        // Password label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(75, 215, 300, 20);
        mainPanel.add(lblPassword);

        // Password field
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(75, 240, 300, 35);
        mainPanel.add(txtPassword);

        // Login button - GREEN
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(46, 204, 113)); // Green color
        btnLogin.setBounds(75, 300, 300, 45);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> handleLogin());
        mainPanel.add(btnLogin);

        // Register button - BLUE
        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(52, 152, 219)); // Blue color
        btnRegister.setBounds(75, 360, 300, 35);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setOpaque(true);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> openRegistration());
        mainPanel.add(btnRegister);

        // Add Enter key listener for login
        txtPassword.addActionListener(e -> handleLogin());

        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDAO.loginUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                "Login successful!\nWelcome, " + user.getFullName(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            // Open dashboard
            DashboardFrame dashboard = new DashboardFrame(user);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }

    private void openRegistration() {
        RegistrationFrame registrationFrame = new RegistrationFrame(this);
        registrationFrame.setVisible(true);
        this.setVisible(false);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

