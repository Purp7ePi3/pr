package it.unibo.exam.view.panel;

import it.unibo.exam.utility.geometry.Point2D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main menu panel for the game, displays play, options and exit buttons.
 * Updated to integrate with GamePanel.
 * @version 1.2
 */
public final class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int WIDTHBUTTON = 800;
    private static final int HEIGHTBUTTON = 80;
    private static final int BUTTONFONTSIZE = 30;
    private static final int BUTTONSPACING = 20;

    private GamePanel gamePanel; // Reference to track the game panel

    /**
     * Creates the main menu panel with buttons.
     *
     * @param window the parent JFrame window
     */
    public MainMenuPanel(final JFrame window) {
        createUI(window);
    }
    
    /**
     * Initialize the UI components.
     * This method is separated from the constructor to avoid calling overridable methods.
     *
     * @param window the parent JFrame window
     */
    private void createUI(final JFrame window) {
        // Panel layout for align the buttons
        super.setLayout(new BorderLayout());
        super.setPreferredSize(window.getSize());
        // Creates the panel where the buttons will stay
        final JPanel buttonPanel = createButtonPanel();
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(BUTTONSPACING, 0, BUTTONSPACING, 0); // Space between buttons
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; // Move the buttons ins the centre

        // Buttons creation
        final JButton playButton = new JButton("Gioca");
        final JButton optionsButton = new JButton("Opzioni");
        final JButton exitButton = new JButton("Esci");

        final Dimension buttonSize = new Dimension(WIDTHBUTTON, HEIGHTBUTTON);
        playButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        //Button's font
        final Font buttonFont = new Font("Arial", Font.BOLD, BUTTONFONTSIZE);
        playButton.setFont(buttonFont);
        optionsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        //Adding the buttons to the panel
        buttonPanel.add(playButton, gbc);
        gbc.gridy++; //Move the next button lower
        buttonPanel.add(optionsButton, gbc);
        gbc.gridy++; //Move the next button lower
        buttonPanel.add(exitButton, gbc);

        //Adding the panel to the window
        super.add(buttonPanel, BorderLayout.CENTER);

        //Action listener fo the button 'Play'
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                //Once you press 'Play', menu panel get removed and the game starts 
                startGame(window);
            }
        });

        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Options not implemented yet
                JOptionPane.showMessageDialog(window, "Opzioni non implementate ancora.");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Show confirmation dialog
                final int confirmed = JOptionPane.showConfirmDialog(window,
                        "Sei sicuro di voler uscire?", "Conferma uscita", JOptionPane.YES_NO_OPTION); 

                if (confirmed == JOptionPane.YES_OPTION) {
                    // Stop game if running
                    if (gamePanel != null) {
                        gamePanel.stopGame();
                    }
                    // Close the window instead of terminating the JVM
                    window.dispose();
                }
            }
        });
    }
    
    /**
     * Starts the game by removing the menu and adding the game panel.
     * 
     * @param window the parent window
     */
    private void startGame(final JFrame window) {
        // Remove the menu panel
        window.getContentPane().removeAll();
        
        // Create and add the game panel
        final Dimension windowSize = window.getSize();
        final Point2D gameSize = new Point2D(windowSize.width, windowSize.height);
        gamePanel = new GamePanel(gameSize);
        
        window.add(gamePanel);
        
        // Ensure the game panel gets focus for key input
        gamePanel.requestFocusInWindow();
        
        // Refresh the window
        window.revalidate();
        window.repaint();
        
        // Optional: Add ESC key listener to return to menu
        addReturnToMenuListener(window);
    }
    
    /**
     * Adds a listener to return to the main menu (ESC key).
     * 
     * @param window the parent window
     */
    private void addReturnToMenuListener(final JFrame window) {
        if (gamePanel != null) {
            gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                    .put(javax.swing.KeyStroke.getKeyStroke("ESCAPE"), "returnToMenu");
            
            gamePanel.getActionMap().put("returnToMenu", new javax.swing.AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(final ActionEvent e) {
                    returnToMenu(window);
                }
            });
        }
    }
    
    /**
     * Returns to the main menu from the game.
     * 
     * @param window the parent window
     */
    private void returnToMenu(final JFrame window) {
        final int confirmed = JOptionPane.showConfirmDialog(window,
                "Tornare al menu principale?", "Conferma", JOptionPane.YES_NO_OPTION);
        
        if (confirmed == JOptionPane.YES_OPTION) {
            // Stop the game
            if (gamePanel != null) {
                gamePanel.stopGame();
                gamePanel = null;
            }
            
            // Remove game panel and restore menu
            window.getContentPane().removeAll();
            window.add(this);
            window.revalidate();
            window.repaint();
        }
    }
    
    /**
     * Creates a panel for the buttons with custom background painting.
     *
     * @return JPanel with GridBagLayout
     */
    private JPanel createButtonPanel() {
        return new JPanel(new GridBagLayout());
    }
}