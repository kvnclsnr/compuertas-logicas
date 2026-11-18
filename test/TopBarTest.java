package test;

import com.ui.components.menu.ExportButton;
import com.ui.components.menu.MenuButton;
import com.ui.components.menu.PopupItem;
import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.main.Content;
import com.ui.layouts.main.TopBar;
import com.ui.layouts.sidePanel.SidePanel;
import com.ui.layouts.workspace.Workspace;

import java.awt.Component;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class TopBarTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running TopBarTest...");
        
        testMenuBarStructureAndNoAyuda();
        testWiringAndConnections();
        testUndoRedoActionsAndState();
        testExportTruthTableToPng();
        testNuevoWhenClean();
        testZoomActions();
        
        System.out.println("ALL TOPBAR TESTS PASSED SUCCESSFULLY!");
    }
    
    private static void testZoomActions() {
        System.out.println("  [Test 6] Ver -> Acercar/Alejar zoom actions...");
        Workspace workspace = new Workspace();
        TopBar topBar = new TopBar(workspace);
        Canvas canvas = workspace.getCanvas();
        
        double initialZoom = canvas.getZoomFactor();
        assert Math.abs(initialZoom - 1.0) < 0.001 : "Initial zoom should be 1.0";
        
        topBar.onAcercar();
        double zoomedIn = canvas.getZoomFactor();
        assert zoomedIn > initialZoom : "Zoom should increase after onAcercar()";
        
        topBar.onAlejar();
        double zoomedBack = canvas.getZoomFactor();
        assert Math.abs(zoomedBack - initialZoom) < 0.001 : "Zoom should return to 1.0 after onAlejar()";
        
        System.out.println("  -> Test 6 passed.");
    }
    
    private static void testMenuBarStructureAndNoAyuda() {
        System.out.println("  [Test 1] Menu Bar structure and absence of 'Ayuda'...");
        TopBar topBar = new TopBar();
        
        assert topBar.getMenuArchivo() != null : "Menu Archivo should exist";
        assert topBar.getMenuEditar() != null : "Menu Editar should exist";
        assert topBar.getMenuVer() != null : "Menu Ver should exist";
        
        // Check that "Ayuda" does not exist in any menu item
        for (Component c : topBar.getComponents()) {
            if (c instanceof javax.swing.JPanel panel) {
                for (Component sub : panel.getComponents()) {
                    if (sub instanceof JMenuBar bar) {
                        for (int i = 0; i < bar.getMenuCount(); i++) {
                            JMenu m = bar.getMenu(i);
                            if (m != null) {
                                assert !"Ayuda".equalsIgnoreCase(m.getText()) : "'Ayuda' menu must NOT exist!";
                            }
                        }
                    }
                }
            }
        }
        
        // Verify options in Archivo
        MenuButton archivo = topBar.getMenuArchivo();
        assert archivo.getItem("Nuevo") != null : "'Nuevo' must exist in Archivo";
        assert archivo.getItem("Abrir") != null : "'Abrir' must exist in Archivo";
        assert archivo.getItem("Guardar") != null : "'Guardar' must exist in Archivo";
        
        // Verify options in Editar
        MenuButton editar = topBar.getMenuEditar();
        assert editar.getItem("Deshacer") != null : "'Deshacer' must exist in Editar";
        assert editar.getItem("Rehacer") != null : "'Rehacer' must exist in Editar";
        
        // Verify Export button options
        ExportButton exportBtn = topBar.getExportButton();
        assert exportBtn != null : "ExportButton must exist";
        assert exportBtn.getItem("Tabla de verdad") != null : "'Tabla de verdad' must exist in ExportButton";
        
        System.out.println("  -> Test 1 passed.");
    }
    
    private static void testWiringAndConnections() {
        System.out.println("  [Test 2] TopBar, Workspace, Canvas, and SidePanel wiring...");
        Workspace workspace = new Workspace();
        Content content = new Content(workspace);
        TopBar topBar = new TopBar(workspace);
        
        assert content.getWorkspace() == workspace : "Content should reference Workspace";
        assert content.getCanvas() == workspace.getCanvas() : "Content should provide Canvas";
        assert content.getSidePanel() == workspace.getSidePanel() : "Content should provide SidePanel";
        
        assert topBar.getWorkspace() == workspace : "TopBar should reference Workspace";
        assert topBar.getCanvas() == workspace.getCanvas() : "TopBar should reference Canvas";
        assert topBar.getSidePanel() == workspace.getSidePanel() : "TopBar should reference SidePanel";
        
        System.out.println("  -> Test 2 passed.");
    }
    
    private static void testUndoRedoActionsAndState() {
        System.out.println("  [Test 3] Editar -> Deshacer/Rehacer actions and dynamic state...");
        Workspace workspace = new Workspace();
        TopBar topBar = new TopBar(workspace);
        Canvas canvas = workspace.getCanvas();
        
        MenuButton editar = topBar.getMenuEditar();
        PopupItem itemDeshacer = editar.getItem("Deshacer");
        PopupItem itemRehacer = editar.getItem("Rehacer");
        
        // Initially no undo, no redo
        assert !itemDeshacer.isEnabled() : "Deshacer should be disabled initially";
        assert !itemRehacer.isEnabled() : "Rehacer should be disabled initially";
        
        // Add a gate
        canvas.addGate("AND", new Point(100, 100));
        assert canvas.canUndo() : "Canvas should be able to undo";
        assert itemDeshacer.isEnabled() : "Deshacer should be enabled after adding a gate";
        assert !itemRehacer.isEnabled() : "Rehacer should remain disabled";
        
        // Trigger undo via TopBar action
        topBar.onDeshacer();
        assert canvas.getComponents().length == 0 : "Canvas should be empty after onDeshacer()";
        assert !itemDeshacer.isEnabled() : "Deshacer should be disabled after undoing the only action";
        assert itemRehacer.isEnabled() : "Rehacer should be enabled after undo";
        
        // Trigger redo via TopBar action
        topBar.onRehacer();
        assert canvas.getComponents().length == 1 : "Canvas should have 1 component restored after onRehacer()";
        assert itemDeshacer.isEnabled() : "Deshacer should be enabled after redo";
        assert !itemRehacer.isEnabled() : "Rehacer should be disabled after redo";
        
        System.out.println("  -> Test 3 passed.");
    }
    
    private static void testExportTruthTableToPng() throws Exception {
        System.out.println("  [Test 4] Export truth table to PNG...");
        Workspace workspace = new Workspace();
        TopBar topBar = new TopBar(workspace);
        Canvas canvas = workspace.getCanvas();
        
        // Add Switch and Bulb and wire them to produce a real circuit truth table
        canvas.addGate("INPUT", new Point(50, 100));
        canvas.addGate("NOT", new Point(150, 100));
        
        File tempPng = Files.createTempFile("truth_table_test_", ".png").toFile();
        tempPng.deleteOnExit();
        
        boolean exported = topBar.exportTruthTableTo(tempPng);
        assert exported : "exportTruthTableTo should return true";
        assert tempPng.exists() && tempPng.length() > 0 : "Exported PNG file should exist and have size > 0";
        
        BufferedImage loadedImage = ImageIO.read(tempPng);
        assert loadedImage != null : "Exported image should be readable by ImageIO";
        assert loadedImage.getWidth() > 0 && loadedImage.getHeight() > 0 : "Image dimensions should be positive";
        
        System.out.println("  -> Test 4 passed. Exported image size: " + loadedImage.getWidth() + "x" + loadedImage.getHeight());
    }
    
    private static void testNuevoWhenClean() {
        System.out.println("  [Test 5] Nuevo when canvas is clean...");
        Workspace workspace = new Workspace();
        TopBar topBar = new TopBar(workspace);
        Canvas canvas = workspace.getCanvas();
        
        canvas.addGate("AND", new Point(100, 100));
        canvas.markSaved(); // simulate no unsaved changes
        assert !canvas.hasUnsavedChanges() : "Should have no unsaved changes after markSaved";
        
        boolean cleared = topBar.onNuevo();
        assert cleared : "onNuevo should proceed and return true";
        assert canvas.getComponents().length == 0 : "Canvas should be cleared after onNuevo";
        assert !canvas.hasUnsavedChanges() : "Canvas should have no unsaved changes after clear";
        
        System.out.println("  -> Test 5 passed.");
    }
}
