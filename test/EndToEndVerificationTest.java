package test;

import java.awt.Component;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.engine.circuit.TruthTableBuilder;
import com.engine.gates.BulbGate;
import com.engine.gates.Gate;
import com.engine.gates.GateFactory;
import com.engine.persistence.Circuito;
import com.engine.persistence.FormatoCircuitoException;
import com.engine.persistence.RepositorioTexto;
import com.ui.circuit.pin.Pin;
import com.ui.components.gatePalette.GateItem;
import com.ui.components.menu.PopupItem;
import com.ui.icons.And;
import com.ui.icons.Bulb;
import com.ui.icons.GateIconFactory;
import com.ui.icons.Switch;
import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.main.TopBar;
import com.ui.layouts.sidePanel.SidePanel;
import com.ui.layouts.workspace.GatePalette;

public class EndToEndVerificationTest {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  INICIANDO VERIFICACIÓN COMPLETA DE REQUISITOS  ");
        System.out.println("=================================================");

        try {
            testRequirement1_Archivo();
            testRequirement2_UndoRedo();
            testRequirement3_MenuAyudaRemoved();
            testRequirement4_BulbFunctionality();
            testRequirement5_ExportTruthTablePNG();
            testRequirement6_PersistenceFormatAndExceptions();
            testRequirement7_ZoomAndPan();
            testRequirement8_CyclePreventionAndSafety();

            System.out.println("=================================================");
            System.out.println("  ¡TODOS LOS REQUISITOS VERIFICADOS CON ÉXITO!   ");
            System.out.println("=================================================");
        } catch (Throwable t) {
            System.err.println("FALLO EN LA VERIFICACIÓN: " + t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Fallo en aserción: " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("Fallo: " + message + " (Esperado: " + expected + ", Actual: " + actual + ")");
    }

    /**
     * REQUISITO 1: Archivo: Nuevo (limpia, pregunta si modificado), Abrir, Guardar.
     */
    private static void testRequirement1_Archivo() throws Exception {
        System.out.println("\n[Req 1] Verificando Menú Archivo (Nuevo, Abrir, Guardar)...");

        Canvas canvas = new Canvas();
        SidePanel sidePanel = new SidePanel(canvas);
        TopBar topBar = new TopBar(canvas, sidePanel);

        // Validar opciones en menu Archivo
        PopupItem itemNuevo = topBar.getMenuArchivo().getItem("Nuevo");
        PopupItem itemAbrir = topBar.getMenuArchivo().getItem("Abrir");
        PopupItem itemGuardar = topBar.getMenuArchivo().getItem("Guardar");

        assertTrue(itemNuevo != null, "Opción 'Nuevo' debe existir en Archivo");
        assertTrue(itemAbrir != null, "Opción 'Abrir' debe existir en Archivo");
        assertTrue(itemGuardar != null, "Opción 'Guardar' debe existir en Archivo");

        // Estado inicial: canvas limpio, sin cambios
        assertTrue(!canvas.hasUnsavedChanges(), "Canvas recién creado no debe tener cambios");
        boolean nuevoResult = topBar.onNuevo();
        assertTrue(nuevoResult, "onNuevo() debe proceder sin popup cuando no hay cambios");

        // Modificar canvas
        canvas.addGate("INPUT", new Point(50, 50));
        assertTrue(canvas.hasUnsavedChanges(), "Canvas con compuerta debe reportar unsaved changes");

        // Guardar a archivo temporal
        Path tempFile = Files.createTempFile("circuito_req1_", ".txt");
        canvas.saveToFile(tempFile);
        assertTrue(!canvas.hasUnsavedChanges(), "Tras guardar, no debe haber cambios pendientes");

        // Limpiar canvas
        canvas.clearCanvas();
        assertEquals(0, canvas.getComponents().length, "clearCanvas() debe dejar el canvas sin componentes");

        // Abrir y cargar desde archivo
        canvas.loadFromFile(tempFile);
        assertEquals(1, canvas.getComponents().length, "loadFromFile() debe haber restaurado el componente");
        assertTrue(!canvas.hasUnsavedChanges(), "Tras cargar archivo, estado debe ser limpio");

        Files.deleteIfExists(tempFile);
        System.out.println("  -> [Req 1] PASADO.");
    }

    /**
     * REQUISITO 2: Editar: Deshacer y Rehacer funcionando para operaciones del lienzo.
     */
    private static void testRequirement2_UndoRedo() {
        System.out.println("\n[Req 2] Verificando Editar (Deshacer y Rehacer)...");

        Canvas canvas = new Canvas();
        SidePanel sidePanel = new SidePanel(canvas);
        TopBar topBar = new TopBar(canvas, sidePanel);

        assertTrue(!canvas.canUndo(), "Inicialmente no debe poder deshacer");
        assertTrue(!canvas.canRedo(), "Inicialmente no debe poder rehacer");

        // 1. Agregar Switch
        canvas.addGate("INPUT", new Point(30, 40));
        assertTrue(canvas.canUndo(), "Debe poder deshacer tras agregar componente");
        assertEquals(1, canvas.getComponents().length, "Canvas debe tener 1 componente");

        // Deshacer agregación
        canvas.undo();
        assertEquals(0, canvas.getComponents().length, "Tras undo, componente debe eliminarse");
        assertTrue(canvas.canRedo(), "Debe poder rehacer");

        // Rehacer agregación
        canvas.redo();
        assertEquals(1, canvas.getComponents().length, "Tras redo, componente debe reaparecer");

        // 2. Mover componente
        JComponent swComp = (JComponent) canvas.getComponents()[0];
        Point p1 = swComp.getLocation();
        Point p2 = new Point(100, 150);
        swComp.setLocation(p2);
        canvas.recordMove(swComp, p1, p2);
        assertEquals(p2, swComp.getLocation(), "Ubicación actual debe ser p2");

        canvas.undo();
        assertEquals(p1, swComp.getLocation(), "Tras undo, ubicación debe ser p1");

        canvas.redo();
        assertEquals(p2, swComp.getLocation(), "Tras redo, ubicación debe volver a p2");

        // 3. Agregar And y Bulb, y conectar cable
        canvas.addGate("AND", new Point(200, 100));
        canvas.addGate("BULB", new Point(350, 100));

        And andGate = null;
        Bulb bulb = null;
        for (Component c : canvas.getComponents()) {
            if (c instanceof And a) andGate = a;
            if (c instanceof Bulb b) bulb = b;
        }
        assertTrue(andGate != null, "AND gate must exist");
        assertTrue(bulb != null, "Bulb must exist");

        Pin outAnd = andGate.getPins().get(2);
        Pin inBulb = bulb.getPins().get(0);
        canvas.connectWire(outAnd, inBulb);

        assertEquals(1, canvas.getWires().size(), "Debe haber 1 cable conectado");

        // Deshacer conexión de cable
        canvas.undo();
        assertEquals(0, canvas.getWires().size(), "Tras undo, cable debe eliminarse");

        // Rehacer conexión de cable
        canvas.redo();
        assertEquals(1, canvas.getWires().size(), "Tras redo, cable debe reaparecer");

        // 4. Eliminar compuerta con cable conectado
        canvas.removeGate(bulb);
        assertEquals(2, canvas.getComponents().length, "Bulb eliminado, quedan 2");
        assertEquals(0, canvas.getWires().size(), "Cable conectado al bulb debe eliminarse");

        // Deshacer eliminación
        canvas.undo();
        assertEquals(3, canvas.getComponents().length, "Bulb restaurado por undo");
        assertEquals(1, canvas.getWires().size(), "Cable del bulb restaurado por undo");

        System.out.println("  -> [Req 2] PASADO.");
    }

    /**
     * REQUISITO 3: Menú Ayuda eliminado completamente.
     */
    private static void testRequirement3_MenuAyudaRemoved() {
        System.out.println("\n[Req 3] Verificando que el menú Ayuda fue eliminado...");

        TopBar topBar = new TopBar();
        // Recorrer todos los componentes del TopBar buscando menús
        for (Component comp : topBar.getComponents()) {
            if (comp instanceof javax.swing.JPanel p) {
                for (Component subComp : p.getComponents()) {
                    if (subComp instanceof JMenuBar mb) {
                        for (int i = 0; i < mb.getMenuCount(); i++) {
                            JMenu menu = mb.getMenu(i);
                            if (menu != null) {
                                String text = menu.getText();
                                assertTrue(!text.equalsIgnoreCase("Ayuda"), "El menú 'Ayuda' NO debe existir!");
                                assertTrue(!text.equalsIgnoreCase("Help"), "El menú 'Help' NO debe existir!");
                            }
                        }
                    }
                }
            }
        }

        System.out.println("  -> [Req 3] PASADO.");
    }

    /**
     * REQUISITO 4: Foco / Bulb en paleta, conecta a salida de compuerta, prende con true y apaga con false.
     */
    private static void testRequirement4_BulbFunctionality() {
        System.out.println("\n[Req 4] Verificando Foco (Bulb) en paleta y comportamiento lógico...");

        // 1. Verificar en paleta
        GatePalette palette = new GatePalette();
        boolean foundInPalette = false;
        Component viewportView = palette.getViewport().getView();
        if (viewportView instanceof javax.swing.JPanel panel) {
            for (Component c : panel.getComponents()) {
                if (c instanceof GateItem item) {
                    for (Component sub : item.getComponents()) {
                        if (sub instanceof javax.swing.JLabel lbl) {
                            if ("BULB".equalsIgnoreCase(lbl.getText()) || "FOCO".equalsIgnoreCase(lbl.getText())) {
                                foundInPalette = true;
                                break;
                            }
                        } else if (sub instanceof Bulb) {
                            foundInPalette = true;
                            break;
                        }
                    }
                }
            }
        }
        assertTrue(foundInPalette, "El Foco (BULB) debe estar presente en GatePalette");

        // 2. Verificar GateFactory y GateIconFactory
        assertTrue(GateIconFactory.create("BULB", 96) instanceof Bulb, "GateIconFactory debe crear Bulb para 'BULB'");
        assertTrue(GateIconFactory.create("FOCO", 96) instanceof Bulb, "GateIconFactory debe crear Bulb para 'FOCO'");
        assertTrue(GateFactory.createGate(GateFactory.Type.BULB, "B1") instanceof BulbGate, "GateFactory Type.BULB");
        assertTrue(GateFactory.createGate(GateFactory.Type.FOCO, "F1") instanceof BulbGate, "GateFactory Type.FOCO");

        // 3. Probar circuito: Switch -> AND -> Foco
        Canvas canvas = new Canvas();
        canvas.addGate("INPUT", new Point(20, 20));
        canvas.addGate("INPUT", new Point(20, 100));
        canvas.addGate("AND", new Point(140, 60));
        canvas.addGate("BULB", new Point(260, 60));

        Switch sw1 = null;
        Switch sw2 = null;
        And andGate = null;
        Bulb bulb = null;

        for (Component c : canvas.getComponents()) {
            if (c instanceof Switch sw) {
                if (sw1 == null) sw1 = sw;
                else sw2 = sw;
            } else if (c instanceof And a) {
                andGate = a;
            } else if (c instanceof Bulb b) {
                bulb = b;
            }
        }

        assertTrue(sw1 != null && sw2 != null && andGate != null && bulb != null, "Todos los componentes deben existir");

        // Verificar que el foco tiene exactamente 1 pin de entrada y ninguno de salida
        assertEquals(1, bulb.getPins().size(), "Foco debe tener exactamente 1 pin");
        assertEquals(Pin.Type.INPUT, bulb.getPins().get(0).getType(), "Pin de foco debe ser de tipo INPUT");
        boolean hasOutput = bulb.getPins().stream().anyMatch(p -> p.getType() == Pin.Type.OUTPUT);
        assertTrue(!hasOutput, "Foco no debe tener pin de salida");

        // Conectar sw1 -> and.pinA, sw2 -> and.pinB, and.out -> bulb.in
        canvas.connectWire(sw1.getOutputPin(), andGate.getPins().get(0));
        canvas.connectWire(sw2.getOutputPin(), andGate.getPins().get(1));
        canvas.connectWire(andGate.getPins().get(2), bulb.getPins().get(0));

        // Inicialmente switches están en false -> AND es false -> Foco apagado
        canvas.evaluateCircuits();
        assertTrue(!bulb.isOn(), "Foco debe estar apagado cuando entradas son false");
        assertTrue(!bulb.isActive(), "Foco isActive() debe ser false");

        // Encender sw1 (1 AND 0 = 0) -> Foco sigue apagado
        sw1.toggle();
        canvas.evaluateCircuits();
        assertTrue(!bulb.isOn(), "Foco debe estar apagado con (1 AND 0)");

        // Encender sw2 (1 AND 1 = 1) -> Foco prende!
        sw2.toggle();
        canvas.evaluateCircuits();
        assertTrue(bulb.isOn(), "Foco debe estar encendido con (1 AND 1)");
        assertTrue(bulb.isActive(), "Foco isActive() debe ser true");

        // Apagar sw1 (0 AND 1 = 0) -> Foco apaga!
        sw1.toggle();
        canvas.evaluateCircuits();
        assertTrue(!bulb.isOn(), "Foco debe apagarse con (0 AND 1)");

        // 4. Verificar que foco no genera tabla de verdad adicional cuando está desconectado
        Canvas isolatedCanvas = new Canvas();
        isolatedCanvas.addGate("AND", new Point(50, 50));
        isolatedCanvas.addGate("BULB", new Point(200, 50));
        assertEquals(1, isolatedCanvas.getCircuitTruthTables().size(), "Canvas con 1 AND y 1 FOCO desconectado debe tener exactamente 1 tabla de verdad (no 2)");

        System.out.println("  -> [Req 4] PASADO.");
    }

    /**
     * REQUISITO 5: Exportar imagen de la tabla de verdad como PNG.
     */
    private static void testRequirement5_ExportTruthTablePNG() throws IOException {
        System.out.println("\n[Req 5] Verificando Exportar tabla de verdad a PNG...");

        Canvas canvas = new Canvas();
        canvas.addGate("INPUT", new Point(20, 20));

        SidePanel sidePanel = new SidePanel(canvas);
        TopBar topBar = new TopBar(canvas, sidePanel);

        // Validar opción en ExportButton
        PopupItem itemTabla = topBar.getExportButton().getItem("Tabla de verdad");
        assertTrue(itemTabla != null, "Opción 'Tabla de verdad' debe existir en Exportar");

        // Generar imagen
        File tempImage = File.createTempFile("tabla_test_", ".png");
        tempImage.deleteOnExit();

        boolean exported = topBar.exportTruthTableTo(tempImage);
        assertTrue(exported, "exportTruthTableTo() debe retornar true");
        assertTrue(tempImage.exists(), "El archivo PNG exportado debe existir");
        assertTrue(tempImage.length() > 0, "El archivo PNG no debe estar vacío");

        // Verificar que es un PNG válido leyendo sus dimensiones
        BufferedImage img = ImageIO.read(tempImage);
        assertTrue(img != null, "La imagen leída no debe ser nula");
        assertTrue(img.getWidth() > 0 && img.getHeight() > 0, "La imagen debe tener dimensiones positivas");

        // Verificar encabezado PNG (primeros 8 bytes: 89 50 4E 47 0D 0A 1A 0A)
        try (FileInputStream fis = new FileInputStream(tempImage)) {
            byte[] header = new byte[8];
            int read = fis.read(header);
            assertEquals(8, read, "Se deben leer 8 bytes de cabecera");
            assertEquals((byte) 0x89, header[0], "Cabecera PNG byte 0");
            assertEquals((byte) 'P', header[1], "Cabecera PNG byte 1");
            assertEquals((byte) 'N', header[2], "Cabecera PNG byte 2");
            assertEquals((byte) 'G', header[3], "Cabecera PNG byte 3");
        }

        Files.deleteIfExists(tempImage.toPath());
        System.out.println("  -> [Req 5] PASADO.");
    }

    /**
     * REQUISITO 6: Persistencia con archivos de texto según diapositivas de clase 14 y 15.
     */
    private static void testRequirement6_PersistenceFormatAndExceptions() throws Exception {
        System.out.println("\n[Req 6] Verificando Persistencia (RepositorioTexto, FormatoCircuitoException, CIRCUITO;1)...");

        RepositorioTexto repo = new RepositorioTexto();
        Path tempFile = Files.createTempFile("circuito_spec_", ".txt");

        try {
            // 1. Probar guardado de circuito completo
            Circuito c = new Circuito("1");
            c.addEntrada("IN-1", true, 50, 60);
            c.addEntrada("IN-2", false, 50, 150);
            c.addCompuerta("G-1", "AND", 180, 100);
            c.addFoco("F-1", 300, 100);
            c.addConexion("IN-1", "OUT", "G-1", "IN-0");
            c.addConexion("IN-2", "OUT", "G-1", "IN-1");
            c.addConexion("G-1", "OUT", "F-1", "IN-0");

            repo.guardar(c, tempFile);

            // Verificar el contenido del archivo de texto
            String content = Files.readString(tempFile);
            System.out.println("Contenido del archivo guardado:\n" + content);

            String[] lines = content.split("\r?\n");
            assertEquals("CIRCUITO;1", lines[0], "Línea 1 debe ser exactamente 'CIRCUITO;1'");
            assertTrue(content.contains("ENTRADA;IN-1;true;50;60"), "Debe contener ENTRADA;IN-1");
            assertTrue(content.contains("COMPUERTA;G-1;AND;180;100"), "Debe contener COMPUERTA;G-1;AND");
            assertTrue(content.contains("FOCO;F-1;300;100"), "Debe contener FOCO;F-1");
            assertTrue(content.contains("CONEXION;IN-1;OUT;G-1;IN-0"), "Debe contener CONEXION");

            // 2. Cargar en Circuito DTO y verificar integridad
            Circuito cargado = repo.cargar(tempFile);
            assertEquals(4, cargado.getComponentes().size(), "Debe tener 4 componentes");
            assertEquals(3, cargado.getConexiones().size(), "Debe tener 3 conexiones");

            // 3. Probar aplicación en Canvas
            Canvas canvas = new Canvas();
            repo.aplicarEnCanvas(cargado, canvas);
            assertEquals(4, canvas.getComponents().length, "Canvas debe tener 4 componentes restaurados");
            assertEquals(3, canvas.getWires().size(), "Canvas debe tener 3 cables restaurados");

            // 4. Probar captura de FormatoCircuitoException
            Path badHeaderFile = Files.createTempFile("bad_header_", ".txt");
            Files.writeString(badHeaderFile, "CIRCUITO;99\nENTRADA;1;true;0;0\n");
            try {
                repo.cargar(badHeaderFile);
                throw new AssertionError("Debió fallar por versión incorrecta");
            } catch (FormatoCircuitoException ex) {
                assertTrue(ex.getNumeroLinea() == 1, "Debe reportar error en línea 1");
                System.out.println("  Excepción capturada como esperado: " + ex.getMessage());
            } finally {
                Files.deleteIfExists(badHeaderFile);
            }
        } finally {
            Files.deleteIfExists(tempFile);
        }

        System.out.println("  -> [Req 6] PASADO.");
    }

    /**
     * REQUISITO 7: Funcionalidad de Acercar/Alejar en Ver, y navegación por el circuito con Space_bar + Right-click.
     */
    private static void testRequirement7_ZoomAndPan() {
        System.out.println("\n[Req 7] Verificando Acercar/Alejar (Zoom) y Navegación (Space + Right-Click Pan)...");

        Canvas canvas = new Canvas();
        canvas.setSize(800, 600);
        SidePanel sidePanel = new SidePanel(canvas);
        TopBar topBar = new TopBar(canvas, sidePanel);

        // Validar opciones en Ver
        PopupItem itemAcercar = topBar.getMenuVer().getItem("Acercar");
        PopupItem itemAlejar = topBar.getMenuVer().getItem("Alejar");
        assertTrue(itemAcercar != null, "Opción 'Acercar' debe existir en Ver");
        assertTrue(itemAlejar != null, "Opción 'Alejar' debe existir en Ver");

        // 1. Probar zoom inicial
        double initialZoom = canvas.getZoomFactor();
        assertEquals(1.0, initialZoom, "Zoom inicial debe ser 1.0");

        // Agregar compuertas
        canvas.addGate("AND", new Point(200, 200));
        canvas.addGate("INPUT", new Point(100, 200));
        assertEquals(2, canvas.getComponents().length, "Debe haber 2 componentes");

        JComponent andGate = null;
        for (Component c : canvas.getComponents()) {
            if (c instanceof And a) andGate = a;
        }
        assertTrue(andGate != null, "AND gate encontrado");
        int initialWidth = andGate.getWidth();

        // 2. Probar Acercar vía TopBar
        topBar.onAcercar();
        assertTrue(canvas.getZoomFactor() > 1.0, "Zoom debe aumentar con onAcercar");
        assertTrue(andGate.getWidth() > initialWidth, "Ancho del componente debe aumentar");

        // 3. Probar Alejar vía TopBar
        topBar.onAlejar();
        assertEquals(1.0, Math.round(canvas.getZoomFactor() * 1000.0) / 1000.0, "Zoom debe volver a 1.0 tras onAlejar");
        assertEquals(initialWidth, andGate.getWidth(), "Ancho debe restaurarse");

        // 4. Probar límites de Zoom (clamping)
        for (int i = 0; i < 20; i++) canvas.zoomIn();
        assertTrue(canvas.getZoomFactor() <= 2.5, "Zoom no debe exceder 2.5");

        for (int i = 0; i < 30; i++) canvas.zoomOut();
        assertTrue(canvas.getZoomFactor() >= 0.4, "Zoom no debe ser menor a 0.4");

        canvas.resetZoom();
        assertEquals(1.0, Math.round(canvas.getZoomFactor() * 1000.0) / 1000.0, "Reset zoom debe ser 1.0");

        // 5. Probar Pan con Spacebar + Right Click
        Point beforeLocation = andGate.getLocation();
        canvas.pan(40, -25);
        assertEquals(new Point(beforeLocation.x + 40, beforeLocation.y - 25), andGate.getLocation(), "Pan debe desplazar compuertas");

        // 6. Probar estado de barra espaciadora
        assertTrue(!canvas.isSpacePressed(), "Spacebar no debe estar presionado inicialmente");
        canvas.setSpacePressed(true);
        assertTrue(canvas.isSpacePressed(), "isSpacePressed debe ser true");
        assertEquals(java.awt.Cursor.HAND_CURSOR, canvas.getCursor().getType(), "Cursor de canvas debe ser HAND_CURSOR con Space");

        canvas.setSpacePressed(false);
        assertTrue(!canvas.isSpacePressed(), "Spacebar liberado");
        assertEquals(java.awt.Cursor.DEFAULT_CURSOR, canvas.getCursor().getType(), "Cursor de canvas debe volver a DEFAULT_CURSOR");

        System.out.println("  -> [Req 7] PASADO.");
    }

    /**
     * REQUISITO 8: Prevención y manejo defensivo de bucles/ciclos de retroalimentación.
     */
    private static void testRequirement8_CyclePreventionAndSafety() {
        System.out.println("\n[Req 8] Verificando Detección y Prevención de Ciclos de Retroalimentación...");

        Canvas canvas = new Canvas();
        canvas.addGate("NAND", new Point(100, 100));
        canvas.addGate("NOT", new Point(250, 100));
        canvas.addGate("AND", new Point(400, 100));

        com.ui.icons.LogicGate nandGate = (com.ui.icons.LogicGate) canvas.getComponents()[0];
        com.ui.icons.LogicGate notGate = (com.ui.icons.LogicGate) canvas.getComponents()[1];
        com.ui.icons.LogicGate andGate = (com.ui.icons.LogicGate) canvas.getComponents()[2];

        Pin nandOut = null;
        Pin nandIn0 = null;
        for (Pin p : nandGate.getPins()) {
            if (p.getType() == Pin.Type.OUTPUT) nandOut = p;
            else if (nandIn0 == null) nandIn0 = p;
        }

        Pin notOut = null;
        Pin notIn = null;
        for (Pin p : notGate.getPins()) {
            if (p.getType() == Pin.Type.OUTPUT) notOut = p;
            else notIn = p;
        }

        Pin andOut = null;
        Pin andIn0 = null;
        for (Pin p : andGate.getPins()) {
            if (p.getType() == Pin.Type.OUTPUT) andOut = p;
            else if (andIn0 == null) andIn0 = p;
        }

        // 1. Auto-ciclo (Self-loop)
        assertTrue(canvas.wouldCreateCycle(nandOut, nandIn0), "Auto-ciclo en compuerta debe ser detectado");
        canvas.connectWire(nandOut, nandIn0);
        assertEquals(0, canvas.getWires().size(), "Auto-ciclo debe ser rechazado sin conectar cable");

        // 2. Conexión válida NAND -> NOT
        assertTrue(!canvas.wouldCreateCycle(nandOut, notIn), "NAND -> NOT es una conexión válida");
        canvas.connectWire(nandOut, notIn);
        assertEquals(1, canvas.getWires().size(), "Cable NAND -> NOT debe conectarse");

        // 3. Ciclo de 2 compuertas NOT -> NAND (caso reportado por el usuario con captura de pantalla)
        assertTrue(canvas.wouldCreateCycle(notOut, nandIn0), "Bucle NOT <-> NAND debe ser detectado");
        canvas.connectWire(notOut, nandIn0);
        assertEquals(1, canvas.getWires().size(), "Conexión que crea bucle NOT <-> NAND debe ser rechazada");

        // 4. Conexión válida NOT -> AND (NAND -> NOT -> AND)
        assertTrue(!canvas.wouldCreateCycle(notOut, andIn0), "NOT -> AND es una conexión válida");
        canvas.connectWire(notOut, andIn0);
        assertEquals(2, canvas.getWires().size(), "Cable NOT -> AND debe conectarse");

        // 5. Ciclo de 3 compuertas AND -> NAND (AND -> NAND crea NAND -> NOT -> AND -> NAND)
        assertTrue(canvas.wouldCreateCycle(andOut, nandIn0), "Ciclo de 3 compuertas debe ser detectado");
        canvas.connectWire(andOut, nandIn0);
        assertEquals(2, canvas.getWires().size(), "Conexión que crea ciclo de 3 compuertas debe ser rechazada");

        // 6. Protección defensiva en el motor (Gate.getValue y Gate.getExpression sin StackOverflowError)
        Gate gA = GateFactory.createGate(GateFactory.Type.NOT, "A");
        Gate gB = GateFactory.createGate(GateFactory.Type.NAND, "B");
        gA.setInput(0, gB);
        gB.setInput(0, gA);

        // Debe retornar sin lanzar StackOverflowError
        boolean valA = gA.getValue();
        boolean valB = gB.getValue();
        // La llamada no debe desbordar la pila y debe completar con éxito
        assertTrue(valA == true || valA == false, "Evaluación de ciclo no lanza StackOverflowError");

        String exprA = gA.getExpression();
        assertTrue(exprA != null && exprA.contains("[CICLO:"), "Expresión con ciclo debe reportar [CICLO:] sin desbordar la pila");

        // TruthTableBuilder con ciclo
        assertTrue(TruthTableBuilder.hasCycle(List.of(gA, gB)), "TruthTableBuilder debe detectar ciclo");
        String[][] table = TruthTableBuilder.build(List.of(gA, gB));
        assertTrue(table.length == 1 && table[0][0].contains("retroalimentación"), "Tabla de verdad debe indicar retroalimentación");

        System.out.println("  -> [Req 8] PASADO.");
    }
}
