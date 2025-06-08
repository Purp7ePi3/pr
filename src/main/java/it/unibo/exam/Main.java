package it.unibo.exam;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.view.panel.MainMenuPanel;
import it.unibo.exam.view.scaling.ScreenScaler;

/**
 * Main application class with dynamic scaling system.
 */
public final class Main {
    // Scaler globale per l'intera applicazione
    private static ScreenScaler globalScaler;
    
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    }

    /**
     * Entry point of the application.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== INIZIALIZZAZIONE CON SCALING DINAMICO ===");
            
            final JFrame window = new JFrame();

            // Get screen dimensions
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int screenWidth = (int) screenSize.getWidth();
            final int screenHeight = (int) screenSize.getHeight();
            
            System.out.println("Dimensioni schermo: " + screenWidth + "x" + screenHeight);

            // Get graphics device for fullscreen support
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();

            // Imposta dimensioni iniziali della finestra (80% dello schermo)
            final int initialWidth = (int) (screenWidth * 0.8);
            final int initialHeight = (int) (screenHeight * 0.8);
            
            // Crea lo scaler globale con le dimensioni iniziali come riferimento
            globalScaler = new ScreenScaler(initialWidth, initialHeight);
            
            System.out.println("Dimensioni finestra iniziali (riferimento): " + initialWidth + "x" + initialHeight);

            // Create KeyHandler with fullscreen support e scaler
            final KeyHandler keyHandler = new KeyHandler(window, gd, globalScaler);

            // Create main menu panel passando lo scaler
            final MainMenuPanel mainMenu = new MainMenuPanel(window, globalScaler);

            // Configure window
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("UniversityEscape");
            window.addKeyListener(keyHandler);

            // Add main menu panel
            window.getContentPane().add(mainMenu);

            // Set initial window size
            window.setSize(initialWidth, initialHeight);
            window.setLocationRelativeTo(null);

            // Configure focus
            window.setFocusable(true);
            window.setFocusTraversalKeysEnabled(false);

            // Add window listeners per gestire il resize
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(final WindowEvent e) {
                    System.out.println("Finestra minimizzata");
                }

                @Override 
                public void windowDeiconified(final WindowEvent e) {
                    System.out.println("Finestra ripristinata");
                    // Aggiorna lo scaler
                    updateScaler(window);
                }

                @Override
                public void windowStateChanged(final WindowEvent e) {
                    System.out.println("Stato finestra cambiato: " + e.getNewState());
                    // Aggiorna lo scaler quando cambia lo stato
                    SwingUtilities.invokeLater(() -> updateScaler(window));
                }
            });

            // Add component listener per il resize
            window.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(final java.awt.event.ComponentEvent e) {
                    System.out.println("Finestra ridimensionata a: " + window.getWidth() + "x" + window.getHeight());
                    updateScaler(window);
                }
            });

            // Make window visible
            window.setVisible(true);

            // Force focus
            SwingUtilities.invokeLater(() -> {
                window.toFront();
                window.requestFocus();
                window.requestFocusInWindow();
                
                // Test iniziale
                javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
                    System.out.println("\n=== STATO INIZIALE ===");
                    System.out.println("Finestra: " + window.getWidth() + "x" + window.getHeight());
                    System.out.println("Scaler pronto, usa F11 per fullscreen");
                    System.out.println("====================");
                });
                timer.setRepeats(false);
                timer.start();
            });
        });
    }

    /**
     * Aggiorna lo scaler globale con le nuove dimensioni della finestra.
     * @param window la finestra principale
     */
    private static void updateScaler(final JFrame window) {
        if (globalScaler != null && window != null) {
            globalScaler.updateDimensions(window.getWidth(), window.getHeight());
            
            // Notifica tutti i componenti che devono riscalare
            notifyComponentsOfScaleChange(window);
        }
    }

    /**
     * Notifica tutti i componenti che le dimensioni sono cambiate.
     * @param window la finestra principale
     */
    private static void notifyComponentsOfScaleChange(final JFrame window) {
        // Notifica specificamente il MainMenuPanel se presente
        if (window.getContentPane().getComponentCount() > 0) {
            final java.awt.Component comp = window.getContentPane().getComponent(0);
            if (comp instanceof MainMenuPanel) {
                ((MainMenuPanel) comp).updateForNewScale();
            }
        }
        
        // Invalida e ridisegna tutti i componenti
        window.invalidate();
        window.revalidate();
        window.repaint();
        
        System.out.println("Componenti notificati del cambio di scala");
    }

    /**
     * Restituisce lo scaler globale per l'uso in altri componenti.
     * @return lo scaler globale
     */
    public static ScreenScaler getGlobalScaler() {
        return globalScaler;
    }

    /**
     * Metodo di utilità per ottenere le dimensioni scalate di una porta.
     * Esempio di come usare lo scaler per elementi specifici del gioco.
     * @param originalDoorWidth larghezza originale della porta
     * @param originalDoorHeight altezza originale della porta
     * @return array con [larghezza_scalata, altezza_scalata]
     */
    public static int[] getScaledDoorSize(final int originalDoorWidth, final int originalDoorHeight) {
        if (globalScaler != null) {
            return globalScaler.scaleDimension(originalDoorWidth, originalDoorHeight);
        }
        return new int[]{originalDoorWidth, originalDoorHeight};
    }

    /**
     * Metodo di utilità per ottenere la posizione scalata di una porta.
     * @param originalX posizione X originale
     * @param originalY posizione Y originale
     * @return array con [x_scalata, y_scalata]
     */
    public static int[] getScaledDoorPosition(final int originalX, final int originalY) {
        if (globalScaler != null) {
            return globalScaler.scalePoint(originalX, originalY);
        }
        return new int[]{originalX, originalY};
    }
}