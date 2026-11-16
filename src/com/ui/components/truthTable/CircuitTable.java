package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class CircuitTable extends JTable {
    private static final int ROW_HEIGHT = 26;
    private static final int HEADER_PADDING = 16;
    
    // CREA LA TABLA; LA PRIMERA FILA DE truthTable SON LOS ENCABEZADOS Y EL RESTO LOS DATOS
    public CircuitTable(String[][] truthTable) {
        super(new DefaultTableModel(
            Arrays.copyOfRange(truthTable, 1, truthTable.length),
            truthTable[0]
        ));
        
        configureTable();
        configureHeader();
        setMinimumColumnWidths();
        
        setScrollSizes();
        
        centerCells();
    }
    
    // DA A LA TABLA EL ALTO DE FILA, LA CUADRÍCULA Y LOS COLORES
    private void configureTable() {
        // LAS COLUMNAS SE REPARTEN EL ANCHO QUE LE DÉ EL PANEL
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setRowHeight(ROW_HEIGHT);
        
        // LA TABLA SOLO SE MIRA: NO SE SELECCIONA NI SE ENFOCA
        setCellSelectionEnabled(false);
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setFocusable(false);
        
        setShowGrid(true);
        setGridColor(Palette.LINE);
        setIntercellSpacing(new Dimension(0, 0));
        
        Styles.setFont(this, 11f);
        Styles.setForeground(this, Palette.INK);
        Styles.setBackground(this, Palette.SURFACE);
    }
    
    // DA AL ENCABEZADO SU FUENTE Y SU RENDERER
    private void configureHeader() {
        JTableHeader header = getTableHeader();
        
        Styles.setFont(header, 11f, true);
        Styles.setForeground(header, Palette.INK);
        Styles.setBackground(header, Palette.SURFACE);
        Styles.setOpaque(header, true);
        
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new HeaderRenderer());
    }
    
    // CADA COLUMNA MIDE COMO MÍNIMO LO QUE OCUPA EL TEXTO DE SU ENCABEZADO, PARA QUE NO SE CORTE
    private void setMinimumColumnWidths() {
        FontMetrics metrics = getFontMetrics(getTableHeader().getFont());
        
        for (int column = 0; column < getColumnCount(); column++) {
            int width = metrics.stringWidth(getColumnName(column)) + HEADER_PADDING;
            
            TableColumn tableColumn = getColumnModel().getColumn(column);
            tableColumn.setMinWidth(width);
            tableColumn.setPreferredWidth(width);
        }
    }
    
    // CENTRA EL TEXTO DE TODAS LAS CELDAS
    private void centerCells() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        setDefaultRenderer(Object.class, renderer);
    }
    
    // LA TABLA Y SU ENCABEZADO MIDEN LO QUE OCUPAN SUS COLUMNAS, PARA QUE EL SCROLL SEPA CUÁNTO DESPLAZAR
    private void setScrollSizes() {
        getTableHeader().setPreferredSize(new Dimension(getPreferredSize().width, ROW_HEIGHT));
        setPreferredScrollableViewportSize(getPreferredSize());
    }
    
    // SE AJUSTA AL ANCHO DEL SCROLL SI SOBRA ESPACIO; SI NO, SE QUEDA ANCHA Y APARECE LA BARRA HORIZONTAL
    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (getParent() instanceof JViewport viewport) {
            return viewport.getWidth() >= getPreferredSize().width;
        }
        
        return false;
    }
    
    // NINGUNA CELDA SE PUEDE EDITAR
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}