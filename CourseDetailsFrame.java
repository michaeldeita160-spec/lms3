package com.lms.ui;

import com.lms.dao.CourseDAO;
import com.lms.dao.LessonDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.model.Quiz;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Course Details Frame - Shows detailed information about a course
 */
public class CourseDetailsFrame extends JFrame {
    private User currentUser;
    private int courseId;
    private CourseDAO courseDAO;
    private LessonDAO lessonDAO;
    private QuizDAO quizDAO;
    private Course course;
    
    public CourseDetailsFrame(User user, int courseId) {
        this.currentUser = user;
        this.courseId = courseId;
        this.courseDAO = new CourseDAO();
        this.lessonDAO = new LessonDAO();
        this.quizDAO = new QuizDAO();
        this.course = courseDAO.getCourseById(courseId);
        initComponents();
    }
    
    private void initComponents() {
        setTitle("LMS - Course Details");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(1000, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(155, 89, 182));
        
        JLabel lblTitle = new JLabel(course.getCourseName());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCode = new JLabel(course.getCourseCode());
        lblCode.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCode.setForeground(new Color(236, 240, 241));
        lblCode.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String teacherName = course.getTeacherName() != null ? course.getTeacherName() : "Not assigned";
        JLabel lblTeacher = new JLabel("Instructor: " + teacherName);
        lblTeacher.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTeacher.setForeground(new Color(236, 240, 241));
        lblTeacher.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblCode);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(lblTeacher);
        
        RoundedButton btnBack = new RoundedButton("Back");
        btnBack.setPreferredSize(new Dimension(120, 40));
        btnBack.setBackgroundColor(new Color(44, 62, 80));
        btnBack.addActionListener(e -> backToDashboard());
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Description section
        if (course.getDescription() != null && !course.getDescription().isEmpty()) {
            RoundedPanel descPanel = new RoundedPanel();
            descPanel.setLayout(new BorderLayout());
            descPanel.setMaximumSize(new Dimension(920, 120));
            descPanel.setPreferredSize(new Dimension(920, 120));
            descPanel.setBackgroundColor(Color.WHITE);
            descPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel lblDescTitle = new JLabel("Course Description");
            lblDescTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblDescTitle.setForeground(new Color(44, 62, 80));
            
            JTextArea txtDesc = new JTextArea(course.getDescription());
            txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtDesc.setForeground(new Color(127, 140, 141));
            txtDesc.setLineWrap(true);
            txtDesc.setWrapStyleWord(true);
            txtDesc.setEditable(false);
            txtDesc.setOpaque(false);
            
            descPanel.add(lblDescTitle, BorderLayout.NORTH);
            descPanel.add(txtDesc, BorderLayout.CENTER);
            
            descPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(descPanel);
            contentPanel.add(Box.createVerticalStrut(20));
        }
        
        // Lessons section
        JLabel lblLessonsTitle = new JLabel("Lessons");
        lblLessonsTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLessonsTitle.setForeground(new Color(44, 62, 80));
        lblLessonsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblLessonsTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        
        List<Lesson> lessons = lessonDAO.getLessonsByCourse(courseId, currentUser.getUserId());
        if (lessons.isEmpty()) {
            JLabel lblNoLessons = new JLabel("No lessons available yet.");
            lblNoLessons.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblNoLessons.setForeground(new Color(127, 140, 141));
            lblNoLessons.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(lblNoLessons);
        } else {
            for (Lesson lesson : lessons) {
                RoundedPanel lessonCard = createLessonCard(lesson);
                lessonCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(lessonCard);
                contentPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Quizzes section
        JLabel lblQuizzesTitle = new JLabel("Quizzes & Assessments");
        lblQuizzesTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblQuizzesTitle.setForeground(new Color(44, 62, 80));
        lblQuizzesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblQuizzesTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        
        List<Quiz> quizzes = quizDAO.getQuizzesByCourse(courseId);
        if (quizzes.isEmpty()) {
            JLabel lblNoQuizzes = new JLabel("No quizzes available yet.");
            lblNoQuizzes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblNoQuizzes.setForeground(new Color(127, 140, 141));
            lblNoQuizzes.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(lblNoQuizzes);
        } else {
            for (Quiz quiz : quizzes) {
                RoundedPanel quizCard = createQuizCard(quiz);
                quizCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(quizCard);
                contentPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private RoundedPanel createLessonCard(Lesson lesson) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setMaximumSize(new Dimension(920, 80));
        card.setPreferredSize(new Dimension(920, 80));
        card.setBackgroundColor(Color.WHITE);
        
        // Completion indicator
        JLabel lblStatus = new JLabel(lesson.isCompleted() ? "✓" : "○");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblStatus.setForeground(lesson.isCompleted() ? new Color(46, 204, 113) : new Color(189, 195, 199));
        lblStatus.setBounds(20, 25, 30, 30);
        card.add(lblStatus);
        
        // Lesson title
        JLabel lblTitle = new JLabel(lesson.getLessonTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBounds(60, 15, 600, 25);
        card.add(lblTitle);
        
        // Lesson order
        JLabel lblOrder = new JLabel("Lesson " + lesson.getLessonOrder());
        lblOrder.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOrder.setForeground(new Color(127, 140, 141));
        lblOrder.setBounds(60, 40, 200, 20);
        card.add(lblOrder);
        
        // View button
        RoundedButton btnView = new RoundedButton("View Lesson");
        btnView.setBounds(720, 20, 160, 40);
        btnView.setBackgroundColor(new Color(52, 152, 219));
        btnView.addActionListener(e -> viewLesson(lesson.getLessonId()));
        card.add(btnView);
        
        return card;
    }
    
    private RoundedPanel createQuizCard(Quiz quiz) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setMaximumSize(new Dimension(920, 80));
        card.setPreferredSize(new Dimension(920, 80));
        card.setBackgroundColor(Color.WHITE);
        
        // Quiz icon
        JLabel lblIcon = new JLabel("📝");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblIcon.setBounds(20, 25, 30, 30);
        card.add(lblIcon);
        
        // Quiz title
        JLabel lblTitle = new JLabel(quiz.getQuizTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBounds(60, 15, 500, 25);
        card.add(lblTitle);
        
        // Quiz info
        JLabel lblInfo = new JLabel(String.format("Total Marks: %d | Duration: %d mins", 
            quiz.getTotalMarks(), quiz.getDurationMinutes()));
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(127, 140, 141));
        lblInfo.setBounds(60, 40, 300, 20);
        card.add(lblInfo);
        
        // Take quiz button
        RoundedButton btnTake = new RoundedButton("Take Quiz");
        btnTake.setBounds(720, 20, 160, 40);
        btnTake.setBackgroundColor(new Color(230, 126, 34));
        btnTake.addActionListener(e -> takeQuiz(quiz.getQuizId()));
        card.add(btnTake);
        
        return card;
    }
    
    private void viewLesson(int lessonId) {
        LessonViewerFrame lessonViewer = new LessonViewerFrame(currentUser, courseId, lessonId);
        lessonViewer.setVisible(true);
        this.dispose();
    }
    
    private void takeQuiz(int quizId) {
        QuizFrame quizFrame = new QuizFrame(currentUser, courseId, quizId);
        quizFrame.setVisible(true);
        this.dispose();
    }
    
    private void backToDashboard() {
        DashboardFrame dashboard = new DashboardFrame(currentUser);
        dashboard.setVisible(true);
        this.dispose();
    }
}

