package it.unibo.exam.controller.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.Dimension;
import java.awt.Toolkit;
import it.unibo.exam.view.scaling.ScreenScaler;

/**
 * Handles keyboard input for the application.
 * Version con scaling automatico degli elementi del gioco.
 */
public final class KeyHandler implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean interactPressed;
    private boolean interactJustPressed;
    
    // References for fullscreen functionality
    private JFrame window;
    private GraphicsDevice graphicsDevice;
    private ScreenScaler scaler;

    /**
     * Default constructor for basic key handling.
     */
    public KeyHandler() {
        System.out.println("KeyHandler creato SENZA supporto fullscreen e scaling");
    }

    /**
     * Constructor with fullscreen support only.
     * @param window the main window
     * @param graphicsDevice the graphics device for fullscreen operations
     */
    public KeyHandler(final JFrame window, final GraphicsDevice graphicsDevice) {
        this.window = window;
        this.graphicsDevice = graphicsDevice;
        System.out.println("KeyHandler creato CON supporto fullscreen, SENZA scaling");
    }

    /**
     * Constructor with fullscreen support and scaling.
     * @param window the main window
     * @param graphicsDevice the graphics device for fullscreen operations
     * @param scaler the screen scaler for dynamic resizing
     */
    public KeyHandler(final JFrame window, final GraphicsDevice graphicsDevice, final ScreenScaler scaler) {
        this.window = window;
        this.graphicsDevice = graphicsDevice;
        this.scaler = scaler;
        System.out.println("KeyHandler creato CON supporto fullscreen E scaling");
        System.out.println("Window: " + (window != null ? "OK" : "NULL"));
        System.out.println("GraphicsDevice: " + (graphicsDevice != null ? "OK" : "NULL"));
        System.out.println("Scaler: " + (scaler != null ? "OK" : "NULL"));
    }

    // Getters rimangono uguali...
    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isInteractPressed() { return interactPressed; }
    
    public boolean isInteractJustPressed() {
        if (interactJustPressed) {
            interactJustPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        // Non usato
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        final int code = e.getKeyCode();
        
        // DEBUG: Stampa TUTTI i tasti premuti
        System.out.println("TASTO PREMUTO: " + code + " (" + KeyEvent.getKeyText(code) + ")");
        
        // Controlli di gioco
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
            if (!interactPressed) {
                interactJustPressed = true;
            }
            interactPressed = true;
        }
        
        // DEBUG F11
        if (code == KeyEvent.VK_F11) {
            System.out.println("*** F11 RILEVATO! ***");
            System.out.println("Window disponibile: " + (window != null));
            System.out.println("GraphicsDevice disponibile: " + (graphicsDevice != null));
            System.out.println("Scaler disponibile: " + (scaler != null));
            
            if (window != null && graphicsDevice != null) {
                System.out.println("Chiamando toggleFullscreen...");
                toggleFullscreen();
            } else {
                System.out.println("ERRORE: window o graphicsDevice sono null!");
            }
        }
        
        // Altri tasti globali
        if ((code == KeyEvent.VK_F4 && e.isAltDown()) || code == KeyEvent.VK_ESCAPE) {
            System.out.println("Tasto di uscita premuto");
            if (window != null && isInMainMenu()) {
                System.out.println("Uscendo dall'applicazione...");
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_E) interactPressed = false;
    }

    /**
     * Toggles between fullscreen and windowed mode with scaling support.
     */
    private void toggleFullscreen() {
        System.out.println("=== TOGGLE FULLSCREEN CON SCALING INIZIATO ===");
        
        if (window == null || graphicsDevice == null) {
            System.out.println("ERRORE: window o graphicsDevice null");
            return;
        }

        // Get current screen dimensions
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();
        
        System.out.println("Dimensioni schermo: " + screenWidth + "x" + screenHeight);
        System.out.println("Finestra corrente in fullscreen: " + (graphicsDevice.getFullScreenWindow() == window));
        
        if (graphicsDevice.getFullScreenWindow() == window) {
            System.out.println("Uscendo dal fullscreen...");
            // Exit fullscreen
            graphicsDevice.setFullScreenWindow(null);
            window.setUndecorated(false);
            window.setExtendedState(JFrame.NORMAL);
            
            // Torna alle dimensioni di riferimento dello scaler
            if (scaler != null) {
                final int refWidth = scaler.getReferenceWidth();
                final int refHeight = scaler.getReferenceHeight();
                System.out.println("Ripristinando dimensioni di riferimento: " + refWidth + "x" + refHeight);
                window.setSize(refWidth, refHeight);
                
                // Aggiorna lo scaler
                scaler.updateDimensions(refWidth, refHeight);
            } else {
                // Fallback: use 80% of screen size
                final int windowWidth = (int) (screenWidth * 0.8);
                final int windowHeight = (int) (screenHeight * 0.8);
                System.out.println("Fallback: impostando finestra a: " + windowWidth + "x" + windowHeight);
                window.setSize(windowWidth, windowHeight);
            }
            
            window.setLocationRelativeTo(null);
            System.out.println("Fullscreen disabilitato");
        } else {
            System.out.println("Entrando in fullscreen...");
            // Enter fullscreen
            window.setUndecorated(true);
            if (graphicsDevice.isFullScreenSupported()) {
                System.out.println("Fullscreen supportato, utilizzando setFullScreenWindow");
                graphicsDevice.setFullScreenWindow(window);
                
                // Aggiorna lo scaler per le dimensioni fullscreen
                if (scaler != null) {
                    System.out.println("Aggiornando scaler per fullscreen: " + screenWidth + "x" + screenHeight);
                    scaler.updateDimensions(screenWidth, screenHeight);
                }
            } else {
                System.out.println("Fullscreen non supportato, usando fallback");
                window.setSize(screenWidth, screenHeight);
                window.setLocation(0, 0);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                
                // Aggiorna lo scaler anche per il fallback
                if (scaler != null) {
                    scaler.updateDimensions(screenWidth, screenHeight);
                }
            }
            System.out.println("Fullscreen abilitato");
        }
        
        // Forza il ridisegno di tutti i componenti
        if (window != null) {
            window.invalidate();
            window.revalidate();
            window.repaint();
        }
        
        System.out.println("=== TOGGLE FULLSCREEN CON SCALING COMPLETATO ===");
    }

    /**
     * Controlla se siamo nel menu principale
     */
    private boolean isInMainMenu() {
        if (window == null || window.getContentPane().getComponentCount() == 0) {
            return false;
        }
        
        try {
            final boolean result = window.getContentPane().getComponent(0) 
                instanceof it.unibo.exam.view.panel.MainMenuPanel;
            System.out.println("In main menu: " + result);
            return result;
        } catch (final Exception e) {
            System.out.println("Errore nel controllo main menu: " + e.getMessage());
            return false;
        }
    }

    /**
     * Getter per lo scaler (utile per altri componenti)
     */
    public ScreenScaler getScaler() {
        return scaler;
    }
}