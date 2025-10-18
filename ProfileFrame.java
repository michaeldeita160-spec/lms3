package com.lms.ui;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;

/**
 * Profile Frame - User profile management
 */
public class ProfileFrame extends JFrame {
    private User currentUser;
    private UserDAO userDAO;
    
    // UI Components
    private JLabel lblProfileImage;
    private CustomTextField txtFullName;
    private CustomTextField txtEmail;
    private CustomTextField txtPhone;
    private JTextArea txtBio;
    private CustomTextField txtDateOfBirth;
    private CustomTextField txtAddress;
    private String selectedImagePath;
    
    public ProfileFrame(User user) {
        this.currentUser = user;
        this.userDAO = new UserDAO();
        initComponents();
        loadUserData();
    }
    
    private void initComponents() {
        setTitle("LMS - My Profile");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = createHeader();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(mainContainer);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("My Profile");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        RoundedButton btnBack = new RoundedButton("← Back to Dashboard");
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.setBackgroundColor(new Color(52, 152, 219));
        btnBack.addActionListener(e -> backToDashboard());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Profile Image Section
        RoundedPanel imagePanel = createProfileImageSection();
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(imagePanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Personal Information Section
        RoundedPanel personalInfoPanel = createPersonalInfoSection();
        personalInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(personalInfoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Contact Information Section
        RoundedPanel contactInfoPanel = createContactInfoSection();
        contactInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(contactInfoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // About Section
        RoundedPanel aboutPanel = createAboutSection();
        aboutPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(aboutPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Action Buttons
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(buttonPanel);
        
        return contentPanel;
    }
    
    private RoundedPanel createProfileImageSection() {
        RoundedPanel panel = new RoundedPanel();
        panel.setLayout(null);
        panel.setMaximumSize(new Dimension(800, 200));
        panel.setPreferredSize(new Dimension(800, 200));
        panel.setBackground(Color.WHITE);
        
        JLabel lblSectionTitle = new JLabel("Profile Picture");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSectionTitle.setForeground(new Color(44, 62, 80));
        lblSectionTitle.setBounds(30, 20, 200, 30);
        panel.add(lblSectionTitle);
        
        // Profile image display
        lblProfileImage = new JLabel();
        lblProfileImage.setBounds(320, 60, 120, 120);
        lblProfileImage.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        lblProfileImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfileImage.setVerticalAlignment(SwingConstants.CENTER);
        lblProfileImage.setOpaque(true);
        lblProfileImage.setBackground(new Color(236, 240, 241));
        
        // Default avatar icon
        lblProfileImage.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblProfileImage.setForeground(new Color(149, 165, 166));
        lblProfileImage.setText(String.valueOf(currentUser.getFullName().charAt(0)).toUpperCase());
        
        panel.add(lblProfileImage);
        
        // Upload button
        RoundedButton btnUpload = new RoundedButton("Choose Image");
        btnUpload.setBounds(470, 90, 150, 40);
        btnUpload.setBackgroundColor(new Color(52, 152, 219));
        btnUpload.addActionListener(e -> chooseProfileImage());
        panel.add(btnUpload);
        
        // Remove button
        RoundedButton btnRemove = new RoundedButton("Remove");
        btnRemove.setBounds(470, 140, 150, 40);
        btnRemove.setBackgroundColor(new Color(231, 76, 60));
        btnRemove.addActionListener(e -> removeProfileImage());
        panel.add(btnRemove);
        
        return panel;
    }
    
    private RoundedPanel createPersonalInfoSection() {
        RoundedPanel panel = new RoundedPanel();
        panel.setLayout(null);
        panel.setMaximumSize(new Dimension(800, 200));
        panel.setPreferredSize(new Dimension(800, 200));
        panel.setBackground(Color.WHITE);
        
        JLabel lblSectionTitle = new JLabel("Personal Information");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSectionTitle.setForeground(new Color(44, 62, 80));
        lblSectionTitle.setBounds(30, 20, 250, 30);
        panel.add(lblSectionTitle);
        
        // Full Name
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFullName.setBounds(30, 70, 150, 25);
        panel.add(lblFullName);
        
        txtFullName = new CustomTextField("Enter your full name");
        txtFullName.setBounds(30, 95, 340, 40);
        panel.add(txtFullName);
        
        // Date of Birth
        JLabel lblDOB = new JLabel("Date of Birth (YYYY-MM-DD)");
        lblDOB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDOB.setBounds(430, 70, 250, 25);
        panel.add(lblDOB);
        
        txtDateOfBirth = new CustomTextField("YYYY-MM-DD");
        txtDateOfBirth.setBounds(430, 95, 340, 40);
        panel.add(txtDateOfBirth);
        
        // Username (Read-only)
        JLabel lblUsername = new JLabel("Username (cannot be changed)");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setBounds(30, 145, 250, 25);
        panel.add(lblUsername);
        
        JLabel lblUsernameValue = new JLabel(currentUser.getUsername());
        lblUsernameValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsernameValue.setForeground(new Color(127, 140, 141));
        lblUsernameValue.setBounds(250, 145, 200, 25);
        panel.add(lblUsernameValue);
        
        return panel;
    }
    
    private RoundedPanel createContactInfoSection() {
        RoundedPanel panel = new RoundedPanel();
        panel.setLayout(null);
        panel.setMaximumSize(new Dimension(800, 150));
        panel.setPreferredSize(new Dimension(800, 150));
        panel.setBackground(Color.WHITE);
        
        JLabel lblSectionTitle = new JLabel("Contact Information");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSectionTitle.setForeground(new Color(44, 62, 80));
        lblSectionTitle.setBounds(30, 20, 250, 30);
        panel.add(lblSectionTitle);
        
        // Email
        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setBounds(30, 60, 150, 25);
        panel.add(lblEmail);
        
        txtEmail = new CustomTextField("Enter your email");
        txtEmail.setBounds(30, 85, 340, 40);
        panel.add(txtEmail);
        
        // Phone
        JLabel lblPhone = new JLabel("Phone Number");
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPhone.setBounds(430, 60, 150, 25);
        panel.add(lblPhone);
        
        txtPhone = new CustomTextField("Enter your phone number");
        txtPhone.setBounds(430, 85, 340, 40);
        panel.add(txtPhone);
        
        return panel;
    }
    
    private RoundedPanel createAboutSection() {
        RoundedPanel panel = new RoundedPanel();
        panel.setLayout(null);
        panel.setMaximumSize(new Dimension(800, 250));
        panel.setPreferredSize(new Dimension(800, 250));
        panel.setBackground(Color.WHITE);
        
        JLabel lblSectionTitle = new JLabel("About Me");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSectionTitle.setForeground(new Color(44, 62, 80));
        lblSectionTitle.setBounds(30, 20, 250, 30);
        panel.add(lblSectionTitle);
        
        // Bio
        JLabel lblBio = new JLabel("Bio / Description");
        lblBio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBio.setBounds(30, 60, 150, 25);
        panel.add(lblBio);
        
        txtBio = new JTextArea();
        txtBio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBio.setLineWrap(true);
        txtBio.setWrapStyleWord(true);
        txtBio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JScrollPane bioScroll = new JScrollPane(txtBio);
        bioScroll.setBounds(30, 85, 740, 60);
        bioScroll.setBorder(null);
        panel.add(bioScroll);
        
        // Address
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblAddress.setBounds(30, 155, 150, 25);
        panel.add(lblAddress);
        
        txtAddress = new CustomTextField("Enter your address");
        txtAddress.setBounds(30, 180, 740, 40);
        panel.add(txtAddress);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setMaximumSize(new Dimension(800, 60));
        panel.setBackground(new Color(236, 240, 241));
        
        RoundedButton btnSave = new RoundedButton("Save Changes");
        btnSave.setPreferredSize(new Dimension(200, 50));
        btnSave.setBackgroundColor(new Color(46, 204, 113));
        btnSave.addActionListener(e -> saveProfile());
        panel.add(btnSave);
        
        RoundedButton btnCancel = new RoundedButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(200, 50));
        btnCancel.setBackgroundColor(new Color(149, 165, 166));
        btnCancel.addActionListener(e -> backToDashboard());
        panel.add(btnCancel);
        
        return panel;
    }
    
    private void loadUserData() {
        // Reload user data from database to get latest info
        User user = userDAO.getUserById(currentUser.getUserId());
        if (user != null) {
            currentUser = user;
        }
        
        txtFullName.setText(currentUser.getFullName());
        txtEmail.setText(currentUser.getEmail());
        txtPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        txtBio.setText(currentUser.getBio() != null ? currentUser.getBio() : "");
        txtDateOfBirth.setText(currentUser.getDateOfBirth() != null ? currentUser.getDateOfBirth() : "");
        txtAddress.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        
        // Load profile image if exists
        if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
            loadProfileImage(currentUser.getProfileImage());
        }
    }
    
    private void chooseProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Create profile_images directory if it doesn't exist
                File profileDir = new File("profile_images");
                if (!profileDir.exists()) {
                    profileDir.mkdirs();
                }
                
                // Copy file to profile_images directory
                String fileName = currentUser.getUserId() + "_" + selectedFile.getName();
                File destFile = new File(profileDir, fileName);
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                selectedImagePath = destFile.getPath();
                loadProfileImage(selectedImagePath);
                
                JOptionPane.showMessageDialog(this,
                    "Image selected! Click 'Save Changes' to update your profile.",
                    "Image Selected",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error loading image: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadProfileImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
                Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblProfileImage.setIcon(new ImageIcon(scaledImg));
                lblProfileImage.setText("");
            }
        } catch (IOException ex) {
            System.err.println("Error loading profile image: " + ex.getMessage());
        }
    }
    
    private void removeProfileImage() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to remove your profile picture?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            selectedImagePath = null;
            lblProfileImage.setIcon(null);
            lblProfileImage.setText(String.valueOf(currentUser.getFullName().charAt(0)).toUpperCase());
            
            // Update database
            userDAO.updateProfileImage(currentUser.getUserId(), null);
            
            JOptionPane.showMessageDialog(this,
                "Profile picture removed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void saveProfile() {
        System.out.println("=== SAVE PROFILE CLICKED ===");

        // Validation
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();

        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);

        if (fullName.isEmpty() || email.isEmpty()) {
            System.err.println("Validation failed: Full Name or Email is empty");
            JOptionPane.showMessageDialog(this,
                "Full Name and Email are required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            System.err.println("Validation failed: Invalid email format");
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("Validation passed. Updating user object...");

        // Update user object
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);
        currentUser.setPhone(txtPhone.getText().trim());
        currentUser.setBio(txtBio.getText().trim());
        currentUser.setDateOfBirth(txtDateOfBirth.getText().trim());
        currentUser.setAddress(txtAddress.getText().trim());

        System.out.println("Calling userDAO.updateUser()...");

        // Save to database
        boolean updateSuccess = userDAO.updateUser(currentUser);

        System.out.println("Update result: " + updateSuccess);

        if (updateSuccess) {
            // Update profile image if changed
            if (selectedImagePath != null) {
                System.out.println("Updating profile image: " + selectedImagePath);
                boolean imageUpdateSuccess = userDAO.updateProfileImage(currentUser.getUserId(), selectedImagePath);
                System.out.println("Image update result: " + imageUpdateSuccess);
                currentUser.setProfileImage(selectedImagePath);
            }

            System.out.println("✓ Profile updated successfully!");
            JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.err.println("✗ Failed to update profile!");
            JOptionPane.showMessageDialog(this,
                "Failed to update profile. Please check the console for errors.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backToDashboard() {
        DashboardFrame dashboard = new DashboardFrame(currentUser);
        dashboard.setVisible(true);
        this.dispose();
    }
}

