package com.ui.components.gatePalette;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ui.events.drag.DragStartHandler;
import com.ui.events.drag.GateTransferHandler;
import com.ui.icons.GateIconFactory;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class GateItem extends JPanel {
    private static final int ICON_SIZE = 64;
    private static final int ROW_HEIGHT = 72;
    
    // CREA LA FILA DE UNA COMPUERTA CON SU ICONO Y SU NOMBRE, LISTA PARA ARRASTRAR
    public GateItem(String name) {
        super(new BorderLayout(16, 0));
        
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        Styles.setPadding(this, 0, 16);
        
        setPreferredSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JComponent icon = GateIconFactory.create(name, ICON_SIZE);
        
        if (icon == null) {
            throw new IllegalArgumentException("Unknown gate: " + name);
        }
        
        JLabel label = new JLabel(name);
        
        Styles.setFont(label, 16f, true);
        
        add(icon, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
        
        // SE PUEDE ARRASTRAR AGARRANDO LA FILA, EL ICONO O EL TEXTO
        GateTransferHandler transferHandler = new GateTransferHandler(name);
        
        installDrag(this, transferHandler);
        installDrag(icon, transferHandler);
        installDrag(label, transferHandler);
    }
    
    // HACE QUE ESE COMPONENTE PUEDA INICIAR EL ARRASTRE DE LA COMPUERTA
    private void installDrag(JComponent component, GateTransferHandler transferHandler) {
        component.setTransferHandler(transferHandler);
        
        DragStartHandler dragHandler = new DragStartHandler(component, transferHandler);
        
        component.addMouseListener(dragHandler);
        component.addMouseMotionListener(dragHandler);
    }
}