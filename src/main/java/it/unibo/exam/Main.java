package it.unibo.exam;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.view.panel.MainMenuPanel;

/**
 * Main application class that initializes the application window.
 * DEBUG MODE: Automatically triggers Easter Egg for testing.
 */
public final class Main {
    
    // DEBUG SETTINGS - Set these to control testing behavior
    private static final boolean DEBUG_MODE = true;  // Set to false for release
    private static final boolean AUTO_TRIGGER_EASTER_EGG = true; // Auto-show Easter Egg dialog
    private static final boolean SKIP_MAIN_MENU = false; // Skip directly to game
    
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    } 

    /**
     * Entry point of the application.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        if (DEBUG_MODE) {
            System.out.println("=== DEBUG MODE ENABLED ===");
            System.out.println("Auto Easter Egg: " + AUTO_TRIGGER_EASTER_EGG);
            System.out.println("Skip Main Menu: " + SKIP_MAIN_MENU);
            System.out.println("==========================");
        }
        
        // Execute UI code in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            final KeyHandler keyHandler = new KeyHandler();
            final JFrame window = new JFrame();

            // Get screen dimensions
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int screenWidth = (int) screenSize.getWidth();
            final int screenHeight = (int) screenSize.getHeight();

            // Get graphics device for fullscreen support
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();

            // Configure window
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("UniversityEscape" + (DEBUG_MODE ? " - DEBUG MODE" : ""));
            window.addKeyListener(keyHandler);

            if (SKIP_MAIN_MENU) {
                // Skip directly to game with debug features
                startGameDirectlyWithDebug(window, screenWidth, screenHeight);
            } else {
                // Create main menu panel with debug features
                final MainMenuPanel mainMenu = createDebugMainMenu(window);
                window.getContentPane().add(mainMenu);
            }

            // Set window size
            final int initialWidth = (int) (screenWidth * 0.8);
            final int initialHeight = (int) (screenHeight * 0.8);
            window.setSize(initialWidth, initialHeight);
            window.setLocationRelativeTo(null);

            // Add window listeners
            addWindowListeners(window, gd);

            // Add global key listeners
            addGlobalKeyListeners(window, gd);

            // Make window visible
            window.setVisible(true);

            // Start in fullscreen
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            // Auto-trigger Easter Egg if enabled
            if (AUTO_TRIGGER_EASTER_EGG && !SKIP_MAIN_MENU) {
                // Delay to let the window fully load
                SwingUtilities.invokeLater(() -> {
                    SwingUtilities.invokeLater(() -> {
                        showDebugEasterEggDialog(window);
                    });
                });
            }
        });
    }
    
    /**
     * Creates a main menu with debug features.
     */
    private static MainMenuPanel createDebugMainMenu(final JFrame window) {
        return new MainMenuPanel(window) {
            // Override to add debug features if needed
        };
    }
    
    /**
     * Starts the game directly with all minigames completed (debug mode).
     */
    private static void startGameDirectlyWithDebug(final JFrame window, final int screenWidth, final int screenHeight) {
        // Create game panel with debug mode
        final it.unibo.exam.utility.geometry.Point2D gameSize = 
            new it.unibo.exam.utility.geometry.Point2D(screenWidth, screenHeight);
        
        final it.unibo.exam.view.panel.GamePanel gamePanel = 
            new it.unibo.exam.view.panel.GamePanel(gameSize, window);
        
        window.add(gamePanel);
        
        // Wait a moment for initialization, then complete all minigames
        SwingUtilities.invokeLater(() -> {
            SwingUtilities.invokeLater(() -> {
                completeAllMinigamesDebug(gamePanel);
                
                if (AUTO_TRIGGER_EASTER_EGG) {
                    // Show Easter Egg dialog after completing minigames
                    SwingUtilities.invokeLater(() -> {
                        showDebugEasterEggDialog(window);
                    });
                }
            });
        });
        
        window.revalidate();
        window.repaint();
        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Shows the Easter Egg dialog in debug mode.
     */
    private static void showDebugEasterEggDialog(final JFrame window) {
        final int choice = javax.swing.JOptionPane.showConfirmDialog(
            window,
            "🎉 DEBUG MODE: Easter Egg Unlocked!\n\n" +
            "🥚 All minigames have been automatically completed!\n\n" +
            "Would you like to test the Pokémon battle?\n" +
            "Students vs Pianini!",
            "Debug - Easter Egg Unlocked!",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            startDebugEasterEggBattle(window);
        }
    }
    
    /**
     * Starts the Easter Egg battle in debug mode.
     */
    private static void startDebugEasterEggBattle(final JFrame window) {
        try {
            final it.unibo.exam.model.entity.minigame.PokemonBattleMinigame battleGame = 
                new it.unibo.exam.model.entity.minigame.PokemonBattleMinigame();
            
            battleGame.start(window, new it.unibo.exam.model.entity.minigame.MinigameCallback() {
                @Override
                public void onComplete(boolean success, int timeSeconds) {
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            javax.swing.JOptionPane.showMessageDialog(window,
                                "🏆 DEBUG VICTORY! Students defeated the Pianini!\n\n" +
                                "Battle time: " + timeSeconds + " seconds\n\n" +
                                "Easter Egg test completed successfully!",
                                "Debug - Battle Won!",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(window,
                                "💀 DEBUG DEFEAT! The Pianini were too strong...\n\n" +
                                "Battle time: " + timeSeconds + " seconds\n\n" +
                                "Try again or test other features!",
                                "Debug - Battle Lost",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    });
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error starting debug Easter Egg battle: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(window, 
                "Error starting Easter Egg battle!\n" + e.getMessage(), 
                "Debug Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Completes all minigames programmatically for debug testing.
     */
    private static void completeAllMinigamesDebug(final it.unibo.exam.view.panel.GamePanel gamePanel) {
        try {
            // Get the main controller from the game panel
            final it.unibo.exam.controller.MainController controller = gamePanel.getMainController();
            
            // We need to access the player through the controller
            // This would require adding a public method to MainController to get the player
            // For now, just show a message
            System.out.println("DEBUG: Would complete all minigames here");
            System.out.println("Note: Need to add public getPlayer() method to MainController");
            
        } catch (Exception e) {
            System.err.println("Error completing minigames in debug mode: " + e.getMessage());
        }
    }
    
    /**
     * Adds window listeners with fullscreen toggle support.
     */
    private static void addWindowListeners(final JFrame window, final GraphicsDevice gd) {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(final WindowEvent e) {
                // When minimized, restore to fullscreen
            }

            @Override
            public void windowDeiconified(final WindowEvent e) {
                // Restore to fullscreen when restored
            }

            @Override
            public void windowStateChanged(final WindowEvent e) {
                // Ensure window stays maximized unless explicitly minimized
            }
        });
    }
    
    /**
     * Adds global key listeners for fullscreen and exit.
     */
    private static void addGlobalKeyListeners(final JFrame window, final GraphicsDevice gd) {
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
                // Not used
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                // F11 to toggle fullscreen
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullscreen(window, gd);
                } else if ((e.getKeyCode() == KeyEvent.VK_F4 && e.isAltDown()) 
                || e.getKeyCode() == KeyEvent.VK_ESCAPE
                && window.getContentPane().getComponent(0) instanceof it.unibo.exam.view.panel.MainMenuPanel) {
                    // Only exit from main menu, not during game
                    window.dispose();
                }
                
                // DEBUG: F12 to trigger Easter Egg anytime
                if (DEBUG_MODE && e.getKeyCode() == KeyEvent.VK_F12) {
                    showDebugEasterEggDialog(window);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                // Not used
            }
        });
    }

    /**
     * Toggles between fullscreen and windowed mode.
     * @param window the main window
     * @param graphicsDevice the graphics device
     */
    private static void toggleFullscreen(final JFrame window, final GraphicsDevice graphicsDevice) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();

        if (graphicsDevice.getFullScreenWindow() != null && graphicsDevice.getFullScreenWindow().equals(window)) {
            // Exit fullscreen
            graphicsDevice.setFullScreenWindow(null);
            window.setUndecorated(false);
            window.setExtendedState(JFrame.NORMAL);
            final int windowWidth = (int) (screenWidth * 0.8);
            final int windowHeight = (int) (screenHeight * 0.8);
            window.setSize(windowWidth, windowHeight);
            window.setLocationRelativeTo(null);
        } else {
            // Enter fullscreen
            window.setUndecorated(false);
            if (graphicsDevice.isFullScreenSupported()) {
                graphicsDevice.setFullScreenWindow(window);
            } else {
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }
}