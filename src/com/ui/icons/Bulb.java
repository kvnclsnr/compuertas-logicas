package com.ui.icons;

import com.engine.gates.BulbGate;
import com.ui.layouts.canvas.Canvas;
import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

// COMPONENTE VISUAL QUE REPRESENTA UN FOCO O BOMBILLA
public class Bulb extends LogicGate {
    private static int counter = 0;
    
    // COLORES PARA EL FOCO ENCENDIDO
    public static final Color ACTIVE_FILL = new Color(255, 238, 88);       // Amarillo brillante
    public static final Color ACTIVE_BORDER = new Color(245, 160, 0);      // Borde dorado cálido
    public static final Color ACTIVE_FILAMENT = new Color(230, 81, 0);     // Filamento naranja incandescente
    public static final Color ACTIVE_RAY = new Color(255, 214, 0);         // Rayos de luz amarillos
    
    // COLORES PARA EL FOCO APAGADO
    public static final Color INACTIVE_FILL = new Color(240, 243, 246);    // Gris claro apagado
    public static final Color INACTIVE_BORDER = Palette.MUTED;             // Borde gris apagado
    public static final Color INACTIVE_FILAMENT = new Color(170, 175, 182);
    public static final Color BASE_COLOR = new Color(130, 138, 145);
    
    // CREA UN FOCO CON NOMBRE ESPECIFICO Y TAMAÑO
    public Bulb(String name, int size) {
        super(size);
        this.engineGate = new BulbGate(name);
    }
    
    // CREA UN FOCO CON NOMBRE AUTOMATICO Y TAMAÑO DADO
    public Bulb(int size) {
        this("FOCO-" + (counter++), size);
    }
    
    // CREA UN FOCO CON TAMAÑO POR DEFECTO
    public Bulb() {
        this(240);
    }
    
    // UN FOCO NO TIENE TERMINAL DE SALIDA
    @Override
    protected boolean hasOutputPin() {
        return false;
    }
    
    // EL FOCO TIENE UNA SOLA ENTRADA CENTRADA VERTICALMENTE
    @Override
    protected int[] getInputPinYPositions() {
        return new int[] { 14 };
    }
    
    // INDICA SI EL FOCO ESTA ENCENDIDO
    public boolean isOn() {
        return getOutputValue();
    }
    
    // INDICA SI EL FOCO ESTA ACTIVO
    public boolean isActive() {
        return getOutputValue();
    }
    
    // ACTUALIZA EL ESTADO Y REDIBUJA
    public void updateState() {
        repaint();
    }
    
    // DIBUJA EL FOCO, SU BASE, SU FILAMENTO Y SUS RAYOS DE LUZ
    @Override
    protected void drawGate(Graphics2D g2d) {
        boolean inCanvas = (getParent() instanceof Canvas);
        boolean active = inCanvas && getOutputValue();
        
        // CABLE DE ENTRADA DESDE EL PIN (X=2) HASTA LA BASE (X=6)
        g2d.setColor(active ? ACTIVE_BORDER : INACTIVE_BORDER);
        g2d.setStroke(new BasicStroke(1.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(2, 14, 6, 14);
        
        // BASE METALICA / ROSCA DEL FOCO
        g2d.setColor(active ? BASE_COLOR : Palette.LINE);
        g2d.fill(new RoundRectangle2D.Double(6.0, 11.5, 3.5, 5.0, 1.5, 1.5));
        
        g2d.setColor(active ? Palette.INK : INACTIVE_BORDER);
        g2d.setStroke(new BasicStroke(0.7f));
        g2d.draw(new RoundRectangle2D.Double(6.0, 11.5, 3.5, 5.0, 1.5, 1.5));
        g2d.drawLine(7, 12, 7, 16);
        g2d.drawLine(8, 12, 8, 16);
        
        // CONTORNO DE LA BOMBILLA
        Path2D.Double bulbEnvelope = new Path2D.Double();
        bulbEnvelope.moveTo(9.5, 11.5);
        bulbEnvelope.curveTo(11.0, 11.5, 12.5, 8.0, 16.5, 8.0);
        bulbEnvelope.curveTo(20.5, 8.0, 23.5, 10.5, 23.5, 14.0);
        bulbEnvelope.curveTo(23.5, 17.5, 20.5, 20.0, 16.5, 20.0);
        bulbEnvelope.curveTo(12.5, 20.0, 11.0, 16.5, 9.5, 16.5);
        bulbEnvelope.closePath();
        
        // RELLENO DEL FOCO
        g2d.setColor(active ? ACTIVE_FILL : INACTIVE_FILL);
        g2d.fill(bulbEnvelope);
        
        // BORDE DEL FOCO
        g2d.setColor(active ? ACTIVE_BORDER : INACTIVE_BORDER);
        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(bulbEnvelope);
        
        // FILAMENTO INTERNO
        Path2D.Double filament = new Path2D.Double();
        filament.moveTo(9.5, 13.0);
        filament.lineTo(13.0, 13.0);
        filament.lineTo(14.5, 11.5);
        filament.lineTo(16.0, 14.0);
        filament.lineTo(14.5, 16.5);
        filament.lineTo(13.0, 15.0);
        filament.lineTo(9.5, 15.0);
        
        g2d.setColor(active ? ACTIVE_FILAMENT : INACTIVE_FILAMENT);
        g2d.setStroke(new BasicStroke(active ? 1.1f : 0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(filament);
        
        // RAYOS DE LUZ CUANDO ESTA ENCENDIDO
        if (active) {
            g2d.setColor(ACTIVE_RAY);
            g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            // Rayos cardinales y diagonales
            g2d.drawLine(16, 5, 16, 2);     // Arriba
            g2d.drawLine(16, 23, 16, 26);   // Abajo
            g2d.drawLine(25, 14, 28, 14);   // Derecha
            g2d.drawLine(22, 8, 25, 5);     // Diagonal superior derecha
            g2d.drawLine(22, 20, 25, 23);   // Diagonal inferior derecha
        }
    }
}
