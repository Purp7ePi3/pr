package it.unibo.exam.model.entities;

import it.unibo.exam.view.scaling.ScreenScaler;
import it.unibo.exam.Main;

/**
 * Esempio di come implementare una porta che si scala automaticamente.
 */
public class ScaledDoor {
    
    // Dimensioni e posizione ORIGINALI (nel sistema di coordinate di riferimento)
    private final int originalX;
    private final int originalY;
    private final int originalWidth;
    private final int originalHeight;
    
    // Dimensioni e posizione SCALATE (aggiornate dinamicamente)
    private int scaledX;
    private int scaledY;
    private int scaledWidth;
    private int scaledHeight;
    
    private final ScreenScaler scaler;
    
    /**
     * Costruttore della porta.
     * @param originalX posizione X originale
     * @param originalY posizione Y originale  
     * @param originalWidth larghezza originale
     * @param originalHeight altezza originale
     * @param scaler il sistema di scaling
     */
    public ScaledDoor(final int originalX, final int originalY, 
                     final int originalWidth, final int originalHeight,
                     final ScreenScaler scaler) {
        this.originalX = originalX;
        this.originalY = originalY;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.scaler = scaler;
        
        // Calcola le dimensioni scalate iniziali
        updateScaledDimensions();
    }
    
    /**
     * Aggiorna le dimensioni scalate in base al fattore di scala corrente.
     * Chiamare questo metodo ogni volta che le dimensioni della finestra cambiano.
     */
    public void updateScaledDimensions() {
        if (scaler != null) {
            scaledX = scaler.scaleX(originalX);
            scaledY = scaler.scaleY(originalY);
            scaledWidth = scaler.scaleWidth(originalWidth);
            scaledHeight = scaler.scaleHeight(originalHeight);
            
            System.out.println("Porta ridimensionata:");
            System.out.println("  Originale: " + originalX + "," + originalY + " " + originalWidth + "x" + originalHeight);
            System.out.println("  Scalata: " + scaledX + "," + scaledY + " " + scaledWidth + "x" + scaledHeight);
        } else {
            // Se non c'è scaler, usa le dimensioni originali
            scaledX = originalX;
            scaledY = originalY;
            scaledWidth = originalWidth;
            scaledHeight = originalHeight;
        }
    }
    
    /**
     * Versione alternativa che usa il scaler globale.
     */
    public void updateUsingGlobalScaler() {
        final ScreenScaler globalScaler = Main.getGlobalScaler();
        if (globalScaler != null) {
            scaledX = globalScaler.scaleX(originalX);
            scaledY = globalScaler.scaleY(originalY);
            scaledWidth = globalScaler.scaleWidth(originalWidth);
            scaledHeight = globalScaler.scaleHeight(originalHeight);
        }
    }
    
    /**
     * Controlla se un punto (scalato) interseca con la porta.
     * @param x coordinata X (già scalata)
     * @param y coordinata Y (già scalata)
     * @return true se il punto interseca la porta
     */
    public boolean intersects(final int x, final int y) {
        return x >= scaledX && x <= scaledX + scaledWidth &&
               y >= scaledY && y <= scaledY + scaledHeight;
    }
    
    /**
     * Controlla se un rettangolo interseca con la porta.
     * @param x coordinata X del rettangolo
     * @param y coordinata Y del rettangolo
     * @param width larghezza del rettangolo
     * @param height altezza del rettangolo
     * @return true se c'è intersezione
     */
    public boolean intersects(final int x, final int y, final int width, final int height) {
        return !(x > scaledX + scaledWidth || 
                x + width < scaledX || 
                y > scaledY + scaledHeight || 
                y + height < scaledY);
    }
    
    // Getters per le dimensioni originali
    public int getOriginalX() { return originalX; }
    public int getOriginalY() { return originalY; }
    public int getOriginalWidth() { return originalWidth; }
    public int getOriginalHeight() { return originalHeight; }
    
    // Getters per le dimensioni scalate (da usare per rendering e collision)
    public int getScaledX() { return scaledX; }
    public int getScaledY() { return scaledY; }
    public int getScaledWidth() { return scaledWidth; }
    public int getScaledHeight() { return scaledHeight; }
    
    /**
     * Metodo per il rendering - usa sempre le dimensioni scalate.
     * @param g il contesto grafico
     */
    public void draw(final java.awt.Graphics2D g) {
        // Disegna la porta usando le dimensioni scalate
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(scaledX, scaledY, scaledWidth, scaledHeight);
        
        // Disegna il bordo
        g.setColor(java.awt.Color.BLACK);
        g.drawRect(scaledX, scaledY, scaledWidth, scaledHeight);
    }
}