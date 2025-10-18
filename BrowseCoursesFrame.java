package com.lms.ui;

import com.lms.dao.CourseDAO;
import com.lms.model.Course;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Browse Courses Frame - View and enroll in available courses
 */
public class BrowseCoursesFrame extends JFrame {
    private User currentUser;
    private CourseDAO courseDAO;
    private JPanel coursesPanel;
    
    public BrowseCoursesFrame(User user) {
        this.currentUser = user;
        this.courseDAO = new CourseDAO();
        initComponents();
        loadCourses();
    }
    
    private void initComponents() {
        setTitle("LMS - Browse Courses");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1000, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("Browse Courses");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        
        RoundedButton btnBack = new RoundedButton("Back to Dashboard");
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.setBackgroundColor(new Color(52, 73, 94));
        btnBack.addActionListener(e -> backToDashboard());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Courses panel
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(new Color(236, 240, 241));
        coursesPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void loadCourses() {
        coursesPanel.removeAll();
        
        List<Course> allCourses = courseDAO.getAllCourses();
        
        if (allCourses.isEmpty()) {
            JLabel lblNoData = new JLabel("No courses available at the moment.");
            lblNoData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblNoData.setForeground(new Color(127, 140, 141));
            coursesPanel.add(lblNoData);
        } else {
            for (Course course : allCourses) {
                RoundedPanel courseCard = createCourseCard(course);
                courseCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                coursesPanel.add(courseCard);
                coursesPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }
    
    private RoundedPanel createCourseCard(Course course) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setMaximumSize(new Dimension(920, 150));
        card.setPreferredSize(new Dimension(920, 150));
        card.setBackgroundColor(Color.WHITE);
        card.setHasShadow(true);
        
        // Course name
        JLabel lblCourseName = new JLabel(course.getCourseName());
        lblCourseName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblCourseName.setForeground(new Color(44, 62, 80));
        lblCourseName.setBounds(25, 20, 600, 30);
        card.add(lblCourseName);
        
        // Course code
        JLabel lblCourseCode = new JLabel("Code: " + course.getCourseCode());
        lblCourseCode.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCourseCode.setForeground(new Color(41, 128, 185));
        lblCourseCode.setBounds(25, 55, 200, 20);
        card.add(lblCourseCode);
        
        // Teacher
        String teacherName = course.getTeacherName() != null ? course.getTeacherName() : "Not assigned";
        JLabel lblTeacher = new JLabel("Instructor: " + teacherName);
        lblTeacher.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTeacher.setForeground(new Color(127, 140, 141));
        lblTeacher.setBounds(25, 80, 300, 20);
        card.add(lblTeacher);
        
        // Description
        String description = course.getDescription();
        if (description != null && description.length() > 100) {
            description = description.substring(0, 100) + "...";
        }
        JLabel lblDescription = new JLabel("<html>" + (description != null ? description : "No description available") + "</html>");
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescription.setForeground(new Color(127, 140, 141));
        lblDescription.setBounds(25, 105, 600, 30);
        card.add(lblDescription);
        
        // Check if already enrolled
        boolean isEnrolled = courseDAO.isUserEnrolled(currentUser.getUserId(), course.getCourseId());
        
        if (isEnrolled) {
            JLabel lblEnrolled = new JLabel("✓ Enrolled");
            lblEnrolled.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblEnrolled.setForeground(new Color(46, 204, 113));
            lblEnrolled.setBounds(700, 20, 100, 30);
            card.add(lblEnrolled);
            
            RoundedButton btnView = new RoundedButton("View Course");
            btnView.setBounds(700, 90, 180, 40);
            btnView.setBackgroundColor(new Color(41, 128, 185));
            btnView.addActionListener(e -> viewCourse(course.getCourseId()));
            card.add(btnView);
        } else {
            RoundedButton btnEnroll = new RoundedButton("Enroll Now");
            btnEnroll.setBounds(700, 90, 180, 40);
            btnEnroll.setBackgroundColor(new Color(46, 204, 113));
            btnEnroll.addActionListener(e -> enrollInCourse(course));
            card.add(btnEnroll);
        }
        
        return card;
    }
    
    private void enrollInCourse(Course course) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to enroll in " + course.getCourseName() + "?",
            "Confirm Enrollment",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (courseDAO.enrollUserInCourse(currentUser.getUserId(), course.getCourseId())) {
                JOptionPane.showMessageDialog(this,
                    "Successfully enrolled in " + course.getCourseName() + "!",
                    "Enrollment Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                loadCourses(); // Refresh the list
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to enroll in the course. Please try again.",
                    "Enrollment Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewCourse(int courseId) {
        CourseDetailsFrame detailsFrame = new CourseDetailsFrame(currentUser, courseId);
        detailsFrame.setVisible(true);
        this.dispose();
    }
    
    private void backToDashboard() {
        DashboardFrame dashboard = new DashboardFrame(currentUser);
        dashboard.setVisible(true);
        this.dispose();
    }
}

