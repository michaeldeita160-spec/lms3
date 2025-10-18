package com.lms.ui;

import com.lms.dao.CourseDAO;
import com.lms.dao.LessonDAO;
import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Lesson Frame - Display all lessons for a course
 */
public class LessonFrame extends JFrame {
    private User currentUser;
    private int courseId;
    private CourseDAO courseDAO;
    private LessonDAO lessonDAO;
    private Course course;
    private JPanel lessonsPanel;
    
    public LessonFrame(User user, int courseId) {
        this.currentUser = user;
        this.courseId = courseId;
        this.courseDAO = new CourseDAO();
        this.lessonDAO = new LessonDAO();
        this.course = courseDAO.getCourseById(courseId);
        initComponents();
        loadLessons();
    }
    
    private void initComponents() {
        setTitle("LMS - Lessons");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setPreferredSize(new Dimension(900, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(46, 204, 113));
        
        JLabel lblTitle = new JLabel("Lessons");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCourse = new JLabel(course.getCourseName());
        lblCourse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCourse.setForeground(new Color(236, 240, 241));
        lblCourse.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblCourse);
        
        RoundedButton btnBack = new RoundedButton("Back to Course");
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.setBackgroundColor(new Color(44, 62, 80));
        btnBack.addActionListener(e -> backToCourse());
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Lessons panel
        lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(new Color(236, 240, 241));
        lessonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JScrollPane scrollPane = new JScrollPane(lessonsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void loadLessons() {
        lessonsPanel.removeAll();
        
        List<Lesson> lessons = lessonDAO.getLessonsByCourse(courseId, currentUser.getUserId());
        
        if (lessons.isEmpty()) {
            JLabel lblNoData = new JLabel("No lessons available for this course yet.");
            lblNoData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblNoData.setForeground(new Color(127, 140, 141));
            lblNoData.setAlignmentX(Component.LEFT_ALIGNMENT);
            lessonsPanel.add(lblNoData);
        } else {
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                RoundedPanel lessonCard = createLessonCard(lesson, i + 1);
                lessonCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                lessonsPanel.add(lessonCard);
                lessonsPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        lessonsPanel.revalidate();
        lessonsPanel.repaint();
    }
    
    private RoundedPanel createLessonCard(Lesson lesson, int number) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setMaximumSize(new Dimension(820, 120));
        card.setPreferredSize(new Dimension(820, 120));
        card.setBackgroundColor(Color.WHITE);
        card.setHasShadow(true);
        
        // Lesson number circle
        JPanel numberCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (lesson.isCompleted()) {
                    g2.setColor(new Color(46, 204, 113));
                } else {
                    g2.setColor(new Color(52, 152, 219));
                }
                g2.fillOval(0, 0, 60, 60);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                String text = String.valueOf(number);
                FontMetrics fm = g2.getFontMetrics();
                int x = (60 - fm.stringWidth(text)) / 2;
                int y = ((60 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        numberCircle.setBounds(25, 30, 60, 60);
        numberCircle.setOpaque(false);
        card.add(numberCircle);
        
        // Lesson title
        JLabel lblTitle = new JLabel(lesson.getLessonTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBounds(105, 25, 500, 30);
        card.add(lblTitle);
        
        // Status
        String statusText = lesson.isCompleted() ? "✓ Completed" : "Not completed";
        JLabel lblStatus = new JLabel(statusText);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStatus.setForeground(lesson.isCompleted() ? new Color(46, 204, 113) : new Color(127, 140, 141));
        lblStatus.setBounds(105, 55, 200, 20);
        card.add(lblStatus);
        
        // Content preview
        String content = lesson.getLessonContent();
        if (content != null && content.length() > 80) {
            content = content.substring(0, 80) + "...";
        }
        JLabel lblPreview = new JLabel("<html>" + (content != null ? content : "No content available") + "</html>");
        lblPreview.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPreview.setForeground(new Color(127, 140, 141));
        lblPreview.setBounds(105, 75, 500, 30);
        card.add(lblPreview);
        
        // View button
        RoundedButton btnView = new RoundedButton("View Lesson");
        btnView.setBounds(630, 35, 160, 50);
        btnView.setBackgroundColor(new Color(52, 152, 219));
        btnView.addActionListener(e -> viewLesson(lesson.getLessonId()));
        card.add(btnView);
        
        return card;
    }
    
    private void viewLesson(int lessonId) {
        LessonViewerFrame lessonViewer = new LessonViewerFrame(currentUser, courseId, lessonId);
        lessonViewer.setVisible(true);
        this.dispose();
    }
    
    private void backToCourse() {
        CourseDetailsFrame courseDetails = new CourseDetailsFrame(currentUser, courseId);
        courseDetails.setVisible(true);
        this.dispose();
    }
}

