package it.unibo.exam.controller.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the application.
 * Improved version with better interaction handling.
 */
public final class KeyHandler implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean interactPressed;
    private boolean interactJustPressed; // For single-press actions

    /**
     * @return true if up key is pressed
     */
    public boolean isUpPressed() {
        return upPressed;
    }

    /**
     * @return true if down key is pressed
     */
    public boolean isDownPressed() {
        return downPressed;
    } 

    /**
     * @return true if left key is pressed
     */
    public boolean isLeftPressed() {
        return leftPressed;
    }

    /**
     * @return true if right key is pressed
     */
    public boolean isRightPressed() {
        return rightPressed;
    }

    /**
     * @return true if interact key is pressed
     */
    public boolean isInteractPressed() {
        return interactPressed;
    }

    /**
     * @return true if interact key was just pressed (single press detection)
     */
    public boolean isInteractJustPressed() {
        if (interactJustPressed) {
            interactJustPressed = false; // Reset after reading
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyTyped(final KeyEvent e) {
        // Not used but required by the KeyListener interface
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true; 
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            if (!interactPressed) { // Only set just pressed if it wasn't already pressed
                interactJustPressed = true;
            }
            interactPressed = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_E) {
            interactPressed = false;
        }
    }
}
