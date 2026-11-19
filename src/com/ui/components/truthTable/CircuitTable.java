package com.ui.components.truthTable;

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

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// TABLA DE VERDAD DE UN CIRCUITO, SOLO PARA MIRAR
public class CircuitTable extends JTable {
    private static final int ROW_HEIGHT = 26;
    private static final int HEADER_PADDING = 16;
    private static final int HEADER_ROW = 0;
    private static final int FIRST_DATA_ROW = 1;
    private static final float FONT_SIZE = 11f;
    
    // CREA LA TABLA, LA PRIMERA FILA SON LOS ENCABEZADOS Y EL RESTO LOS DATOS
    public CircuitTable(String[][] truthTable) {
        super(new DefaultTableModel(
            Arrays.copyOfRange(truthTable, FIRST_DATA_ROW, truthTable.length),
            truthTable[HEADER_ROW]
        ));
        
        configureTable();
        configureHeader();
        setMinimumColumnWidths();
        setScrollSizes();
        centerCells();
    }
    
    // SE AJUSTA AL ANCHO DEL SCROLL SI SOBRA ESPACIO, SI NO SE QUEDA ANCHA Y APARECE LA BARRA HORIZONTAL
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
    
    // DA A LA TABLA SU ALTO DE FILA, SU CUADRICULA Y SUS COLORES, Y LA DEJA SIN SELECCION
    private void configureTable() {
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setRowHeight(ROW_HEIGHT);
        
        setCellSelectionEnabled(false);
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setFocusable(false);
        
        setShowGrid(true);
        setGridColor(Palette.LINE);
        setIntercellSpacing(new Dimension(0, 0));
        
        Styles.setFont(this, FONT_SIZE);
        setForeground(Palette.INK);
        setBackground(Palette.SURFACE);
    }
    
    // DA AL ENCABEZADO SU FUENTE, SU COLOR Y SU DIBUJANTE, Y NO DEJA MOVERLO
    private void configureHeader() {
        JTableHeader header = getTableHeader();
        
        Styles.setFont(header, FONT_SIZE, true);
        header.setForeground(Palette.INK);
        header.setBackground(Palette.SURFACE);
        header.setOpaque(true);
        
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new HeaderRenderer());
    }
    
    // CADA COLUMNA MIDE COMO MINIMO LO QUE OCUPA EL TEXTO DE SU ENCABEZADO PARA QUE NO SE CORTE
    private void setMinimumColumnWidths() {
        FontMetrics metrics = getFontMetrics(getTableHeader().getFont());
        
        for (int column = 0; column < getColumnCount(); column++) {
            int width = metrics.stringWidth(getColumnName(column)) + HEADER_PADDING;
            
            TableColumn tableColumn = getColumnModel().getColumn(column);
            tableColumn.setMinWidth(width);
            tableColumn.setPreferredWidth(width);
        }
    }
    
    // LA TABLA Y SU ENCABEZADO MIDEN LO QUE OCUPAN SUS COLUMNAS PARA QUE EL SCROLL SEPA CUANTO DESPLAZAR
    private void setScrollSizes() {
        getTableHeader().setPreferredSize(new Dimension(getPreferredSize().width, ROW_HEIGHT));
        setPreferredScrollableViewportSize(getPreferredSize());
    }
    
    // CENTRA EL TEXTO DE TODAS LAS CELDAS
    private void centerCells() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        setDefaultRenderer(Object.class, renderer);
    }
}