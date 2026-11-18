package com.ui.layouts.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.engine.persistence.FormatoCircuitoException;
import com.ui.components.common.CenteredPanel;
import com.ui.components.menu.ExportButton;
import com.ui.components.menu.MenuButton;
import com.ui.components.menu.PopupItem;
import com.ui.history.UndoRedoListener;
import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.sidePanel.SidePanel;
import com.ui.layouts.workspace.Workspace;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class TopBar extends JPanel {
    
    // TOOLBAR (Menú "Ayuda" eliminado completamente según Requisito 3)
    private static final String[] MENU_NAMES = { "Archivo", "Editar", "Ver" };
    private static final String[][] MENU_OPTIONS = {
        { "Nuevo", "Abrir", "Guardar" },
        { "Deshacer", "Rehacer" },
        { "Acercar", "Alejar" },
    };
    
    // EXPORT
    private static final String EXPORT_LABEL = "Exportar";
    private static final String[] EXPORT_OPTIONS = { "JSON", "Tabla de verdad" };
    
    private Workspace workspace;
    private Canvas canvas;
    private SidePanel sidePanel;
    
    private MenuButton menuArchivo;
    private MenuButton menuEditar;
    private MenuButton menuVer;
    private ExportButton exportButton;
    
    private final UndoRedoListener historyListener = this::updateUndoRedoState;
    
    // CONSTRUCTORES
    public TopBar() {
        this((Workspace) null);
    }
    
    public TopBar(Workspace workspace) {
        this(workspace != null ? workspace.getCanvas() : null,
             workspace != null ? workspace.getSidePanel() : null);
        this.workspace = workspace;
    }
    
    public TopBar(Canvas canvas, SidePanel sidePanel) {
        this.canvas = canvas;
        this.sidePanel = sidePanel;
        
        configureMenuColors();
        
        setLayout(new BorderLayout());
        Styles.setOpaque(this, true);
        Styles.setBackground(this, Palette.SURFACE);
        
        Styles.setPadding(this, 0, 16);
        Styles.setBorder(this, 0, 0, 1, 0, Palette.LINE);
        
        setPreferredSize(new Dimension(0, 36));
        
        add(new CenteredPanel(createMenuBar(), GridBagConstraints.WEST), BorderLayout.WEST);
        
        exportButton = createExportButton();
        add(new CenteredPanel(exportButton, GridBagConstraints.EAST), BorderLayout.EAST);
        
        setupActions();
        attachUndoRedoListener();
    }
    
    // SETTERS Y GETTERS
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        if (workspace != null) {
            setCanvas(workspace.getCanvas());
            setSidePanel(workspace.getSidePanel());
        }
    }
    
    public Workspace getWorkspace() {
        return workspace;
    }
    
    public void setCanvas(Canvas canvas) {
        if (this.canvas != null && this.canvas.getUndoRedoManager() != null) {
            this.canvas.getUndoRedoManager().removeListener(historyListener);
        }
        this.canvas = canvas;
        attachUndoRedoListener();
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public void setSidePanel(SidePanel sidePanel) {
        this.sidePanel = sidePanel;
    }
    
    public SidePanel getSidePanel() {
        return sidePanel;
    }
    
    public MenuButton getMenuArchivo() {
        return menuArchivo;
    }
    
    public MenuButton getMenuEditar() {
        return menuEditar;
    }
    
    public MenuButton getMenuVer() {
        return menuVer;
    }
    
    public ExportButton getExportButton() {
        return exportButton;
    }
    
    // DEFINE LOS COLORES DE SELECCIÓN DE LOS MENÚS EN LA INTERFAZ
    private void configureMenuColors() {
        UIManager.put("Menu.selectionForeground", Palette.ACCENT);
        UIManager.put("Menu.selectionBackground", Palette.SURFACE);
        UIManager.put("MenuItem.selectionForeground", Palette.INK);
        UIManager.put("MenuItem.selectionBackground", Palette.BACKGROUND);
    }
    
    // CREA CADA MENÚ CON LAS OPCIONES DE SU MISMA POSICIÓN EN LA LISTA
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        Styles.setBorder(menuBar, BorderFactory.createEmptyBorder());
        
        for (int i = 0; i < MENU_NAMES.length; i++) {
            MenuButton menu = new MenuButton(MENU_NAMES[i], MENU_OPTIONS[i]);
            menuBar.add(menu);
            if ("Archivo".equals(MENU_NAMES[i])) {
                menuArchivo = menu;
            } else if ("Editar".equals(MENU_NAMES[i])) {
                menuEditar = menu;
            } else if ("Ver".equals(MENU_NAMES[i])) {
                menuVer = menu;
            }
        }
        
        return menuBar;
    }
    
    // CREA EL BOTÓN CON LOS FORMATOS DISPONIBLES PARA EXPORTAR
    private ExportButton createExportButton() {
        return new ExportButton(EXPORT_LABEL, EXPORT_OPTIONS);
    }
    
    // CONECTA LOS EVENTOS DE LOS MENÚS Y BOTÓN DE EXPORTACIÓN
    private void setupActions() {
        if (menuArchivo != null) {
            PopupItem itemNuevo = menuArchivo.getItem("Nuevo");
            if (itemNuevo != null) {
                itemNuevo.addActionListener(e -> onNuevo());
            }
            PopupItem itemAbrir = menuArchivo.getItem("Abrir");
            if (itemAbrir != null) {
                itemAbrir.addActionListener(e -> onAbrir());
            }
            PopupItem itemGuardar = menuArchivo.getItem("Guardar");
            if (itemGuardar != null) {
                itemGuardar.addActionListener(e -> onGuardar());
            }
        }
        
        if (menuEditar != null) {
            PopupItem itemDeshacer = menuEditar.getItem("Deshacer");
            if (itemDeshacer != null) {
                itemDeshacer.addActionListener(e -> onDeshacer());
            }
            PopupItem itemRehacer = menuEditar.getItem("Rehacer");
            if (itemRehacer != null) {
                itemRehacer.addActionListener(e -> onRehacer());
            }
        }
        
        if (menuVer != null) {
            PopupItem itemAcercar = menuVer.getItem("Acercar");
            if (itemAcercar != null) {
                itemAcercar.addActionListener(e -> onAcercar());
            }
            PopupItem itemAlejar = menuVer.getItem("Alejar");
            if (itemAlejar != null) {
                itemAlejar.addActionListener(e -> onAlejar());
            }
        }
        
        if (exportButton != null) {
            PopupItem itemTabla = exportButton.getItem("Tabla de verdad");
            if (itemTabla != null) {
                itemTabla.addActionListener(e -> onExportTruthTable());
            }
        }
    }
    
    private void attachUndoRedoListener() {
        if (canvas != null && canvas.getUndoRedoManager() != null) {
            canvas.getUndoRedoManager().addListener(historyListener);
        }
        updateUndoRedoState();
    }
    
    private void updateUndoRedoState() {
        if (menuEditar != null) {
            PopupItem itemDeshacer = menuEditar.getItem("Deshacer");
            if (itemDeshacer != null) {
                itemDeshacer.setEnabled(canvas != null && canvas.canUndo());
            }
            PopupItem itemRehacer = menuEditar.getItem("Rehacer");
            if (itemRehacer != null) {
                itemRehacer.setEnabled(canvas != null && canvas.canRedo());
            }
        }
    }
    
    // ACCIONES DE ARCHIVO
    
    /**
     * Nuevo: Limpia el lienzo. Si hay cambios no guardados, pregunta al usuario si desea guardar primero.
     * @return true si se limpió el lienzo, false si fue cancelado por el usuario.
     */
    public boolean onNuevo() {
        if (canvas == null) {
            return false;
        }
        
        if (canvas.hasUnsavedChanges()) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "¿Desea guardar los cambios del circuito antes de crear uno nuevo?",
                "Guardar cambios",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                boolean saved = onGuardar();
                if (!saved) {
                    return false; // El usuario canceló el guardado o falló
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false; // Operación cancelada
            }
        }
        
        canvas.clearCanvas();
        return true;
    }
    
    /**
     * Abrir: Abre un JFileChooser (.txt). Si hay cambios sin guardar, pregunta primero.
     * Carga el circuito usando RepositorioTexto.
     * @return true si se cargó un circuito con éxito, false en caso contrario.
     */
    public boolean onAbrir() {
        if (canvas == null) {
            return false;
        }
        
        if (canvas.hasUnsavedChanges()) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "¿Desea guardar los cambios del circuito antes de abrir otro archivo?",
                "Guardar cambios",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                boolean saved = onGuardar();
                if (!saved) {
                    return false; // Canceló el guardado o falló
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false; // Operación cancelada
            }
        }
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir circuito");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de circuito (*.txt)", "txt"));
        
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try {
                canvas.loadFromFile(selectedFile.toPath());
                return true;
            } catch (FormatoCircuitoException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error en el formato del circuito:\n" + ex.getMessage(),
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al abrir el archivo:\n" + ex.getMessage(),
                    "Error de lectura",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return false;
    }
    
    /**
     * Guardar: Abre un JFileChooser (.txt) y guarda el circuito actual usando RepositorioTexto.
     * @return true si se guardó exitosamente, false en caso de cancelación o error.
     */
    public boolean onGuardar() {
        if (canvas == null) {
            return false;
        }
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar circuito");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de circuito (*.txt)", "txt"));
        chooser.setSelectedFile(new File("circuito.txt"));
        
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            
            if (file.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(
                    this,
                    "El archivo \"" + file.getName() + "\" ya existe.\n¿Desea reemplazarlo?",
                    "Confirmar reemplazo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (overwrite != JOptionPane.YES_OPTION) {
                    return false;
                }
            }
            
            try {
                canvas.saveToFile(file.toPath());
                JOptionPane.showMessageDialog(
                    this,
                    "Circuito guardado exitosamente.",
                    "Guardar",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el archivo:\n" + ex.getMessage(),
                    "Error al guardar",
                    JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        return false;
    }
    
    // ACCIONES DE EDICIÓN
    
    public void onDeshacer() {
        if (canvas != null) {
            canvas.undo();
        }
    }
    
    public void onRehacer() {
        if (canvas != null) {
            canvas.redo();
        }
    }
    
    // ACCIONES DE VISTA (ZOOM)
    
    public void onAcercar() {
        if (canvas != null) {
            canvas.zoomIn();
        }
    }
    
    public void onAlejar() {
        if (canvas != null) {
            canvas.zoomOut();
        }
    }
    
    // ACCIONES DE EXPORTACIÓN
    
    /**
     * Exporta la tabla de verdad actual a un archivo de imagen PNG.
     */
    public void onExportTruthTable() {
        if (sidePanel == null && workspace != null) {
            sidePanel = workspace.getSidePanel();
        }
        if (sidePanel == null) {
            JOptionPane.showMessageDialog(
                this,
                "No hay panel de tabla de verdad disponible para exportar.",
                "Exportar",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar Tabla de Verdad");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes PNG (*.png)", "png"));
        chooser.setSelectedFile(new File("tabla_de_verdad.png"));
        
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }
            
            if (file.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(
                    this,
                    "El archivo \"" + file.getName() + "\" ya existe.\n¿Desea reemplazarlo?",
                    "Confirmar reemplazo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            try {
                exportTruthTableTo(file);
                JOptionPane.showMessageDialog(
                    this,
                    "Tabla de verdad exportada como imagen PNG exitosamente.",
                    "Exportar",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al exportar la imagen:\n" + ex.getMessage(),
                    "Error de exportación",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Renderiza la tabla de verdad (SidePanel) en un BufferedImage de alta calidad.
     */
    public BufferedImage renderTruthTableImage() {
        if (sidePanel == null && workspace != null) {
            sidePanel = workspace.getSidePanel();
        }
        if (sidePanel == null) {
            return null;
        }
        
        int width = sidePanel.getWidth();
        int height = sidePanel.getHeight();
        if (width <= 0 || height <= 0) {
            Dimension pref = sidePanel.getPreferredSize();
            width = Math.max(pref.width, 360);
            height = Math.max(pref.height, 400);
            sidePanel.setSize(width, height);
            sidePanel.doLayout();
        }
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(sidePanel.getBackground());
            g2.fillRect(0, 0, width, height);
            sidePanel.paint(g2);
        } finally {
            g2.dispose();
        }
        
        return image;
    }
    
    /**
     * Guarda la imagen de la tabla de verdad directamente en el archivo PNG especificado.
     */
    public boolean exportTruthTableTo(File file) throws IOException {
        BufferedImage image = renderTruthTableImage();
        if (image == null) {
            return false;
        }
        return ImageIO.write(image, "png", file);
    }
}
