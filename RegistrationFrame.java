package com.lms.ui;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Registration Frame
 */
public class RegistrationFrame extends JFrame {
    private CustomTextField txtUsername;
    private CustomTextField txtEmail;
    private CustomTextField txtFullName;
    private CustomPasswordField txtPassword;
    private CustomPasswordField txtConfirmPassword;
    private RoundedButton btnRegister;
    private RoundedButton btnBack;
    private UserDAO userDAO;
    private LoginFrame loginFrame;
    
    public RegistrationFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("LMS - Registration");
        setSize(500, 700);
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
                Color color1 = new Color(46, 204, 113);
                Color color2 = new Color(155, 89, 182);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Registration panel
        RoundedPanel regPanel = new RoundedPanel();
        regPanel.setBounds(50, 50, 400, 580);
        regPanel.setBackgroundColor(Color.WHITE);
        regPanel.setHasShadow(true);
        regPanel.setLayout(null);
        
        // Title
        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(46, 204, 113));
        lblTitle.setBounds(30, 30, 300, 35);
        regPanel.add(lblTitle);
        
        JLabel lblSubtitle = new JLabel("Join our learning platform");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(100, 100, 100));
        lblSubtitle.setBounds(30, 65, 200, 25);
        regPanel.add(lblSubtitle);
        
        int yPos = 110;
        int fieldHeight = 40;
        int spacing = 70;
        
        // Full Name
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFullName.setBounds(30, yPos, 100, 25);
        regPanel.add(lblFullName);
        
        txtFullName = new CustomTextField();
        txtFullName.setPlaceholder("Enter your full name");
        txtFullName.setBounds(30, yPos + 25, 340, fieldHeight);
        regPanel.add(txtFullName);
        
        yPos += spacing;
        
        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setBounds(30, yPos, 100, 25);
        regPanel.add(lblUsername);
        
        txtUsername = new CustomTextField();
        txtUsername.setPlaceholder("Choose a username");
        txtUsername.setBounds(30, yPos + 25, 340, fieldHeight);
        regPanel.add(txtUsername);
        
        yPos += spacing;
        
        // Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEmail.setBounds(30, yPos, 100, 25);
        regPanel.add(lblEmail);
        
        txtEmail = new CustomTextField();
        txtEmail.setPlaceholder("Enter your email");
        txtEmail.setBounds(30, yPos + 25, 340, fieldHeight);
        regPanel.add(txtEmail);
        
        yPos += spacing;
        
        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setBounds(30, yPos, 100, 25);
        regPanel.add(lblPassword);
        
        txtPassword = new CustomPasswordField();
        txtPassword.setPlaceholder("Create a password");
        txtPassword.setBounds(30, yPos + 25, 340, fieldHeight);
        regPanel.add(txtPassword);
        
        yPos += spacing;
        
        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblConfirmPassword.setBounds(30, yPos, 150, 25);
        regPanel.add(lblConfirmPassword);
        
        txtConfirmPassword = new CustomPasswordField();
        txtConfirmPassword.setPlaceholder("Re-enter password");
        txtConfirmPassword.setBounds(30, yPos + 25, 340, fieldHeight);
        regPanel.add(txtConfirmPassword);

        yPos += spacing;

        // Role field removed - all registrations are automatically set to "student"

        // Register button
        btnRegister = new RoundedButton("CREATE ACCOUNT");
        btnRegister.setBounds(30, yPos, 340, 45);
        btnRegister.setBackgroundColor(new Color(46, 204, 113));
        btnRegister.addActionListener(e -> handleRegistration());
        regPanel.add(btnRegister);

        yPos += 60;

        // Already have account label and back button
        JLabel lblHaveAccount = new JLabel("Already have an account?");
        lblHaveAccount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHaveAccount.setForeground(new Color(100, 100, 100));
        lblHaveAccount.setBounds(80, yPos, 160, 30);
        regPanel.add(lblHaveAccount);

        btnBack = new RoundedButton("LOGIN");
        btnBack.setBounds(230, yPos, 80, 30);
        btnBack.setBackgroundColor(new Color(52, 152, 219));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.addActionListener(e -> backToLogin());
        regPanel.add(btnBack);
        
        mainPanel.add(regPanel);
        add(mainPanel);
    }
    
    private void handleRegistration() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String role = "student"; // All registrations are automatically set to student role
        
        // Validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 6 characters long!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if username exists
        try {
            if (userDAO.usernameExists(username)) {
                JOptionPane.showMessageDialog(this,
                    "Username already exists! Please choose another.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database connection error! Please check if MySQL is running.\n" + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Check if email exists
        try {
            if (userDAO.emailExists(email)) {
                JOptionPane.showMessageDialog(this,
                    "Email already registered! Please use another.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database connection error! Please check if MySQL is running.\n" + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Create user
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        System.out.println("Attempting to register user: " + username);

        try {
            if (userDAO.registerUser(user)) {
                JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                backToLogin();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Registration failed! Please check the console for errors.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Registration error: " + e.getMessage() +
                "\n\nPlease check:\n" +
                "1. MySQL is running\n" +
                "2. Database 'lms_db' exists\n" +
                "3. Database credentials are correct",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void backToLogin() {
        loginFrame.setVisible(true);
        this.dispose();
    }
}

