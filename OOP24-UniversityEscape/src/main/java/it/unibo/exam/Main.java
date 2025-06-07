package it.unibo.exam;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.view.panel.MainMenuPanel;

/**
 * Main application class that initializes the application window.
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
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int screenWidth = (int) screenSize.getWidth();
            final int screenHeight = (int) screenSize.getHeight();

            // Set default size for when window is not maximized (80% of screen size)
            final int defaultWidth = (int) (screenWidth * 0.8);
            final int defaultHeight = (int) (screenHeight * 0.8);
            final MainMenuPanel mainMenu = new MainMenuPanel(window);

            // Set window here
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("UniversityEscape");
            window.addKeyListener(keyHandler);
            // Create and add the MainMenuPanel to the window
            window.getContentPane().add(mainMenu);
            window.setSize(defaultWidth, defaultHeight);
            // Add window listener to handle iconification (minimize) events
            window.addWindowListener(new WindowAdapter() {
                /***
                 * 
                 * @param e
                 * can resize window
                 */
                @Override
                public void windowIconified(final WindowEvent e) {
                    // When minimized, restore to default size instead of minimizing
                    window.setState(JFrame.NORMAL);
                }

                /***
                 * 
                 * @param e 
                 * set default size
                 */
                @Override
                public void windowDeiconified(final WindowEvent e) {
                    window.setSize(defaultWidth, defaultHeight);
                    window.setLocationRelativeTo(null);
                }
            });
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
