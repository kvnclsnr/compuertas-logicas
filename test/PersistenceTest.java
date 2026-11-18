package test;

import com.engine.persistence.Circuito;
import com.engine.persistence.FormatoCircuitoException;
import com.engine.persistence.RepositorioTexto;
import com.ui.circuit.pin.Pin;
import com.ui.icons.Bulb;
import com.ui.icons.LogicGate;
import com.ui.icons.Switch;
import com.ui.layouts.canvas.Canvas;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class PersistenceTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando pruebas de Persistencia (Clase 14 y 15)...");
        
        testGuardarYCargarCircuitoDTO();
        testGuardarYCargarCanvasCompleto();
        testArchivoVacio();
        testCabeceraInvalida();
        testIdentificadorDuplicado();
        testCompuertaDesconocida();
        testReferenciaInexistente();
        testCoordenadaInvalida();
        testArchivoNoExiste();
        
        System.out.println("TODAS LAS PRUEBAS DE PERSISTENCIA PASARON CON ÉXITO.");
    }

    private static void testGuardarYCargarCircuitoDTO() throws IOException, FormatoCircuitoException {
        System.out.println("  [Test 1] Guardar y cargar modelo Circuito...");
        Path tmp = Files.createTempFile("circuito-test-", ".txt");
        try {
            Circuito c = new Circuito("1");
            c.addEntrada("IN-0", true, 100, 150);
            c.addEntrada("IN-1", false, 100, 280);
            c.addCompuerta("AND-0", "AND", 300, 200);
            c.addFoco("FOCO-0", 500, 200);
            c.addConexion("IN-0", "OUT", "AND-0", "IN0");
            c.addConexion("IN-1", "OUT", "AND-0", "IN1");
            c.addConexion("AND-0", "OUT", "FOCO-0", "IN0");

            RepositorioTexto repo = new RepositorioTexto();
            repo.guardar(c, tmp);

            // Verificar contenido del archivo
            String contenido = Files.readString(tmp);
            assert contenido.startsWith("CIRCUITO;1") : "Debe iniciar con CIRCUITO;1";
            assert contenido.contains("ENTRADA;IN-0;true;100;150") : "Debe contener ENTRADA;IN-0";
            assert contenido.contains("COMPUERTA;AND-0;AND;300;200") : "Debe contener COMPUERTA;AND-0";
            assert contenido.contains("FOCO;FOCO-0;500;200") : "Debe contener FOCO;FOCO-0";
            assert contenido.contains("CONEXION;IN-0;OUT;AND-0;IN0") : "Debe contener CONEXION IN-0";

            // Cargar y validar
            Circuito cargado = repo.cargar(tmp);
            assert cargado.getVersion().equals("1") : "Versión debe ser 1";
            assert cargado.getComponentes().size() == 4 : "Debe tener 4 componentes";
            assert cargado.getConexiones().size() == 3 : "Debe tener 3 conexiones";

            System.out.println("    -> OK");
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testGuardarYCargarCanvasCompleto() throws IOException, FormatoCircuitoException {
        System.out.println("  [Test 2] Guardar y cargar Canvas (con Switch, AND, Foco y cables)...");
        Path tmp = Files.createTempFile("canvas-test-", ".txt");
        try {
            Canvas canvas = new Canvas();
            canvas.addGate("INPUT", new Point(100, 120));
            canvas.addGate("INPUT", new Point(100, 250));
            canvas.addGate("AND", new Point(280, 180));
            canvas.addGate("BULB", new Point(460, 180));

            // Encender el primer switch
            Switch sw0 = (Switch) canvas.getComponents()[0];
            sw0.setOn(true);

            // Conectar cables
            Switch sw1 = (Switch) canvas.getComponents()[1];
            LogicGate andGate = (LogicGate) canvas.getComponents()[2];
            Bulb bulb = (Bulb) canvas.getComponents()[3];

            List<Pin> andPins = andGate.getPins();
            Pin andIn0 = andPins.stream().filter(p -> p.getType() == Pin.Type.INPUT && p.getIndex() == 0).findFirst().get();
            Pin andIn1 = andPins.stream().filter(p -> p.getType() == Pin.Type.INPUT && p.getIndex() == 1).findFirst().get();
            Pin andOut = andPins.stream().filter(p -> p.getType() == Pin.Type.OUTPUT).findFirst().get();

            Pin bulbIn = bulb.getPins().stream().filter(p -> p.getType() == Pin.Type.INPUT).findFirst().get();

            canvas.connectWire(sw0.getOutputPin(), andIn0);
            canvas.connectWire(sw1.getOutputPin(), andIn1);
            canvas.connectWire(andOut, bulbIn);

            assert canvas.getWires().size() == 3 : "Debe haber 3 cables";

            // Guardar canvas a archivo
            canvas.saveToFile(tmp);

            // Crear nuevo canvas y restaurar desde archivo
            Canvas canvasRestaurado = new Canvas();
            canvasRestaurado.loadFromFile(tmp);

            assert canvasRestaurado.getComponents().length == 4 : "Debe haber 4 componentes en canvas restaurado";
            assert canvasRestaurado.getWires().size() == 3 : "Debe haber 3 cables en canvas restaurado";

            // Verificar evaluación lógica
            // IN-0 es true, IN-1 es false -> AND es false -> Bulb apagado
            canvasRestaurado.evaluateCircuit();
            Bulb bulbRestaurado = null;
            for (java.awt.Component c : canvasRestaurado.getComponents()) {
                if (c instanceof Bulb b) bulbRestaurado = b;
            }
            assert bulbRestaurado != null : "Bulb restaurado no encontrado";
            assert !bulbRestaurado.isOn() : "Bulb debe estar apagado con true AND false";

            System.out.println("    -> OK");
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testArchivoVacio() throws IOException {
        System.out.println("  [Test 3] Negativo: Archivo vacío...");
        Path tmp = Files.createTempFile("empty-test-", ".txt");
        try {
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(tmp);
            assert false : "Debería lanzar FormatoCircuitoException";
        } catch (FormatoCircuitoException e) {
            System.out.println("    -> Capturado correctamente: " + e.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testCabeceraInvalida() throws IOException {
        System.out.println("  [Test 4] Negativo: Cabecera inválida...");
        Path tmp = Files.createTempFile("header-test-", ".txt");
        try {
            Files.writeString(tmp, "NOT_CIRCUITO;1\nENTRADA;IN-0;true;100;100\n");
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(tmp);
            assert false : "Debería lanzar FormatoCircuitoException";
        } catch (FormatoCircuitoException e) {
            System.out.println("    -> Capturado correctamente: " + e.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testIdentificadorDuplicado() throws IOException {
        System.out.println("  [Test 5] Negativo: Identificador duplicado...");
        Path tmp = Files.createTempFile("dup-test-", ".txt");
        try {
            Files.writeString(tmp, "CIRCUITO;1\nENTRADA;IN-0;true;100;100\nENTRADA;IN-0;false;200;200\n");
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(tmp);
            assert false : "Debería lanzar FormatoCircuitoException";
        } catch (FormatoCircuitoException e) {
            System.out.println("    -> Capturado correctamente: " + e.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testCompuertaDesconocida() throws IOException {
        System.out.println("  [Test 6] Negativo: Tipo de compuerta desconocido...");
        Path tmp = Files.createTempFile("gate-test-", ".txt");
        try {
            Files.writeString(tmp, "CIRCUITO;1\nCOMPUERTA;G1;UNKNOWN_GATE;100;100\n");
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(tmp);
            assert false : "Debería lanzar FormatoCircuitoException";
        } catch (FormatoCircuitoException e) {
            System.out.println("    -> Capturado correctamente: " + e.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testReferenciaInexistente() throws IOException {
        System.out.println("  [Test 7] Negativo: Referencia de conexión inexistente...");
        Path tmp = Files.createTempFile("ref-test-", ".txt");
        try {
            Files.writeString(tmp, "CIRCUITO;1\nENTRADA;IN-0;true;100;100\nCONEXION;IN-0;OUT;G_NO_EXISTE;IN0\n");
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(tmp);
            assert false : "Debería lanzar FormatoCircuitoException";
        } catch (FormatoCircuitoException e) {
            System.out.println("    -> Capturado correctamente: " + e.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testCoordenadaInvalida() throws IOException {
        System.out.println("  [Test 8] Negativo: Coordenadas no numéricas...");
        Path tmp = Files.createTempFile("coord-test-", ".txt");
        try {
            Files.writeString(tmp, "CIRCUITO;1\nENTRADA;IN-0;true;abc;200\n");
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(tmp);
            assert false : "Debería lanzar FormatoCircuitoException";
        } catch (FormatoCircuitoException e) {
            System.out.println("    -> Capturado correctamente: " + e.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private static void testArchivoNoExiste() throws FormatoCircuitoException {
        System.out.println("  [Test 9] Negativo: Archivo no existe (IOException)...");
        Path noExiste = Path.of("ruta_que_no_existe_12345.txt");
        try {
            RepositorioTexto repo = new RepositorioTexto();
            repo.cargar(noExiste);
            assert false : "Debería lanzar NoSuchFileException/IOException";
        } catch (NoSuchFileException e) {
            System.out.println("    -> Capturado NoSuchFileException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("    -> Capturado IOException: " + e.getMessage());
        }
    }
}
