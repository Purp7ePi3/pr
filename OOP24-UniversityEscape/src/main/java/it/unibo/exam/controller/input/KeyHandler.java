package it.unibo.exam.controller.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the application.
 */
public final class KeyHandler implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean interactPressed;

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
