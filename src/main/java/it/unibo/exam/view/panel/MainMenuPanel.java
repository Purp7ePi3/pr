package it.unibo.exam.view.panel;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import it.unibo.exam.view.scaling.ScreenScaler;

/**
 * Main menu panel with scaling support.
 */
public class MainMenuPanel extends JPanel {
    
    private final JFrame parentWindow;
    private final ScreenScaler scaler;
    
    // Dimensioni originali degli elementi del menu (coordinate di riferimento)
    private static final int ORIGINAL_TITLE_Y = 200;
    private static final int ORIGINAL_BUTTON_WIDTH = 200;
    private static final int ORIGINAL_BUTTON_HEIGHT = 50;
    private static final int ORIGINAL_BUTTON_SPACING = 80;
    private static final int ORIGINAL_FIRST_BUTTON_Y = 300;
    
    /**
     * Constructor che mantiene compatibilità con versione precedente.
     * @param parentWindow the parent window
     */
    public MainMenuPanel(final JFrame parentWindow) {
        this(parentWindow, null);
    }
    
    /**
     * Constructor with scaling support.
     * @param parentWindow the parent window
     * @param scaler the screen scaler for dynamic resizing
     */
    public MainMenuPanel(final JFrame parentWindow, final ScreenScaler scaler) {
        this.parentWindow = parentWindow;
        this.scaler = scaler;
        
        System.out.println("MainMenuPanel creato:");
        System.out.println("  ParentWindow: " + (parentWindow != null ? "OK" : "NULL"));
        System.out.println("  Scaler: " + (scaler != null ? "OK" : "NULL"));
        
        // Configurazione base del panel
        setFocusable(true);
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        final Graphics2D g2d = (Graphics2D) g;
        
        // Abilita antialiasing per testo più bello
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawMenu(g2d);
    }
    
    /**
     * Disegna il menu principale con scaling automatico.
     * @param g2d il contesto grafico
     */
    private void drawMenu(final Graphics2D g2d) {
        final int panelWidth = getWidth();
        final int panelHeight = getHeight();
        
        System.out.println("Disegnando menu su panel: " + panelWidth + "x" + panelHeight);
        
        // Calcola le dimensioni scalate
        final int titleY = scaler != null ? scaler.scaleY(ORIGINAL_TITLE_Y) : ORIGINAL_TITLE_Y;
        final int buttonWidth = scaler != null ? scaler.scaleWidth(ORIGINAL_BUTTON_WIDTH) : ORIGINAL_BUTTON_WIDTH;
        final int buttonHeight = scaler != null ? scaler.scaleHeight(ORIGINAL_BUTTON_HEIGHT) : ORIGINAL_BUTTON_HEIGHT;
        final int buttonSpacing = scaler != null ? scaler.scaleY(ORIGINAL_BUTTON_SPACING) : ORIGINAL_BUTTON_SPACING;
        final int firstButtonY = scaler != null ? scaler.scaleY(ORIGINAL_FIRST_BUTTON_Y) : ORIGINAL_FIRST_BUTTON_Y;
        
        // Font scalato per il titolo
        final int titleFontSize = scaler != null ? scaler.scaleSize(48) : 48;
        final int buttonFontSize = scaler != null ? scaler.scaleSize(18) : 18;
        
        // Disegna il titolo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        final String title = "UNIVERSITY ESCAPE";
        final FontMetrics titleMetrics = g2d.getFontMetrics();
        final int titleX = (panelWidth - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, titleY);
        
        // Disegna i pulsanti del menu
        g2d.setFont(new Font("Arial", Font.PLAIN, buttonFontSize));
        final FontMetrics buttonMetrics = g2d.getFontMetrics();
        
        final String[] menuOptions = {"Nuova Partita", "Carica Partita", "Opzioni", "Esci"};
        final int buttonX = (panelWidth - buttonWidth) / 2;
        
        for (int i = 0; i < menuOptions.length; i++) {
            final int buttonY = firstButtonY + (i * buttonSpacing);
            
            // Disegna il rettangolo del pulsante
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
            
            // Disegna il bordo del pulsante
            g2d.setColor(Color.WHITE);
            g2d.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
            
            // Disegna il testo del pulsante (centrato)
            final String text = menuOptions[i];
            final int textX = buttonX + (buttonWidth - buttonMetrics.stringWidth(text)) / 2;
            final int textY = buttonY + (buttonHeight + buttonMetrics.getAscent()) / 2 - 2;
            g2d.drawString(text, textX, textY);
        }
        
        // Disegna le istruzioni in basso
        final int instructionFontSize = scaler != null ? scaler.scaleSize(14) : 14;
        g2d.setFont(new Font("Arial", Font.ITALIC, instructionFontSize));
        g2d.setColor(Color.LIGHT_GRAY);
        
        final String[] instructions = {
            "Usa WASD per muoverti",
            "Premi E per interagire", 
            "F11 per fullscreen",
            "ESC per uscire"
        };
        
        final FontMetrics instrMetrics = g2d.getFontMetrics();
        final int instructionStartY = panelHeight - (instructions.length * (instrMetrics.getHeight() + 5)) - 20;
        
        for (int i = 0; i < instructions.length; i++) {
            final String instruction = instructions[i];
            final int instrX = (panelWidth - instrMetrics.stringWidth(instruction)) / 2;
            final int instrY = instructionStartY + (i * (instrMetrics.getHeight() + 5));
            g2d.drawString(instruction, instrX, instrY);
        }
        
        // Debug: mostra informazioni di scaling
        if (scaler != null && scaler.isScaled()) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
            final String scaleInfo = String.format("Scale: %.2f x %.2f", 
                                                  scaler.getScaleX(), scaler.getScaleY());
            g2d.drawString(scaleInfo, 10, 20);
            
            final String dimInfo = String.format("Dim: %dx%d -> %dx%d", 
                                                scaler.getReferenceWidth(), scaler.getReferenceHeight(),
                                                scaler.getCurrentWidth(), scaler.getCurrentHeight());
            g2d.drawString(dimInfo, 10, 35);
        }
    }
    
    /**
     * Forza il ridisegno del menu quando le dimensioni cambiano.
     * Chiamare questo metodo quando lo scaler viene aggiornato.
     */
    public void updateForNewScale() {
        System.out.println("MainMenuPanel: aggiornamento per nuovo scaling");
        invalidate();
        revalidate();
        repaint();
    }
    
    /**
     * Getter per lo scaler.
     * @return lo scaler corrente
     */
    public ScreenScaler getScaler() {
        return scaler;
    }
}