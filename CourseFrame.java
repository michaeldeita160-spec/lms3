package com.lms.ui;

import com.lms.dao.CourseDAO;
import com.lms.model.Course;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Course Frame - Display user's enrolled courses
 */
public class CourseFrame extends JFrame {
    private User currentUser;
    private CourseDAO courseDAO;
    private JPanel coursesPanel;
    
    public CourseFrame(User user) {
        this.currentUser = user;
        this.courseDAO = new CourseDAO();
        initComponents();
        loadCourses();
    }
    
    private void initComponents() {
        setTitle("LMS - My Courses");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(1000, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("My Courses");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        
        RoundedButton btnBack = new RoundedButton("Back to Dashboard");
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.setBackgroundColor(new Color(44, 62, 80));
        btnBack.addActionListener(e -> backToDashboard());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Courses panel
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(0, 2, 20, 20));
        coursesPanel.setBackground(new Color(236, 240, 241));
        coursesPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void loadCourses() {
        coursesPanel.removeAll();
        
        List<Course> enrolledCourses = courseDAO.getEnrolledCourses(currentUser.getUserId());
        
        if (enrolledCourses.isEmpty()) {
            coursesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JLabel lblNoData = new JLabel("You are not enrolled in any courses yet.");
            lblNoData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblNoData.setForeground(new Color(127, 140, 141));
            coursesPanel.add(lblNoData);
        } else {
            for (Course course : enrolledCourses) {
                RoundedPanel courseCard = createCourseCard(course);
                coursesPanel.add(courseCard);
            }
        }
        
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }
    
    private RoundedPanel createCourseCard(Course course) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setPreferredSize(new Dimension(420, 220));
        card.setBackgroundColor(Color.WHITE);
        card.setHasShadow(true);
        
        // Color bar at top
        JPanel colorBar = new JPanel();
        colorBar.setBounds(0, 0, 420, 8);
        Color[] colors = {
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(155, 89, 182),
            new Color(241, 196, 15),
            new Color(230, 126, 34)
        };
        colorBar.setBackground(colors[course.getCourseId() % colors.length]);
        card.add(colorBar);
        
        // Course name
        JLabel lblCourseName = new JLabel("<html>" + course.getCourseName() + "</html>");
        lblCourseName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCourseName.setForeground(new Color(44, 62, 80));
        lblCourseName.setBounds(20, 25, 380, 50);
        card.add(lblCourseName);
        
        // Course code
        JLabel lblCourseCode = new JLabel(course.getCourseCode());
        lblCourseCode.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCourseCode.setForeground(new Color(127, 140, 141));
        lblCourseCode.setBounds(20, 80, 200, 20);
        card.add(lblCourseCode);
        
        // Teacher
        String teacherName = course.getTeacherName() != null ? course.getTeacherName() : "Not assigned";
        JLabel lblTeacher = new JLabel("👤 " + teacherName);
        lblTeacher.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTeacher.setForeground(new Color(127, 140, 141));
        lblTeacher.setBounds(20, 105, 300, 20);
        card.add(lblTeacher);
        
        // View button
        RoundedButton btnView = new RoundedButton("View Course");
        btnView.setBounds(20, 150, 180, 45);
        btnView.setBackgroundColor(new Color(52, 152, 219));
        btnView.addActionListener(e -> viewCourse(course.getCourseId()));
        card.add(btnView);
        
        // Lessons button
        RoundedButton btnLessons = new RoundedButton("Lessons");
        btnLessons.setBounds(220, 150, 180, 45);
        btnLessons.setBackgroundColor(new Color(46, 204, 113));
        btnLessons.addActionListener(e -> viewLessons(course.getCourseId()));
        card.add(btnLessons);
        
        return card;
    }
    
    private void viewCourse(int courseId) {
        CourseDetailsFrame detailsFrame = new CourseDetailsFrame(currentUser, courseId);
        detailsFrame.setVisible(true);
        this.dispose();
    }
    
    private void viewLessons(int courseId) {
        LessonFrame lessonFrame = new LessonFrame(currentUser, courseId);
        lessonFrame.setVisible(true);
        this.dispose();
    }
    
    private void backToDashboard() {
        DashboardFrame dashboard = new DashboardFrame(currentUser);
        dashboard.setVisible(true);
        this.dispose();
    }
}

