package it.unibo.exam.view.panel;

import it.unibo.exam.controller.MainController;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.view.GameRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Main game panel that handles rendering of the game world.
 * This panel integrates the MainController and GameRenderer to display the game.
 */
public class GamePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private final MainController mainController;
    private final GameRenderer gameRenderer;
    
    /**
     * Constructor for GamePanel.
     * 
     * @param initialSize the initial size of the game panel
     */
    public GamePanel(final Point2D initialSize) {
        this.mainController = new MainController(initialSize);
        this.gameRenderer = mainController.getGameRenderer();
        
        initializePanel(initialSize);
        setupResizeListener();
        
        // Start the game controller
        startGameController();
    }
    
    /**
     * Alternative constructor that takes Dimension.
     * 
     * @param initialSize the initial size as a Dimension
     */
    public GamePanel(final Dimension initialSize) {
        this(new Point2D(initialSize.width, initialSize.height));
    }
    
    /**
     * Initializes the panel settings.
     * 
     * @param initialSize the initial size of the panel
     */
    private void initializePanel(final Point2D initialSize) {
        setPreferredSize(new Dimension(initialSize.getX(), initialSize.getY()));
        setFocusable(true);
        setDoubleBuffered(true);
        
        // Add key listener from the controller
        addKeyListener(mainController.getKeyHandler());
    }
    
    /**
     * Sets up a component listener to handle panel resizing.
     */
    private void setupResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension newSize = getSize();
                final Point2D newPoint = new Point2D(newSize.width, newSize.height);
                mainController.resize(newPoint);
            }
        });
    }
    
    /**
     * Starts the game controller in a separate thread.
     */
    private void startGameController() {
        final Thread gameThread = new Thread(() -> {
            mainController.start();
        }, "GameControllerThread");
        
        gameThread.setDaemon(true); // Ensure thread stops when main application stops
        gameThread.start();
    }
    
    /**
     * Stops the game controller.
     */
    public void stopGame() {
        mainController.stop();
    }
    
    /**
     * Overrides paintComponent to render the game.
     * 
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        final Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Enable anti-aliasing for smoother graphics
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Render the game world
            gameRenderer.renderGame(g2d);
            
            // Render the HUD on top
            gameRenderer.renderHud(g2d);
            
        } finally {
            g2d.dispose();
        }
        
        // Schedule next repaint for smooth animation
        repaint();
    }
    
    /**
     * Gets the main controller for external access if needed.
     * 
     * @return the main controller
     */
    public MainController getMainController() {
        return mainController;
    }
    
    /**
     * Requests focus for this panel to receive key events.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
}