package it.unibo.exam.model.entity.minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Kahoot-style quiz minigame with multiple choice questions.
 * Players must answer questions correctly within a time limit to succeed.
 */
public class KahootMinigame implements Minigame {
    
    private JFrame gameFrame;
    private MinigameCallback callback;
    private long startTime;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    
    // List of quiz questions with answers and correct answer indices
    private final List<Question> questions = List.of(
        new Question("What is the capital of Italy?", 
                    List.of("Rome", "Milan", "Naples", "Turin"), 0),
        new Question("Who wrote 'The Divine Comedy'?", 
                    List.of("Petrarch", "Boccaccio", "Dante", "Manzoni"), 2),
        new Question("What is the result of 2 + 2?", 
                    List.of("3", "4", "5", "6"), 1),
        new Question("In what year did the Berlin Wall fall?", 
                    List.of("1987", "1988", "1989", "1990"), 2),
        new Question("Which planet is closest to the Sun?", 
                    List.of("Venus", "Mercury", "Earth", "Mars"), 1)
    );
    
    @Override
    public void start(JFrame parentFrame, MinigameCallback onComplete) {
        this.callback = onComplete;
        this.startTime = System.currentTimeMillis();
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        
        createGameWindow(parentFrame);
        showNextQuestion();
    }
    
    @Override
    public void stop() {
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }
    
    @Override
    public String getName() {
        return "Quiz Kahoot";
    }
    
    @Override
    public String getDescription() {
        return "Answer all questions correctly to win!";
    }
    
    /**
     * Creates and configures the main game window.
     * 
     * @param parentFrame the parent frame for centering
     */
    private void createGameWindow(JFrame parentFrame) {
        gameFrame = new JFrame("Quiz Kahoot - " + getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(600, 400);
        gameFrame.setLocationRelativeTo(parentFrame);
        gameFrame.setResizable(false);
        
        // Add window close listener to handle incomplete games
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (callback != null) {
                    callback.onComplete(false, getElapsedSeconds());
                }
            }
        });
    }
    
    /**
     * Displays the next question in the quiz sequence.
     * If all questions are answered, completes the quiz.
     */
    private void showNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            // Quiz completed - check if player passed
            gameFrame.dispose();
            if (callback != null) {
                boolean success = correctAnswers >= questions.size() * 0.6; // 60% to pass
                callback.onComplete(success, getElapsedSeconds());
            }
            return;
        }
        
        Question question = questions.get(currentQuestionIndex);
        
        // Clear previous content
        gameFrame.getContentPane().removeAll();
        gameFrame.setLayout(new BorderLayout());
        
        // Create header panel with progress information
        JPanel headerPanel = createHeaderPanel();
        
        // Create question display panel
        JPanel questionPanel = createQuestionPanel(question);
        
        // Create answer buttons panel
        JPanel answersPanel = createAnswersPanel(question);
        
        // Add all panels to the frame
        gameFrame.add(headerPanel, BorderLayout.NORTH);
        gameFrame.add(questionPanel, BorderLayout.CENTER);
        gameFrame.add(answersPanel, BorderLayout.SOUTH);
        
        gameFrame.revalidate();
        gameFrame.repaint();
        gameFrame.setVisible(true);
    }
    
    /**
     * Creates the header panel with progress and score information.
     * 
     * @return configured header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel progressLabel = new JLabel(String.format("Question %d/%d", 
                                        currentQuestionIndex + 1, questions.size()));
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel scoreLabel = new JLabel(String.format("Score: %d/%d", 
                                     correctAnswers, currentQuestionIndex));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.add(scoreLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Creates the question display panel.
     * 
     * @param question the question to display
     * @return configured question panel
     */
    private JPanel createQuestionPanel(Question question) {
        JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel questionLabel = new JLabel("<html><div style='text-align: center;'>" + 
                                        question.getQuestion() + "</div></html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionPanel.add(questionLabel);
        
        return questionPanel;
    }
    
    /**
     * Creates the panel with answer buttons.
     * 
     * @param question the question with answers
     * @return configured answers panel
     */
    private JPanel createAnswersPanel(Question question) {
        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        answersPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        answersPanel.setBackground(Color.WHITE);
        
        List<String> answers = question.getAnswers();
        Color[] buttonColors = {
            new Color(255, 77, 77),   // Red
            new Color(77, 150, 255),  // Blue
            new Color(255, 195, 77),  // Yellow
            new Color(77, 255, 150)   // Green
        };
        
        for (int i = 0; i < answers.size(); i++) {
            final int answerIndex = i;
            JButton answerButton = new JButton(answers.get(i));
            answerButton.setFont(new Font("Arial", Font.BOLD, 14));
            answerButton.setBackground(buttonColors[i]);
            answerButton.setForeground(Color.WHITE);
            answerButton.setFocusPainted(false);
            answerButton.setBorderPainted(false);
            answerButton.setPreferredSize(new Dimension(200, 60));
            
            answerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleAnswer(answerIndex, question.getCorrectAnswer());
                }
            });
            
            answersPanel.add(answerButton);
        }
        
        return answersPanel;
    }
    
    /**
     * Handles the player's answer selection.
     * 
     * @param selectedAnswer the index of the selected answer
     * @param correctAnswer the index of the correct answer
     */
    private void handleAnswer(int selectedAnswer, int correctAnswer) {
        if (selectedAnswer == correctAnswer) {
            correctAnswers++;
            showFeedback("Correct!", new Color(77, 255, 150));
        } else {
            showFeedback("Wrong! The correct answer was: " + 
                        questions.get(currentQuestionIndex).getAnswers().get(correctAnswer), 
                        new Color(255, 77, 77));
        }
        
        currentQuestionIndex++;
        
        // Wait a moment before showing the next question
        Timer timer = new Timer(2000, e -> showNextQuestion());
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Shows feedback to the player after answering.
     * 
     * @param message the feedback message
     * @param color the background color for the feedback
     */
    private void showFeedback(String message, Color color) {
        gameFrame.getContentPane().removeAll();
        
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBackground(color);
        
        JLabel feedbackLabel = new JLabel("<html><div style='text-align: center;'>" + 
                                        message + "</div></html>");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 24));
        feedbackLabel.setForeground(Color.WHITE);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        feedbackPanel.add(feedbackLabel, BorderLayout.CENTER);
        gameFrame.add(feedbackPanel);
        
        gameFrame.revalidate();
        gameFrame.repaint();
    }
    
    /**
     * Calculates the elapsed time since the minigame started.
     * 
     * @return elapsed time in seconds
     */
    private int getElapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
    
    /**
     * Inner class representing a quiz question with multiple choice answers.
     */
    private static class Question {
        private final String question;
        private final List<String> answers;
        private final int correctAnswer;
        
        /**
         * Creates a new question.
         * 
         * @param question the question text
         * @param answers list of possible answers
         * @param correctAnswer index of the correct answer (0-based)
         */
        public Question(String question, List<String> answers, int correctAnswer) {
            this.question = question;
            this.answers = List.copyOf(answers); // Immutable copy
            this.correctAnswer = correctAnswer;
        }
        
        public String getQuestion() { return question; }
        public List<String> getAnswers() { return answers; }
        public int getCorrectAnswer() { return correctAnswer; }
    }
}