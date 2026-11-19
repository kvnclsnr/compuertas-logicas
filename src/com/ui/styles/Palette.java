package com.ui.styles;

import java.awt.Color;

// COLORES DE LA APLICACION
public final class Palette {
    private static final int ACCENT_TINT_ALPHA = 24;
    
    public static final Color BACKGROUND = new Color(240, 242, 244);
    public static final Color SURFACE = new Color(255, 255, 255);
    public static final Color LINE = new Color(205, 210, 215);
    public static final Color ACCENT = new Color(0, 126, 195);
    public static final Color INK = new Color(25, 27, 30);
    public static final Color MUTED = new Color(100, 106, 112);
    public static final Color ACCENT_TINT = new Color(
        ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), ACCENT_TINT_ALPHA
    );
    
    private Palette() { }
}