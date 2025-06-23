package it.unibo.exam.model.entity.minigame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Memory card matching minigame.
 * Players must find all matching pairs of cards by flipping them two at a time.
 */
public class MemoryMinigame implements Minigame {
    
    private JFrame gameFrame;
    private MinigameCallback callback;
    private long startTime;
    private List<JButton> cardButtons;
    private List<String> cardValues;
    private JButton firstCard = null;
    private JButton secondCard = null;
    private int matchesFound = 0;
    private int totalPairs;
    private boolean canClick = true;
    
    // Symbols used for the memory cards
    private final String[] symbols = {"@", "#", "$", "%", "&", "*", "+", "="};
    
    @Override
    public void start(JFrame parentFrame, MinigameCallback onComplete) {
        this.callback = onComplete;
        this.startTime = System.currentTimeMillis();
        this.matchesFound = 0;
        this.totalPairs = 8;
        
        createGameWindow(parentFrame);
        initializeCards();
    }
    
    @Override
    public void stop() {
        if (gameFrame != null) {
            gameFrame.dispose();
        }
    }
    
    @Override
    public String getName() {
        return "Memory Game";
    }
    
    @Override
    public String getDescription() {
        return "Find all matching pairs of cards!";
    }
    
    /**
     * Creates and configures the main game window.
     * 
     * @param parentFrame the parent frame for centering
     */
    private void createGameWindow(JFrame parentFrame) {
        gameFrame = new JFrame("Memory Game - " + getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(600, 600);
        gameFrame.setLocationRelativeTo(parentFrame);
        gameFrame.setResizable(false);
        
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
     * Initializes the card grid and sets up the game layout.
     */
    private void initializeCards() {
        cardButtons = new ArrayList<>();
        cardValues = new ArrayList<>();
        
        // Create pairs of symbols
        for (int i = 0; i < totalPairs; i++) {
            cardValues.add(symbols[i]);
            cardValues.add(symbols[i]);
        }
        
        // Shuffle the cards randomly
        Collections.shuffle(cardValues);
        
        // Setup main layout
        gameFrame.setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Create card grid panel
        JPanel cardPanel = createCardPanel();
        
        gameFrame.add(headerPanel, BorderLayout.NORTH);
        gameFrame.add(cardPanel, BorderLayout.CENTER);
        
        gameFrame.setVisible(true);
    }
    
    /**
     * Creates the header panel with game title and instructions.
     * 
     * @return configured header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Memory Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel instructionLabel = new JLabel("Find the pairs!");
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(instructionLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Creates the grid panel containing all the card buttons.
     * 
     * @return configured card panel
     */
    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cardPanel.setBackground(new Color(230, 230, 230));
        
        for (int i = 0; i < cardValues.size(); i++) {
            final int index = i;
            JButton cardButton = createCardButton(index);
            cardButtons.add(cardButton);
            cardPanel.add(cardButton);
        }
        
        return cardPanel;
    }
    
    /**
     * Creates a single card button with appropriate styling and click handler.
     * 
     * @param index the index of this card in the card values list
     * @return configured card button
     */
    private JButton createCardButton(int index) {
        JButton cardButton = new JButton("?");
        cardButton.setFont(new Font("Arial", Font.BOLD, 24));
        cardButton.setBackground(new Color(100, 150, 200));
        cardButton.setForeground(Color.WHITE);
        cardButton.setFocusPainted(false);
        cardButton.setPreferredSize(new Dimension(80, 80));
        
        cardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canClick) {
                    flipCard(cardButton, index);
                }
            }
        });
        
        return cardButton;
    }
    
    /**
     * Handles flipping a card and checking for matches.
     * 
     * @param button the card button that was clicked
     * @param index the index of the card in the values list
     */
    private void flipCard(JButton button, int index) {
        // Only flip face-down cards
        if (button.getText().equals("?")) {
            // Reveal the card
            button.setText(cardValues.get(index));
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            
            if (firstCard == null) {
                // This is the first card flipped
                firstCard = button;
            } else if (secondCard == null) {
                // This is the second card flipped
                secondCard = button;
                canClick = false; // Prevent further clicks while checking
                
                // Check for match after a short delay
                Timer timer = new Timer(1000, e -> checkMatch());
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    /**
     * Checks if the two flipped cards match and handles the result.
     */
    private void checkMatch() {
        if (firstCard.getText().equals(secondCard.getText())) {
            // Match found - disable the cards and mark them as matched
            matchesFound++;
            firstCard.setEnabled(false);
            secondCard.setEnabled(false);
            firstCard.setBackground(new Color(77, 255, 150)); // Green for matched
            secondCard.setBackground(new Color(77, 255, 150));
            
            // Check if all pairs have been found
            if (matchesFound == totalPairs) {
                gameFrame.dispose();
                if (callback != null) {
                    callback.onComplete(true, getElapsedSeconds());
                }
                return;
            }
        } else {
            // No match - flip cards back face down
            firstCard.setText("?");
            secondCard.setText("?");
            firstCard.setBackground(new Color(100, 150, 200));
            secondCard.setBackground(new Color(100, 150, 200));
            firstCard.setForeground(Color.WHITE);
            secondCard.setForeground(Color.WHITE);
        }
        
        // Reset for next turn
        firstCard = null;
        secondCard = null;
        canClick = true;
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