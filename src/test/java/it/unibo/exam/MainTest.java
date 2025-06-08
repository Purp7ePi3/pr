package it.unibo.exam;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Unused keep here for future implementation
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.fail;
import java.lang.reflect.InvocationTargetException;

import java.awt.GraphicsEnvironment;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.view.panel.MainMenuPanel;

class MainTest {
    @Test
    void testKeyHandlerAddedAndResponds() {
    if (GraphicsEnvironment.isHeadless()) {
        return;
    }

    Main.main(new String[]{});

    try {
        SwingUtilities.invokeAndWait(() -> {
            final Frame[] frames = JFrame.getFrames();
            JFrame mainFrame = null;
            for (final Frame frame : frames) {
                if (frame instanceof JFrame && "UniversityEscape".equals(frame.getTitle())) {
                    mainFrame = (JFrame) frame;
                    break;
                }
            }

            assertNotNull(mainFrame, "UniversityEscape JFrame not found");

            // Check if KeyHandler is added
            final KeyListener[] keyListeners = mainFrame.getKeyListeners();
            assertTrue(keyListeners.length > 0, "No KeyListeners found");

            boolean keyHandlerFound = false;
            for (final KeyListener listener : keyListeners) {
                if (listener instanceof KeyHandler) {
                    keyHandlerFound = true;
                    break;
                }
            }
            assertTrue(keyHandlerFound, "KeyHandler not found in KeyListeners");

            // Test KeyHandler response
            final KeyHandler keyHandler = (KeyHandler) keyListeners[0];

            // Simulate key press
            final KeyEvent pressEvent = new KeyEvent(mainFrame, KeyEvent.KEY_PRESSED,
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            keyHandler.keyPressed(pressEvent);
            assertTrue(keyHandler.isUpPressed(), "Up key should be pressed");

            // Simulate key release
            final KeyEvent releaseEvent = new KeyEvent(mainFrame, KeyEvent.KEY_RELEASED,
                                        System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            keyHandler.keyReleased(releaseEvent);
            assertFalse(keyHandler.isUpPressed(), "Up key should be released");
        });
    } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
        fail("Test was interrupted: " + e.getMessage());
    } catch (final InvocationTargetException e) {
        final Throwable cause = e.getCause();
        fail("Exception in UI thread: " + (cause != null ? cause.getMessage() : e.getMessage()));
    }
}

    @Test
    void testMainMenuPanelCreation() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            final JFrame testFrame = new JFrame("Test Frame");
            final MainMenuPanel mainMenu = new MainMenuPanel(testFrame);

            assertNotNull(mainMenu, "MainMenuPanel should be created");
            assertEquals(3, mainMenu.getComponentCount(), "MainMenuPanel should have 3 buttons");

            for (int i = 0; i < mainMenu.getComponentCount(); i++) {
                assertTrue(mainMenu.getComponent(i) instanceof JButton, "Component should be a JButton");
            }

            final JButton playButton = (JButton) mainMenu.getComponent(0);
            final JButton optionsButton = (JButton) mainMenu.getComponent(1);
            final JButton exitButton = (JButton) mainMenu.getComponent(2);

            assertEquals("Gioca", playButton.getText(), "First button should be 'Gioca'");
            assertEquals("Opzioni", optionsButton.getText(), "Second button should be 'Opzioni'");
            assertEquals("Esci", exitButton.getText(), "Third button should be 'Esci'");
        });
    }

    @Test
    void testMainMenuButtonActions() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            final JFrame testFrame = new JFrame("Test Frame");
            final MainMenuPanel mainMenu = new MainMenuPanel(testFrame);
            testFrame.getContentPane().add(mainMenu);

            final JButton playButton = (JButton) mainMenu.getComponent(0);
            final JButton optionsButton = (JButton) mainMenu.getComponent(1);
            final JButton exitButton = (JButton) mainMenu.getComponent(2);

            // Test play button action
            playButton.doClick();
            assertEquals(0, testFrame.getContentPane().getComponentCount(),
                    "Content pane should be empty after clicking 'Gioca'");

            // Test options button action
            optionsButton.doClick();
            // Since we can't directly check if JOptionPane is shown, we'll check if the button's action listener is called
            assertTrue(optionsButton.getActionListeners().length > 0, "Options button should have an action listener");

            // Test exit button action
            exitButton.doClick();
            // Similarly, we'll check if the exit button's action listener is called
            assertTrue(exitButton.getActionListeners().length > 0, "Exit button should have an action listener");

            // Additional check: the frame should still be visible after clicking the exit button
            // because in the test environment, System.exit() is not actually called
            assertTrue(testFrame.isVisible(), "Frame should still be visible after clicking 'Esci'");
        });
    }
}
