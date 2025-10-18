package com.lms.ui;

import com.lms.dao.QuizDAO;
import com.lms.model.Quiz;
import com.lms.model.QuizQuestion;
import com.lms.model.User;
import com.lms.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quiz Frame - Take quizzes and assessments
 */
public class QuizFrame extends JFrame {
    private User currentUser;
    private int courseId;
    private int quizId;
    private QuizDAO quizDAO;
    private Quiz quiz;
    private List<QuizQuestion> questions;
    private Map<Integer, String> answers;
    private int currentQuestionIndex = 0;
    
    private JLabel lblQuestionNumber;
    private JTextArea txtQuestion;
    private ButtonGroup optionGroup;
    private JRadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD;
    private RoundedButton btnPrevious, btnNext, btnSubmit;
    
    public QuizFrame(User user, int courseId, int quizId) {
        this.currentUser = user;
        this.courseId = courseId;
        this.quizId = quizId;
        this.quizDAO = new QuizDAO();
        this.quiz = quizDAO.getQuizById(quizId);
        this.questions = quizDAO.getQuizQuestions(quizId);
        this.answers = new HashMap<>();
        initComponents();

        // Load first question after UI is initialized
        if (questions != null && !questions.isEmpty()) {
            SwingUtilities.invokeLater(() -> loadQuestion(0));
        } else {
            JOptionPane.showMessageDialog(this,
                "This quiz has no questions available.",
                "No Questions",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void initComponents() {
        setTitle("LMS - Quiz");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(230, 126, 34));
        headerPanel.setPreferredSize(new Dimension(900, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(230, 126, 34));
        
        JLabel lblTitle = new JLabel(quiz.getQuizTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblInfo = new JLabel(String.format("Total Questions: %d | Total Marks: %d | Duration: %d mins",
            questions.size(), quiz.getTotalMarks(), quiz.getDurationMinutes()));
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo.setForeground(new Color(236, 240, 241));
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblInfo);
        
        if (quiz.getDescription() != null && !quiz.getDescription().isEmpty()) {
            JLabel lblDesc = new JLabel(quiz.getDescription());
            lblDesc.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblDesc.setForeground(new Color(236, 240, 241));
            lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
            titlePanel.add(Box.createVerticalStrut(3));
            titlePanel.add(lblDesc);
        }
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Question panel
        RoundedPanel questionPanel = new RoundedPanel();
        questionPanel.setLayout(null);
        questionPanel.setBackgroundColor(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Question number
        lblQuestionNumber = new JLabel();
        lblQuestionNumber.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblQuestionNumber.setForeground(new Color(230, 126, 34));
        lblQuestionNumber.setBounds(0, 0, 800, 30);
        questionPanel.add(lblQuestionNumber);
        
        // Question text
        txtQuestion = new JTextArea();
        txtQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtQuestion.setForeground(new Color(44, 62, 80));
        txtQuestion.setBackground(Color.WHITE);
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setEditable(false);
        txtQuestion.setFocusable(false);
        txtQuestion.setBounds(0, 40, 800, 100);
        questionPanel.add(txtQuestion);
        
        // Options
        optionGroup = new ButtonGroup();
        
        rbOptionA = createOptionRadioButton("A");
        rbOptionA.setBounds(0, 140, 800, 40);
        questionPanel.add(rbOptionA);
        
        rbOptionB = createOptionRadioButton("B");
        rbOptionB.setBounds(0, 190, 800, 40);
        questionPanel.add(rbOptionB);
        
        rbOptionC = createOptionRadioButton("C");
        rbOptionC.setBounds(0, 240, 800, 40);
        questionPanel.add(rbOptionC);
        
        rbOptionD = createOptionRadioButton("D");
        rbOptionD.setBounds(0, 290, 800, 40);
        questionPanel.add(rbOptionD);
        
        optionGroup.add(rbOptionA);
        optionGroup.add(rbOptionB);
        optionGroup.add(rbOptionC);
        optionGroup.add(rbOptionD);
        
        JPanel questionWrapper = new JPanel(new BorderLayout());
        questionWrapper.setBackground(new Color(236, 240, 241));
        questionWrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        questionWrapper.add(questionPanel, BorderLayout.CENTER);
        
        mainPanel.add(questionWrapper, BorderLayout.CENTER);
        
        // Bottom navigation panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(900, 80));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Previous button
        btnPrevious = new RoundedButton("← Previous");
        btnPrevious.setPreferredSize(new Dimension(150, 50));
        btnPrevious.setBackgroundColor(new Color(149, 165, 166));
        btnPrevious.addActionListener(e -> navigateQuestion(-1));
        
        // Progress label
        JLabel lblProgress = new JLabel("Question 1 of " + questions.size());
        lblProgress.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblProgress.setForeground(new Color(127, 140, 141));
        lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Next button
        btnNext = new RoundedButton("Next →");
        btnNext.setPreferredSize(new Dimension(150, 50));
        btnNext.setBackgroundColor(new Color(52, 152, 219));
        btnNext.addActionListener(e -> navigateQuestion(1));
        
        // Submit button
        btnSubmit = new RoundedButton("Submit Quiz");
        btnSubmit.setPreferredSize(new Dimension(150, 50));
        btnSubmit.setBackgroundColor(new Color(46, 204, 113));
        btnSubmit.setVisible(false);
        btnSubmit.addActionListener(e -> submitQuiz());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(btnNext);
        rightPanel.add(btnSubmit);
        
        bottomPanel.add(btnPrevious, BorderLayout.WEST);
        bottomPanel.add(lblProgress, BorderLayout.CENTER);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JRadioButton createOptionRadioButton(String option) {
        JRadioButton rb = new JRadioButton();
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rb.setForeground(new Color(44, 62, 80));
        rb.setBackground(Color.WHITE);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rb.addActionListener(e -> saveAnswer(option));
        return rb;
    }
    
    private void loadQuestion(int index) {
        if (index < 0 || index >= questions.size()) return;

        currentQuestionIndex = index;
        QuizQuestion question = questions.get(index);

        lblQuestionNumber.setText("Question " + (index + 1) + " of " + questions.size() +
                                 " (Marks: " + question.getMarks() + ")");
        txtQuestion.setText(question.getQuestionText());

        rbOptionA.setText("A. " + question.getOptionA());
        rbOptionB.setText("B. " + question.getOptionB());
        rbOptionC.setText("C. " + question.getOptionC());
        rbOptionD.setText("D. " + question.getOptionD());

        // Load saved answer if exists
        String savedAnswer = answers.get(question.getQuestionId());
        optionGroup.clearSelection();
        if (savedAnswer != null) {
            switch (savedAnswer) {
                case "A": rbOptionA.setSelected(true); break;
                case "B": rbOptionB.setSelected(true); break;
                case "C": rbOptionC.setSelected(true); break;
                case "D": rbOptionD.setSelected(true); break;
            }
        }

        // Update button visibility
        btnPrevious.setEnabled(index > 0);
        btnNext.setVisible(index < questions.size() - 1);
        btnSubmit.setVisible(index == questions.size() - 1);

        // Force UI refresh
        revalidate();
        repaint();
    }
    
    private void saveAnswer(String answer) {
        QuizQuestion question = questions.get(currentQuestionIndex);
        answers.put(question.getQuestionId(), answer);
    }
    
    private void navigateQuestion(int direction) {
        int newIndex = currentQuestionIndex + direction;
        if (newIndex >= 0 && newIndex < questions.size()) {
            loadQuestion(newIndex);
        }
    }
    
    private void submitQuiz() {
        int unanswered = questions.size() - answers.size();
        
        if (unanswered > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "You have " + unanswered + " unanswered question(s). Do you want to submit anyway?",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Calculate score
        double score = 0;
        int totalMarks = 0;
        
        for (QuizQuestion question : questions) {
            totalMarks += question.getMarks();
            String userAnswer = answers.get(question.getQuestionId());
            if (userAnswer != null && userAnswer.equals(question.getCorrectAnswer())) {
                score += question.getMarks();
            }
        }
        
        // Save quiz attempt
        if (quizDAO.submitQuizAttempt(quizId, currentUser.getUserId(), score, totalMarks)) {
            double percentage = (score / totalMarks) * 100;
            String message = String.format(
                "Quiz submitted successfully!\n\n" +
                "Your Score: %.2f / %d\n" +
                "Percentage: %.2f%%\n" +
                "Status: %s",
                score, totalMarks, percentage,
                percentage >= quiz.getPassingMarks() ? "PASSED" : "FAILED"
            );
            
            JOptionPane.showMessageDialog(this,
                message,
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE);
            
            backToCourse();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to submit quiz. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backToCourse() {
        CourseDetailsFrame courseDetails = new CourseDetailsFrame(currentUser, courseId);
        courseDetails.setVisible(true);
        this.dispose();
    }
}

