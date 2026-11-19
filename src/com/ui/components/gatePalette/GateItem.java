package com.ui.components.gatePalette;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ui.events.drag.DragStartHandler;
import com.ui.events.drag.GateTransferHandler;
import com.ui.icons.ElementFactory;
import com.ui.icons.ElementType;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

// FILA DE LA PALETA CON EL ICONO Y EL NOMBRE DE UN ELEMENTO, LISTA PARA ARRASTRAR
public class GateItem extends JPanel {
    private static final int ICON_SIZE = 64;
    private static final int ROW_HEIGHT = 72;
    private static final int CONTENT_GAP = 16;
    private static final int HORIZONTAL_PADDING = 16;
    private static final float LABEL_FONT_SIZE = 16f;
    
    // CREA LA FILA Y HACE QUE SE PUEDA ARRASTRAR DESDE LA FILA, EL ICONO O EL TEXTO
    public GateItem(ElementType type) {
        super(new BorderLayout(CONTENT_GAP, 0));
        
        configureAppearance();
        
        JComponent icon = ElementFactory.create(type, ICON_SIZE);
        JLabel label = createLabel(type);
        
        add(icon, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
        
        GateTransferHandler transferHandler = new GateTransferHandler(type);
        
        installDrag(this, transferHandler);
        installDrag(icon, transferHandler);
        installDrag(label, transferHandler);
    }
    
    // DA A LA FILA SU COLOR, SU BORDE, SU TAMAÑO Y SU CURSOR
    private void configureAppearance() {
        setBackground(Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        Styles.setPadding(this, 0, HORIZONTAL_PADDING);
        
        setPreferredSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    // CREA EL TEXTO CON EL NOMBRE DEL ELEMENTO
    private JLabel createLabel(ElementType type) {
        JLabel label = new JLabel(type.name());
        
        Styles.setFont(label, LABEL_FONT_SIZE, true);
        
        return label;
    }
    
    // HACE QUE UN COMPONENTE PUEDA INICIAR EL ARRASTRE DEL ELEMENTO
    private void installDrag(JComponent component, GateTransferHandler transferHandler) {
        DragStartHandler dragHandler = new DragStartHandler(component, transferHandler);
        
        component.setTransferHandler(transferHandler);
        component.addMouseListener(dragHandler);
        component.addMouseMotionListener(dragHandler);
    }
}