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
 * Updated to open in fullscreen mode.
 */
public final class Main {
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    } 

    /**
     * Entry point of the application.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
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

            // Create main menu panel
            final MainMenuPanel mainMenu = new MainMenuPanel(window);

            // Configure window
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("UniversityEscape");
            window.addKeyListener(keyHandler);

            // Add main menu panel
            window.getContentPane().add(mainMenu);

            // Set fullscreen mode
            final int initialWidth = (int) (screenWidth * 0.8);
            final int initialHeight = (int) (screenHeight * 0.8);
            window.setSize(initialWidth, initialHeight);
            window.setLocationRelativeTo(null);

            // Add window listeners
            window.addWindowListener(new WindowAdapter() {
                /**
                 * Handle window iconification events.
                 * @param e window event
                 */
                @Override
                public void windowIconified(final WindowEvent e) {
                    // When minimized, restore to fullscreen
                }

                /**
                 * Handle window deiconification events.
                 * @param e window event
                 */
                @Override
                public void windowDeiconified(final WindowEvent e) {
                    // Restore to fullscreen when restored

                }

                /**
                 * Handle window state changes.
                 * @param e window event
                 */
                @Override
                public void windowStateChanged(final WindowEvent e) {
                    // Ensure window stays maximized unless explicitly minimized

                }
            });

            // Add global key listener for fullscreen toggle (F11) and exit (Alt+F4, Escape)
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
                    && window.getContentPane().getComponent(0) instanceof MainMenuPanel) {
                        // Only exit from main menu, not during game
                        window.dispose();
                    }
                }

                @Override
                public void keyReleased(final KeyEvent e) {
                    // Not used
                }
            });

            // Make window visible
            window.setVisible(true);

            // Ensure we start in fullscreen
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        if (graphicsDevice.getFullScreenWindow().equals(window)) {
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
