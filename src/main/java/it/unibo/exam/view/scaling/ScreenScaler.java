package it.unibo.exam.view.scaling;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Gestisce lo scaling degli elementi del gioco in base alle dimensioni della finestra.
 * Le dimensioni di riferimento sono impostate dinamicamente.
 */
public final class ScreenScaler {

    private final int referenceWidth;
    private final int referenceHeight;

    private int currentWidth;
    private int currentHeight;
    private double scaleX;
    private double scaleY;
    private double uniformScale; // Per mantenere proporzioni

    /**
     * Costruttore che prende le dimensioni di riferimento come input.
     * @param referenceWidth larghezza di riferimento (es. dimensioni finestra iniziale)
     * @param referenceHeight altezza di riferimento (es. dimensioni finestra iniziale)
     */
    public ScreenScaler(final int referenceWidth, final int referenceHeight) {
        this.referenceWidth = referenceWidth;
        this.referenceHeight = referenceHeight;
        this.currentWidth = referenceWidth;
        this.currentHeight = referenceHeight;

        // Inizialmente scale = 1.0 (nessun scaling)
        this.scaleX = 1.0;
        this.scaleY = 1.0;
        this.uniformScale = 1.0;
    }

    /**
     * Costruttore che prende le dimensioni di riferimento da un componente.
     * @param component componente da cui prendere le dimensioni di riferimento
     */
    public ScreenScaler(final java.awt.Component component) {
        this(component.getWidth(), component.getHeight());
    }

    /**
     * Aggiorna le dimensioni correnti e ricalcola i fattori di scala.
     * @param width nuova larghezza
     * @param height nuova altezza
     */
    public void updateDimensions(final int width, final int height) {
        this.currentWidth = width;
        this.currentHeight = height;

        // Calcola i fattori di scala rispetto alle dimensioni di riferimento
        this.scaleX = (double) width / referenceWidth;
        this.scaleY = (double) height / referenceHeight;

        // Usa il fattore più piccolo per mantenere le proporzioni
        this.uniformScale = Math.min(scaleX, scaleY);

        System.out.println("ScreenScaler aggiornato:");
        System.out.println("  Riferimento: " + referenceWidth + "x" + referenceHeight);
        System.out.println("  Corrente: " + width + "x" + height);
        System.out.println("  Scale X: " + String.format("%.3f", scaleX));
        System.out.println("  Scale Y: " + String.format("%.3f", scaleY));
        System.out.println("  Scale uniforme: " + String.format("%.3f", uniformScale));
    }

    /**
     * Aggiorna le dimensioni da un componente.
     * @param component componente da cui prendere le nuove dimensioni
     */
    public void updateFromComponent(final java.awt.Component component) {
        updateDimensions(component.getWidth(), component.getHeight());
    }

    /**
     * Scala una coordinata X.
     * @param originalX coordinata X originale
     * @return coordinata X scalata
     */
    public int scaleX(final int originalX) {
        return (int) Math.round(originalX * scaleX);
    }

    /**
     * Scala una coordinata Y.
     * @param originalY coordinata Y originale  
     * @return coordinata Y scalata
     */
    public int scaleY(final int originalY) {
        return (int) Math.round(originalY * scaleY);
    }

    /**
     * Scala una dimensione mantenendo le proporzioni.
     * @param originalSize dimensione originale
     * @return dimensione scalata proporzionalmente
     */
    public int scaleSize(final int originalSize) {
        return (int) Math.round(originalSize * uniformScale);
    }

    /**
     * Scala una larghezza.
     * @param originalWidth larghezza originale
     * @return larghezza scalata
     */
    public int scaleWidth(final int originalWidth) {
        return (int) Math.round(originalWidth * scaleX);
    }

    /**
     * Scala un'altezza.
     * @param originalHeight altezza originale
     * @return altezza scalata
     */
    public int scaleHeight(final int originalHeight) {
        return (int) Math.round(originalHeight * scaleY);
    }

    /**
     * Scala un punto (x, y) e restituisce un array [x_scalato, y_scalato].
     * @param x coordinata X originale
     * @param y coordinata Y originale
     * @return array con coordinate scalate
     */
    public int[] scalePoint(final int x, final int y) {
        return new int[]{scaleX(x), scaleY(y)};
    }

    /**
     * Scala una dimensione (width, height) e restituisce un array [w_scalato, h_scalato].
     * @param width larghezza originale
     * @param height altezza originale
     * @return array con dimensioni scalate
     */
    public int[] scaleDimension(final int width, final int height) {
        return new int[]{scaleWidth(width), scaleHeight(height)};
    }

    /**
     * Scala una dimensione mantenendo proporzioni e restituisce un array [w_scalato, h_scalato].
     * @param width larghezza originale
     * @param height altezza originale
     * @return array con dimensioni scalate proporzionalmente
     */
    public int[] scaleProportional(final int width, final int height) {
        return new int[]{scaleSize(width), scaleSize(height)};
    }

    /**
     * Converte coordinate scalate a coordinate originali (reverse scaling).
     * @param scaledX coordinata X scalata
     * @return coordinata X originale
     */
    public int unscaleX(final int scaledX) {
        return (int) Math.round(scaledX / scaleX);
    }

    /**
     * Converte coordinate scalate a coordinate originali (reverse scaling).
     * @param scaledY coordinata Y scalata
     * @return coordinata Y originale
     */
    public int unscaleY(final int scaledY) {
        return (int) Math.round(scaledY / scaleY);
    }

    // Getters
    public int getReferenceWidth() { return referenceWidth; }
    public int getReferenceHeight() { return referenceHeight; }
    public int getCurrentWidth() { return currentWidth; }
    public int getCurrentHeight() { return currentHeight; }
    public double getScaleX() { return scaleX; }
    public double getScaleY() { return scaleY; }
    public double getUniformScale() { return uniformScale; }

    /**
     * Verifica se le dimensioni sono cambiate rispetto al riferimento.
     * @return true se le dimensioni sono diverse dal riferimento
     */
    public boolean isScaled() {
        return currentWidth != referenceWidth || currentHeight != referenceHeight;
    }

    /**
     * Metodo di utilità per ottenere le dimensioni dello schermo.
     * @return dimensioni dello schermo
     */
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Crea uno scaler usando le dimensioni della finestra come riferimento
     * e le dimensioni dello schermo come target.
     * @param windowWidth larghezza finestra di riferimento
     * @param windowHeight altezza finestra di riferimento
     * @return scaler configurato per fullscreen
     */
    public static ScreenScaler createForFullscreen(final int windowWidth, final int windowHeight) {
        final Dimension screen = getScreenSize();
        final ScreenScaler scaler = new ScreenScaler(windowWidth, windowHeight);
        scaler.updateDimensions(screen.width, screen.height);
        return scaler;
    }
}
