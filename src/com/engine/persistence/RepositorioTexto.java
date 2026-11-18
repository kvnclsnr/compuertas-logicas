package com.engine.persistence;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;

import com.ui.circuit.pin.Pin;
import com.ui.circuit.wire.Wire;
import com.ui.events.drag.GateDragHandler;
import com.ui.icons.Bulb;
import com.ui.icons.GateIconFactory;
import com.ui.icons.LogicGate;
import com.ui.icons.Switch;
import com.ui.layouts.canvas.Canvas;

/**
 * Implementación de persistencia en archivos de texto según las clases 14 y 15 de POO.
 * Cumple con el formato especificado:
 * CIRCUITO;1
 * ENTRADA;<id>;<boolean>;<x>;<y>
 * COMPUERTA;<id>;<TYPE>;<x>;<y>
 * FOCO;<id>;<x>;<y>
 * CONEXION;<fromCompId>;<fromPinName>;<toCompId>;<toPinName>
 *
 * Utiliza Path, Files.newBufferedReader, Files.newBufferedWriter, StandardCharsets.UTF_8
 * y bloques try-with-resources.
 */
public class RepositorioTexto implements RepositorioCircuitos {
    private static final int ICON_SIZE = 96;
    private static final Set<String> TIPOS_COMPUERTAS_VALIDAS = Set.of(
        "AND", "NAND", "OR", "NOR", "NOT", "XOR", "XNOR"
    );

    @Override
    public void guardar(Circuito c, Path ruta) throws IOException {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser nula");
        }
        if (c == null) {
            throw new IllegalArgumentException("El circuito no puede ser nulo");
        }

        if (ruta.getParent() != null) {
            Files.createDirectories(ruta.getParent());
        }

        try (BufferedWriter bw = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            // Cabecera obligatoria (Diapositivas 51, 56 de Clase 14)
            String version = (c.getVersion() != null && !c.getVersion().isEmpty()) ? c.getVersion() : "1";
            bw.write("CIRCUITO;" + version);
            bw.newLine();

            // Guardar componentes
            for (Circuito.ComponenteDTO comp : c.getComponentes()) {
                switch (comp.getTipo()) {
                    case ENTRADA -> {
                        bw.write("ENTRADA;" + comp.getId() + ";" + comp.getValorInicial() + ";" + comp.getX() + ";" + comp.getY());
                        bw.newLine();
                    }
                    case COMPUERTA -> {
                        bw.write("COMPUERTA;" + comp.getId() + ";" + comp.getTipoCompuerta() + ";" + comp.getX() + ";" + comp.getY());
                        bw.newLine();
                    }
                    case FOCO -> {
                        bw.write("FOCO;" + comp.getId() + ";" + comp.getX() + ";" + comp.getY());
                        bw.newLine();
                    }
                }
            }

            // Guardar conexiones
            for (Circuito.ConexionDTO con : c.getConexiones()) {
                bw.write("CONEXION;" + con.getFromCompId() + ";" + con.getFromPinName() + ";" + con.getToCompId() + ";" + con.getToPinName());
                bw.newLine();
            }
        }
    }

    @Override
    public Circuito cargar(Path ruta) throws IOException, FormatoCircuitoException {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser nula");
        }
        if (!Files.exists(ruta)) {
            throw new java.nio.file.NoSuchFileException(ruta.toString());
        }

        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        }

        if (lineas.isEmpty()) {
            throw new FormatoCircuitoException(1, "El archivo está vacío");
        }

        // Buscar primera línea con contenido
        int indexLinea = 0;
        while (indexLinea < lineas.size() && lineas.get(indexLinea).trim().isEmpty()) {
            indexLinea++;
        }

        if (indexLinea >= lineas.size()) {
            throw new FormatoCircuitoException(1, "El archivo está vacío");
        }

        String cabecera = lineas.get(indexLinea).trim();
        if (!cabecera.startsWith("CIRCUITO;")) {
            throw new FormatoCircuitoException(indexLinea + 1, "Cabecera o versión inválida (debe iniciar con CIRCUITO;1)");
        }

        String[] cabeceraPartes = cabecera.split(";", -1);
        if (cabeceraPartes.length < 2 || !cabeceraPartes[1].equals("1")) {
            throw new FormatoCircuitoException(indexLinea + 1, "Versión de circuito no soportada: " + cabecera);
        }

        Circuito circuito = new Circuito(cabeceraPartes[1]);
        Set<String> idsDeclarados = new HashSet<>();

        // Clase 14 Slide 58: Dos pasadas para no depender del orden entre componentes y conexiones.
        // PASADA 1: Registro y validación de componentes
        for (int i = indexLinea + 1; i < lineas.size(); i++) {
            int numLinea = i + 1;
            String l = lineas.get(i).trim();
            if (l.isEmpty() || l.startsWith("#")) {
                continue;
            }

            String[] partes = l.split(";", -1);
            String comando = partes[0].trim().toUpperCase();

            switch (comando) {
                case "ENTRADA" -> {
                    // ENTRADA;<id>;<boolean>;<x>;<y> o ENTRADA;<id>;<boolean>
                    if (partes.length < 3) {
                        throw new FormatoCircuitoException(numLinea, "Formato ENTRADA incompleto. Esperado: ENTRADA;<id>;<boolean>;<x>;<y>");
                    }
                    String id = partes[1].trim();
                    if (id.isEmpty()) {
                        throw new FormatoCircuitoException(numLinea, "Identificador de entrada vacío");
                    }
                    if (idsDeclarados.contains(id)) {
                        throw new FormatoCircuitoException(numLinea, "Identificador duplicado: " + id);
                    }
                    idsDeclarados.add(id);

                    boolean val = Boolean.parseBoolean(partes[2].trim());
                    int x = 50;
                    int y = 50;
                    if (partes.length >= 5) {
                        try {
                            x = Integer.parseInt(partes[3].trim());
                            y = Integer.parseInt(partes[4].trim());
                        } catch (NumberFormatException e) {
                            throw new FormatoCircuitoException(numLinea, "Coordenadas no numéricas en ENTRADA", e);
                        }
                    }
                    circuito.addEntrada(id, val, x, y);
                }
                case "COMPUERTA" -> {
                    // COMPUERTA;<id>;<TYPE>;<x>;<y>
                    if (partes.length < 5) {
                        throw new FormatoCircuitoException(numLinea, "Formato COMPUERTA incompleto. Esperado: COMPUERTA;<id>;<TYPE>;<x>;<y>");
                    }
                    String id = partes[1].trim();
                    if (id.isEmpty()) {
                        throw new FormatoCircuitoException(numLinea, "Identificador de compuerta vacío");
                    }
                    if (idsDeclarados.contains(id)) {
                        throw new FormatoCircuitoException(numLinea, "Identificador duplicado: " + id);
                    }
                    idsDeclarados.add(id);

                    String tipo = partes[2].trim().toUpperCase();
                    if (!TIPOS_COMPUERTAS_VALIDAS.contains(tipo)) {
                        throw new FormatoCircuitoException(numLinea, "Tipo de compuerta desconocido: " + tipo);
                    }

                    int x;
                    int y;
                    try {
                        x = Integer.parseInt(partes[3].trim());
                        y = Integer.parseInt(partes[4].trim());
                    } catch (NumberFormatException e) {
                        throw new FormatoCircuitoException(numLinea, "Coordenadas no numéricas en COMPUERTA", e);
                    }
                    circuito.addCompuerta(id, tipo, x, y);
                }
                case "FOCO" -> {
                    // FOCO;<id>;<x>;<y>
                    if (partes.length < 4) {
                        throw new FormatoCircuitoException(numLinea, "Formato FOCO incompleto. Esperado: FOCO;<id>;<x>;<y>");
                    }
                    String id = partes[1].trim();
                    if (id.isEmpty()) {
                        throw new FormatoCircuitoException(numLinea, "Identificador de foco vacío");
                    }
                    if (idsDeclarados.contains(id)) {
                        throw new FormatoCircuitoException(numLinea, "Identificador duplicado: " + id);
                    }
                    idsDeclarados.add(id);

                    int x;
                    int y;
                    try {
                        x = Integer.parseInt(partes[2].trim());
                        y = Integer.parseInt(partes[3].trim());
                    } catch (NumberFormatException e) {
                        throw new FormatoCircuitoException(numLinea, "Coordenadas no numéricas en FOCO", e);
                    }
                    circuito.addFoco(id, x, y);
                }
                case "CONEXION" -> {
                    // Se procesará en la segunda pasada
                }
                default -> {
                    throw new FormatoCircuitoException(numLinea, "Comando desconocido en archivo de circuito: " + comando);
                }
            }
        }

        // PASADA 2: Validación y registro de conexiones
        for (int i = indexLinea + 1; i < lineas.size(); i++) {
            int numLinea = i + 1;
            String l = lineas.get(i).trim();
            if (l.isEmpty() || l.startsWith("#")) {
                continue;
            }

            String[] partes = l.split(";", -1);
            if (partes[0].trim().equalsIgnoreCase("CONEXION")) {
                if (partes.length < 5) {
                    throw new FormatoCircuitoException(numLinea, "Formato CONEXION incompleto. Esperado: CONEXION;<fromCompId>;<fromPinName>;<toCompId>;<toPinName>");
                }
                String fromCompId = partes[1].trim();
                String fromPinName = partes[2].trim();
                String toCompId = partes[3].trim();
                String toPinName = partes[4].trim();

                if (!idsDeclarados.contains(fromCompId)) {
                    throw new FormatoCircuitoException(numLinea, "Referencia de origen inexistente: " + fromCompId);
                }
                if (!idsDeclarados.contains(toCompId)) {
                    throw new FormatoCircuitoException(numLinea, "Referencia de destino inexistente: " + toCompId);
                }

                circuito.addConexion(fromCompId, fromPinName, toCompId, toPinName);
            }
        }

        return circuito;
    }

    /**
     * Serializa el estado actual de los componentes y cables del Canvas a un modelo Circuito.
     */
    public Circuito desdeCanvas(Canvas canvas) {
        Circuito circuito = new Circuito("1");
        if (canvas == null) {
            return circuito;
        }

        Map<Component, String> compToId = new HashMap<>();
        int compIndex = 0;

        for (Component comp : canvas.getComponents()) {
            if (!(comp instanceof JComponent jc)) {
                continue;
            }

            String id = comp.getName();

            if (comp instanceof Switch sw) {
                if (id == null || id.isEmpty()) {
                    id = sw.getLogicInput().getName();
                }
                comp.setName(id);
                compToId.put(comp, id);
                circuito.addEntrada(id, sw.isOn(), comp.getX(), comp.getY());
            } else if (comp instanceof Bulb bulb) {
                if (id == null || id.isEmpty()) {
                    id = bulb.getEngineGate() != null ? bulb.getEngineGate().getName() : "FOCO-" + (compIndex++);
                }
                comp.setName(id);
                compToId.put(comp, id);
                circuito.addFoco(id, comp.getX(), comp.getY());
            } else if (comp instanceof LogicGate lg) {
                if (id == null || id.isEmpty()) {
                    id = lg.getEngineGate() != null ? lg.getEngineGate().getName() : lg.getClass().getSimpleName().toUpperCase() + "-" + (compIndex++);
                }
                comp.setName(id);
                compToId.put(comp, id);
                String tipo = lg.getClass().getSimpleName().toUpperCase();
                circuito.addCompuerta(id, tipo, comp.getX(), comp.getY());
            }
        }

        for (Wire wire : canvas.getWires()) {
            Pin src = wire.getSource();
            Pin tgt = wire.getTarget();

            String fromId = compToId.get(src.getOwner());
            String toId = compToId.get(tgt.getOwner());

            if (fromId != null && toId != null) {
                String fromPinName = "OUT";
                String toPinName = "IN" + tgt.getIndex();
                circuito.addConexion(fromId, fromPinName, toId, toPinName);
            }
        }

        return circuito;
    }

    /**
     * Reconstruye en el Canvas los componentes y conexiones definidos en el Circuito.
     */
    public void aplicarEnCanvas(Circuito circuito, Canvas canvas) {
        if (canvas == null || circuito == null) {
            return;
        }

        // Limpia el lienzo por completo
        canvas.clearCanvas();

        Map<String, JComponent> idToComp = new HashMap<>();

        // Paso 1: Instanciar y ubicar componentes
        for (Circuito.ComponenteDTO compDto : circuito.getComponentes()) {
            JComponent icon = null;
            switch (compDto.getTipo()) {
                case ENTRADA -> {
                    Switch sw = new Switch(ICON_SIZE);
                    sw.setSwitchId(compDto.getId());
                    sw.setOn(compDto.getValorInicial());
                    icon = sw;
                }
                case COMPUERTA -> {
                    icon = GateIconFactory.create(compDto.getTipoCompuerta(), ICON_SIZE);
                    if (icon instanceof LogicGate lg) {
                        lg.setGateId(compDto.getId());
                    }
                }
                case FOCO -> {
                    icon = GateIconFactory.create("BULB", ICON_SIZE);
                    if (icon == null) {
                        icon = GateIconFactory.create("FOCO", ICON_SIZE);
                    }
                    if (icon instanceof LogicGate lg) {
                        lg.setGateId(compDto.getId());
                    } else if (icon != null) {
                        icon.setName(compDto.getId());
                        icon.setToolTipText(compDto.getId());
                    }
                }
            }

            if (icon != null) {
                icon.setBounds(compDto.getX(), compDto.getY(), ICON_SIZE, ICON_SIZE);
                new GateDragHandler(icon);

                if (icon instanceof LogicGate lg && lg.getEngineGate() != null && !(icon instanceof Bulb)) {
                    canvas.getCircuitManager().addGate(lg.getEngineGate());
                }

                canvas.add(icon);
                idToComp.put(compDto.getId(), icon);
            }
        }

        // Paso 2: Conectar cables
        for (Circuito.ConexionDTO conDto : circuito.getConexiones()) {
            JComponent fromComp = idToComp.get(conDto.getFromCompId());
            JComponent toComp = idToComp.get(conDto.getToCompId());

            if (fromComp == null || toComp == null) {
                continue;
            }

            Pin outPin = null;
            if (fromComp instanceof Switch sw) {
                outPin = sw.getOutputPin();
            } else if (fromComp instanceof LogicGate lg) {
                for (Pin p : lg.getPins()) {
                    if (p.getType() == Pin.Type.OUTPUT) {
                        outPin = p;
                        break;
                    }
                }
            }

            Pin inPin = null;
            int targetIndex = parsePinIndex(conDto.getToPinName());
            if (toComp instanceof LogicGate lg) {
                for (Pin p : lg.getPins()) {
                    if (p.getType() == Pin.Type.INPUT && p.getIndex() == targetIndex) {
                        inPin = p;
                        break;
                    }
                }
                if (inPin == null) {
                    // Fallback a cualquier pin de entrada disponible
                    for (Pin p : lg.getPins()) {
                        if (p.getType() == Pin.Type.INPUT) {
                            inPin = p;
                            break;
                        }
                    }
                }
            }

            if (outPin != null && inPin != null) {
                canvas.connectWire(outPin, inPin);
            }
        }

        canvas.getCircuitManager().reorganize();
        canvas.evaluateCircuit();
        canvas.notifyCircuitChange();
        canvas.revalidate();
        canvas.repaint();
    }

    private int parsePinIndex(String pinName) {
        if (pinName == null || pinName.isEmpty()) {
            return 0;
        }
        String s = pinName.trim().toUpperCase();
        if (s.endsWith("1") || s.equals("B") || s.equals("IN1") || s.equals("INPUT1")) {
            return 1;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    /**
     * Guarda el circuito actual de Canvas a un archivo de texto.
     */
    public void guardarCanvas(Canvas canvas, Path ruta) throws IOException {
        Circuito c = desdeCanvas(canvas);
        guardar(c, ruta);
    }

    /**
     * Carga un archivo de texto y lo restaura en el Canvas.
     */
    public void cargarEnCanvas(Canvas canvas, Path ruta) throws IOException, FormatoCircuitoException {
        Circuito c = cargar(ruta);
        aplicarEnCanvas(c, canvas);
    }
}
