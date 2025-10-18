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
 * Lesson Viewer Frame - View individual lesson content
 */
public class LessonViewerFrame extends JFrame {
    private User currentUser;
    private int courseId;
    private int lessonId;
    private CourseDAO courseDAO;
    private LessonDAO lessonDAO;
    private Course course;
    private Lesson currentLesson;
    private List<Lesson> allLessons;
    private int currentIndex;
    
    public LessonViewerFrame(User user, int courseId, int lessonId) {
        this.currentUser = user;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.courseDAO = new CourseDAO();
        this.lessonDAO = new LessonDAO();
        this.course = courseDAO.getCourseById(courseId);
        this.allLessons = lessonDAO.getLessonsByCourse(courseId, currentUser.getUserId());
        
        // Find current lesson index
        for (int i = 0; i < allLessons.size(); i++) {
            if (allLessons.get(i).getLessonId() == lessonId) {
                currentIndex = i;
                currentLesson = allLessons.get(i);
                break;
            }
        }
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("LMS - Lesson Viewer");
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
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(52, 152, 219));
        
        JLabel lblTitle = new JLabel(currentLesson.getLessonTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCourse = new JLabel(course.getCourseName() + " - Lesson " + currentLesson.getLessonOrder());
        lblCourse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCourse.setForeground(new Color(236, 240, 241));
        lblCourse.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblCourse);
        
        RoundedButton btnBack = new RoundedButton("Back to Lessons");
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.setBackgroundColor(new Color(44, 62, 80));
        btnBack.addActionListener(e -> backToLessons());
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackgroundColor(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JTextArea txtContent = new JTextArea(currentLesson.getLessonContent());
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtContent.setForeground(new Color(44, 62, 80));
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setEditable(false);
        txtContent.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(txtContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel mainContentWrapper = new JPanel(new BorderLayout());
        mainContentWrapper.setBackground(new Color(236, 240, 241));
        mainContentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainContentWrapper.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(mainContentWrapper, BorderLayout.CENTER);
        
        // Bottom navigation panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(1000, 80));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Previous button
        RoundedButton btnPrevious = new RoundedButton("← Previous Lesson");
        btnPrevious.setPreferredSize(new Dimension(180, 50));
        btnPrevious.setBackgroundColor(new Color(149, 165, 166));
        btnPrevious.setEnabled(currentIndex > 0);
        btnPrevious.addActionListener(e -> navigateLesson(-1));
        
        // Center panel with completion checkbox
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.WHITE);
        
        JCheckBox chkCompleted = new JCheckBox("Mark as completed");
        chkCompleted.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chkCompleted.setForeground(new Color(44, 62, 80));
        chkCompleted.setBackground(Color.WHITE);
        chkCompleted.setSelected(currentLesson.isCompleted());
        chkCompleted.addActionListener(e -> {
            if (chkCompleted.isSelected()) {
                if (lessonDAO.markLessonCompleted(currentUser.getUserId(), currentLesson.getLessonId())) {
                    JOptionPane.showMessageDialog(this,
                        "Lesson marked as completed!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    currentLesson.setCompleted(true);
                }
            }
        });
        
        centerPanel.add(chkCompleted);
        
        // Next button
        RoundedButton btnNext = new RoundedButton("Next Lesson →");
        btnNext.setPreferredSize(new Dimension(180, 50));
        btnNext.setBackgroundColor(new Color(46, 204, 113));
        btnNext.setEnabled(currentIndex < allLessons.size() - 1);
        btnNext.addActionListener(e -> navigateLesson(1));
        
        bottomPanel.add(btnPrevious, BorderLayout.WEST);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);
        bottomPanel.add(btnNext, BorderLayout.EAST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void navigateLesson(int direction) {
        int newIndex = currentIndex + direction;
        if (newIndex >= 0 && newIndex < allLessons.size()) {
            Lesson nextLesson = allLessons.get(newIndex);
            LessonViewerFrame newViewer = new LessonViewerFrame(currentUser, courseId, nextLesson.getLessonId());
            newViewer.setVisible(true);
            this.dispose();
        }
    }
    
    private void backToLessons() {
        LessonFrame lessonFrame = new LessonFrame(currentUser, courseId);
        lessonFrame.setVisible(true);
        this.dispose();
    }
}

