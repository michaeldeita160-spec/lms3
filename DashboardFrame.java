package com.lms.ui;

import com.lms.dao.CourseDAO;
import com.lms.model.CourseProgress;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Dashboard Frame - Shows user progress for each course
 */
public class DashboardFrame extends JFrame {
    private User currentUser;
    private CourseDAO courseDAO;
    private JPanel contentPanel;
    private JPanel sidebarPanel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.courseDAO = new CourseDAO();
        refreshUserData(); // Reload user data to get latest profile info
        initComponents();
        loadDashboardData();
    }

    private void refreshUserData() {
        // Reload user data from database to get updated profile image
        com.lms.dao.UserDAO userDAO = new com.lms.dao.UserDAO();
        User updatedUser = userDAO.getUserById(currentUser.getUserId());
        if (updatedUser != null) {
            this.currentUser = updatedUser;
        }
    }
    
    private void initComponents() {
        setTitle("LMS - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(236, 240, 241));
        
        // Sidebar
        createSidebar();
        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        
        // Content area
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = createHeader();
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content scroll pane
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(236, 240, 241));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        add(mainContainer);
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(null);
        sidebarPanel.setPreferredSize(new Dimension(250, 700));
        sidebarPanel.setBackground(new Color(44, 62, 80));

        // Logo/Title
        JLabel lblLogo = new JLabel("LMS");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBounds(20, 20, 100, 40);
        sidebarPanel.add(lblLogo);

        // Profile Image
        JLabel lblProfileImage = new JLabel();
        lblProfileImage.setBounds(20, 75, 60, 60);
        lblProfileImage.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        lblProfileImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfileImage.setVerticalAlignment(SwingConstants.CENTER);
        lblProfileImage.setOpaque(true);
        lblProfileImage.setBackground(new Color(52, 73, 94));

        // Load profile image or show default
        if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
            try {
                File imageFile = new File(currentUser.getProfileImage());
                if (imageFile.exists()) {
                    BufferedImage img = ImageIO.read(imageFile);
                    Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    lblProfileImage.setIcon(new ImageIcon(scaledImg));
                } else {
                    // Show default avatar with initial
                    lblProfileImage.setFont(new Font("Segoe UI", Font.BOLD, 24));
                    lblProfileImage.setForeground(Color.WHITE);
                    lblProfileImage.setText(String.valueOf(currentUser.getFullName().charAt(0)).toUpperCase());
                }
            } catch (IOException e) {
                // Show default avatar with initial
                lblProfileImage.setFont(new Font("Segoe UI", Font.BOLD, 24));
                lblProfileImage.setForeground(Color.WHITE);
                lblProfileImage.setText(String.valueOf(currentUser.getFullName().charAt(0)).toUpperCase());
            }
        } else {
            // Show default avatar with initial
            lblProfileImage.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblProfileImage.setForeground(Color.WHITE);
            lblProfileImage.setText(String.valueOf(currentUser.getFullName().charAt(0)).toUpperCase());
        }
        sidebarPanel.add(lblProfileImage);

        // User info (moved to the right of profile image)
        JLabel lblUserName = new JLabel(currentUser.getFullName());
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUserName.setForeground(Color.WHITE);
        lblUserName.setBounds(90, 80, 150, 25);
        sidebarPanel.add(lblUserName);

        JLabel lblUserRole = new JLabel(currentUser.getRole().toUpperCase());
        lblUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUserRole.setForeground(new Color(189, 195, 199));
        lblUserRole.setBounds(90, 105, 150, 20);
        sidebarPanel.add(lblUserRole);
        
        // Menu buttons
        int yPos = 160;
        int btnHeight = 45;
        int spacing = 10;
        
        RoundedButton btnDashboard = createMenuButton("Dashboard", yPos);
        btnDashboard.setBackgroundColor(new Color(52, 73, 94));
        btnDashboard.addActionListener(e -> loadDashboardData());
        sidebarPanel.add(btnDashboard);
        yPos += btnHeight + spacing;
        
        RoundedButton btnCourses = createMenuButton("My Courses", yPos);
        btnCourses.addActionListener(e -> openCourses());
        sidebarPanel.add(btnCourses);
        yPos += btnHeight + spacing;
        
        RoundedButton btnGrades = createMenuButton("Grades & Reports", yPos);
        btnGrades.addActionListener(e -> openGrades());
        sidebarPanel.add(btnGrades);
        yPos += btnHeight + spacing;
        
        RoundedButton btnBrowse = createMenuButton("Browse Courses", yPos);
        btnBrowse.addActionListener(e -> openBrowseCourses());
        sidebarPanel.add(btnBrowse);
        yPos += btnHeight + spacing;

        RoundedButton btnProfile = createMenuButton("My Profile", yPos);
        btnProfile.addActionListener(e -> openProfile());
        sidebarPanel.add(btnProfile);
        yPos += btnHeight + spacing;

        // Logout button at bottom
        RoundedButton btnLogout = createMenuButton("Logout", 600);
        btnLogout.setBackgroundColor(new Color(192, 57, 43));
        btnLogout.addActionListener(e -> logout());
        sidebarPanel.add(btnLogout);
    }
    
    private RoundedButton createMenuButton(String text, int yPos) {
        RoundedButton btn = new RoundedButton(text);
        btn.setBounds(15, yPos, 220, 45);
        btn.setBackgroundColor(new Color(52, 73, 94));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        return btn;
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(44, 62, 80));
        
        JLabel lblSubtitle = new JLabel("Welcome back, " + currentUser.getFullName() + "!");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(127, 140, 141));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblSubtitle);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private void loadDashboardData() {
        // Clear content
        Component scrollPane = contentPanel.getComponent(1);
        JScrollPane sp = (JScrollPane) scrollPane;
        JViewport viewport = sp.getViewport();
        JPanel mainContent = (JPanel) viewport.getView();
        mainContent.removeAll();
        
        // Get course progress
        List<CourseProgress> progressList = courseDAO.getCourseProgress(currentUser.getUserId());
        
        if (progressList.isEmpty()) {
            JLabel lblNoData = new JLabel("You are not enrolled in any courses yet.");
            lblNoData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblNoData.setForeground(new Color(127, 140, 141));
            lblNoData.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainContent.add(lblNoData);
        } else {
            // Add progress cards for each course
            for (CourseProgress progress : progressList) {
                RoundedPanel courseCard = createCourseProgressCard(progress);
                courseCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                mainContent.add(courseCard);
                mainContent.add(Box.createVerticalStrut(15));
            }
        }
        
        mainContent.revalidate();
        mainContent.repaint();
    }
    
    private RoundedPanel createCourseProgressCard(CourseProgress progress) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setMaximumSize(new Dimension(900, 180));
        card.setPreferredSize(new Dimension(900, 180));
        card.setBackgroundColor(Color.WHITE);
        card.setHasShadow(true);
        
        // Course name
        JLabel lblCourseName = new JLabel(progress.getCourseName());
        lblCourseName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCourseName.setForeground(new Color(44, 62, 80));
        lblCourseName.setBounds(25, 20, 500, 25);
        card.add(lblCourseName);
        
        // Course code
        JLabel lblCourseCode = new JLabel(progress.getCourseCode());
        lblCourseCode.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCourseCode.setForeground(new Color(127, 140, 141));
        lblCourseCode.setBounds(25, 45, 200, 20);
        card.add(lblCourseCode);
        
        // Progress label
        JLabel lblProgress = new JLabel("Course Progress");
        lblProgress.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblProgress.setForeground(new Color(52, 73, 94));
        lblProgress.setBounds(25, 80, 150, 20);
        card.add(lblProgress);
        
        // Progress bar
        ProgressBar progressBar = new ProgressBar(0, 100);
        progressBar.setValue((int) progress.getProgressPercentage());
        progressBar.setString(String.format("%.1f%% Complete", progress.getProgressPercentage()));
        progressBar.setBounds(25, 105, 400, 30);
        
        // Set color based on progress
        if (progress.getProgressPercentage() >= 75) {
            progressBar.setBarColor(new Color(46, 204, 113));
        } else if (progress.getProgressPercentage() >= 50) {
            progressBar.setBarColor(new Color(241, 196, 15));
        } else {
            progressBar.setBarColor(new Color(231, 76, 60));
        }
        card.add(progressBar);
        
        // Lessons info
        JLabel lblLessons = new JLabel(String.format("Lessons: %d / %d", 
            progress.getCompletedLessons(), progress.getTotalLessons()));
        lblLessons.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLessons.setForeground(new Color(127, 140, 141));
        lblLessons.setBounds(25, 145, 200, 20);
        card.add(lblLessons);
        
        // Average score
        JLabel lblScore = new JLabel(String.format("Avg Score: %.1f%%", progress.getAverageScore()));
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblScore.setForeground(new Color(41, 128, 185));
        lblScore.setBounds(650, 20, 150, 25);
        card.add(lblScore);
        
        // View course button
        RoundedButton btnView = new RoundedButton("View Course");
        btnView.setBounds(650, 120, 200, 40);
        btnView.setBackgroundColor(new Color(41, 128, 185));
        btnView.addActionListener(e -> openCourseDetails(progress.getCourseId()));
        card.add(btnView);
        
        return card;
    }
    
    private void openCourses() {
        CourseFrame courseFrame = new CourseFrame(currentUser);
        courseFrame.setVisible(true);
        this.dispose();
    }
    
    private void openGrades() {
        try {
            System.out.println("Opening Grades & Reports...");
            ReportFrame reportFrame = new ReportFrame(currentUser);
            reportFrame.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            System.err.println("Error opening Grades & Reports:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening Grades & Reports: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openBrowseCourses() {
        try {
            System.out.println("Opening Browse Courses...");
            BrowseCoursesFrame browseFrame = new BrowseCoursesFrame(currentUser);
            browseFrame.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            System.err.println("Error opening Browse Courses:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening Browse Courses: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openCourseDetails(int courseId) {
        CourseDetailsFrame detailsFrame = new CourseDetailsFrame(currentUser, courseId);
        detailsFrame.setVisible(true);
        this.dispose();
    }

    private void openProfile() {
        ProfileFrame profileFrame = new ProfileFrame(currentUser);
        profileFrame.setVisible(true);
        this.dispose();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}

