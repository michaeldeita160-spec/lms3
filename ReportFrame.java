package com.lms.ui;

import com.lms.dao.CourseDAO;
import com.lms.dao.GradeDAO;
import com.lms.dao.QuizDAO;
import com.lms.model.Course;
import com.lms.model.Grade;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Report Frame - View grades and performance reports
 */
public class ReportFrame extends JFrame {
    private User currentUser;
    private GradeDAO gradeDAO;
    private QuizDAO quizDAO;
    private CourseDAO courseDAO;
    private JComboBox<Course> cmbCourses;
    private JTable tblQuizResults;
    private DefaultTableModel tableModel;
    
    public ReportFrame(User user) {
        this.currentUser = user;
        this.gradeDAO = new GradeDAO();
        this.quizDAO = new QuizDAO();
        this.courseDAO = new CourseDAO();
        initComponents();
        loadGrades();
    }
    
    private void initComponents() {
        setTitle("LMS - Grades & Reports");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(1000, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("Grades & Reports");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        
        RoundedButton btnBack = new RoundedButton("Back to Dashboard");
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.setBackgroundColor(new Color(44, 62, 80));
        btnBack.addActionListener(e -> backToDashboard());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Overall grades section
        JLabel lblGradesTitle = new JLabel("Overall Course Grades");
        lblGradesTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGradesTitle.setForeground(new Color(44, 62, 80));
        lblGradesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblGradesTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Grades panel
        JPanel gradesPanel = new JPanel();
        gradesPanel.setLayout(new BoxLayout(gradesPanel, BoxLayout.Y_AXIS));
        gradesPanel.setBackground(new Color(236, 240, 241));
        gradesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        List<Grade> grades = gradeDAO.getGradesByUser(currentUser.getUserId());
        
        if (grades.isEmpty()) {
            JLabel lblNoGrades = new JLabel("No grades available yet.");
            lblNoGrades.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblNoGrades.setForeground(new Color(127, 140, 141));
            lblNoGrades.setAlignmentX(Component.LEFT_ALIGNMENT);
            gradesPanel.add(lblNoGrades);
        } else {
            for (Grade grade : grades) {
                RoundedPanel gradeCard = createGradeCard(grade);
                gradeCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                gradesPanel.add(gradeCard);
                gradesPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        contentPanel.add(gradesPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Quiz results section
        JLabel lblQuizTitle = new JLabel("Quiz Results");
        lblQuizTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblQuizTitle.setForeground(new Color(44, 62, 80));
        lblQuizTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblQuizTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Course selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.setBackground(new Color(236, 240, 241));
        selectorPanel.setMaximumSize(new Dimension(920, 50));
        selectorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSelectCourse = new JLabel("Select Course:");
        lblSelectCourse.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSelectCourse.setForeground(new Color(44, 62, 80));
        selectorPanel.add(lblSelectCourse);
        
        cmbCourses = new JComboBox<>();
        cmbCourses.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCourses.setPreferredSize(new Dimension(300, 35));
        cmbCourses.addActionListener(e -> loadQuizResults());
        selectorPanel.add(cmbCourses);
        
        contentPanel.add(selectorPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Quiz results table
        RoundedPanel tablePanel = new RoundedPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setMaximumSize(new Dimension(920, 300));
        tablePanel.setPreferredSize(new Dimension(920, 300));
        tablePanel.setBackgroundColor(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] columnNames = {"Quiz Title", "Score", "Total Marks", "Percentage", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblQuizResults = new JTable(tableModel);
        tblQuizResults.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblQuizResults.setRowHeight(35);
        tblQuizResults.setGridColor(new Color(189, 195, 199));
        tblQuizResults.setSelectionBackground(new Color(189, 195, 199));

        // Custom header renderer with forced colors
        JTableHeader header = tblQuizResults.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Create a completely custom header renderer that forces our colors
        DefaultTableCellRenderer customHeaderRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(52, 152, 219));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        };

        // Apply custom renderer to all header columns
        for (int i = 0; i < tblQuizResults.getColumnModel().getColumnCount(); i++) {
            tblQuizResults.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
        }

        // Center align data cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < tblQuizResults.getColumnCount(); i++) {
            tblQuizResults.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tblQuizResults);
        scrollPane.setBorder(null);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tablePanel);
        
        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(mainScrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void loadGrades() {
        // Load courses into combo box
        List<Course> courses = courseDAO.getEnrolledCourses(currentUser.getUserId());
        cmbCourses.removeAllItems();
        for (Course course : courses) {
            cmbCourses.addItem(course);
        }
        
        if (!courses.isEmpty()) {
            loadQuizResults();
        }
    }
    
    private void loadQuizResults() {
        tableModel.setRowCount(0);
        
        Course selectedCourse = (Course) cmbCourses.getSelectedItem();
        if (selectedCourse == null) return;
        
        List<Object[]> attempts = quizDAO.getQuizAttempts(currentUser.getUserId(), selectedCourse.getCourseId());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        
        for (Object[] attempt : attempts) {
            String quizTitle = (String) attempt[0];
            double score = (Double) attempt[1];
            int totalMarks = (Integer) attempt[2];
            double percentage = (score / totalMarks) * 100;
            String date = sdf.format(attempt[3]);
            
            tableModel.addRow(new Object[]{
                quizTitle,
                String.format("%.2f", score),
                totalMarks,
                String.format("%.2f%%", percentage),
                date
            });
        }
    }
    
    private RoundedPanel createGradeCard(Grade grade) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(null);
        card.setMaximumSize(new Dimension(920, 100));
        card.setPreferredSize(new Dimension(920, 100));
        card.setBackgroundColor(Color.WHITE);
        card.setHasShadow(true);
        
        // Course name
        JLabel lblCourseName = new JLabel(grade.getCourseName());
        lblCourseName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCourseName.setForeground(new Color(44, 62, 80));
        lblCourseName.setBounds(25, 20, 400, 25);
        card.add(lblCourseName);
        
        // Remarks
        if (grade.getRemarks() != null && !grade.getRemarks().isEmpty()) {
            JLabel lblRemarks = new JLabel(grade.getRemarks());
            lblRemarks.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblRemarks.setForeground(new Color(127, 140, 141));
            lblRemarks.setBounds(25, 50, 400, 20);
            card.add(lblRemarks);
        }
        
        // Score
        JLabel lblScore = new JLabel(String.format("%.2f%%", grade.getTotalScore()));
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblScore.setForeground(new Color(52, 152, 219));
        lblScore.setBounds(650, 20, 120, 40);
        card.add(lblScore);
        
        // Letter grade
        JLabel lblLetterGrade = new JLabel(grade.getLetterGrade());
        lblLetterGrade.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Color based on grade
        Color gradeColor;
        switch (grade.getLetterGrade()) {
            case "A": gradeColor = new Color(46, 204, 113); break;
            case "B": gradeColor = new Color(52, 152, 219); break;
            case "C": gradeColor = new Color(241, 196, 15); break;
            case "D": gradeColor = new Color(230, 126, 34); break;
            default: gradeColor = new Color(231, 76, 60);
        }
        lblLetterGrade.setForeground(gradeColor);
        lblLetterGrade.setBounds(800, 30, 60, 30);
        card.add(lblLetterGrade);
        
        return card;
    }
    
    private void backToDashboard() {
        DashboardFrame dashboard = new DashboardFrame(currentUser);
        dashboard.setVisible(true);
        this.dispose();
    }
}

