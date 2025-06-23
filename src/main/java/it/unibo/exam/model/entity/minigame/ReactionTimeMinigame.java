package it.unibo.exam.model.entity.minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Reaction time minigame that tests player reflexes.
 * Players must click when the screen turns green to test their reaction time.
 */
public class ReactionTimeMinigame implements Minigame {
    
    private JFrame gameFrame;
    private MinigameCallback callback;
    private long startTime;
    private JPanel colorPanel;
    private JLabel instructionLabel;
    private JLabel roundLabel;
    private Timer waitTimer;
    private Timer gameTimer;
    private long reactionStartTime;
    private boolean canClick = false;
    private int round = 0;
    private final int TOTAL_ROUNDS = 5;
    private int successfulRounds = 0;
    
    @Override
    public void start(JFrame parentFrame, MinigameCallback onComplete) {
        this.callback = onComplete;
        this.startTime = System.currentTimeMillis();
        this.round = 0;
        this.successfulRounds = 0;
        
        createGameWindow(parentFrame);
        startNextRound();
    }
    
    @Override
    public void stop() {
        if (waitTimer != null) waitTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }
    
    @Override
    public String getName() {
        return "Reaction Time";
    }
    
    @Override
    public String getDescription() {
        return "Click when it turns green!";
    }
    
    /**
     * Creates and configures the main game window.
     * 
     * @param parentFrame the parent frame for centering
     */
    private void createGameWindow(JFrame parentFrame) {
        gameFrame = new JFrame("Reaction Time - " + getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(500, 400);
        gameFrame.setLocationRelativeTo(parentFrame);
        gameFrame.setResizable(false);
        gameFrame.setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Create instruction panel
        instructionLabel = new JLabel("Wait for green...", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create clickable color panel
        colorPanel = new JPanel();
        colorPanel.setBackground(Color.RED);
        colorPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        colorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick();
            }
        });
        
        gameFrame.add(headerPanel, BorderLayout.NORTH);
        gameFrame.add(instructionLabel, BorderLayout.CENTER);
        gameFrame.add(colorPanel, BorderLayout.SOUTH);
        
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                stop();
                if (callback != null) {
                    callback.onComplete(false, getElapsedSeconds());
                }
            }
        });
        
        gameFrame.setVisible(true);
    }
    
    /**
     * Creates the header panel with title and round progress.
     * 
     * @return configured header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Reaction Test");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        roundLabel = new JLabel("Round 1/" + TOTAL_ROUNDS);
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(roundLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Starts the next round of the reaction test.
     */
    private void startNextRound() {
        if (round >= TOTAL_ROUNDS) {
            // Game completed - check success rate
            gameFrame.dispose();
            if (callback != null) {
                boolean success = successfulRounds >= TOTAL_ROUNDS * 0.6; // 60% success rate to pass
                callback.onComplete(success, getElapsedSeconds());
            }
            return;
        }
        
        round++;
        canClick = false;
        colorPanel.setBackground(Color.RED);
        instructionLabel.setText("Wait for green... (" + round + "/" + TOTAL_ROUNDS + ")");
        roundLabel.setText("Round " + round + "/" + TOTAL_ROUNDS);
        
        // Wait a random time between 1-5 seconds before showing green
        Random random = new Random();
        int waitTime = 1000 + random.nextInt(4000);
        
        waitTimer = new Timer(waitTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGreen();
            }
        });
        waitTimer.setRepeats(false);
        waitTimer.start();
    }
    
    /**
     * Shows the green signal for the player to click.
     */
    private void showGreen() {
        colorPanel.setBackground(Color.GREEN);
        instructionLabel.setText("CLICK NOW!");
        canClick = true;
        reactionStartTime = System.currentTimeMillis();
        
        // Set timeout after 2 seconds
        gameTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canClick) {
                    showResult("Too slow!", false);
                }
            }
        });
        gameTimer.setRepeats(false);
        gameTimer.start();
    }
    
    /**
     * Handles mouse clicks on the color panel.
     */
    private void handleClick() {
        if (canClick) {
            // Player clicked at the right time
            long reactionTime = System.currentTimeMillis() - reactionStartTime;
            canClick = false;
            if (gameTimer != null) gameTimer.stop();
            
            if (reactionTime < 1000) { // Less than 1 second is good
                successfulRounds++;
                showResult("Excellent! " + reactionTime + "ms", true);
            } else {
                showResult("Good! " + reactionTime + "ms", true);
            }
        } else if (colorPanel.getBackground().equals(Color.RED)) {
            // Player clicked too early
            showResult("Too early! Wait for green.", false);
        }
    }
    
    /**
     * Shows the result of the current round and proceeds to next.
     * 
     * @param message the result message
     * @param success whether the round was successful
     */
    private void showResult(String message, boolean success) {
        instructionLabel.setText(message);
        colorPanel.setBackground(success ? Color.YELLOW : Color.GRAY);
        
        // Wait before starting next round
        Timer timer = new Timer(1500, e -> startNextRound());
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Calculates the elapsed time since the minigame started.
     * 
     * @return elapsed time in seconds
     */
    private int getElapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
}