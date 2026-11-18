package com.engine.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representación en memoria de los datos persistibles de un circuito lógico.
 * Almacena las declaraciones de componentes (entradas, compuertas, focos) con sus
 * posiciones gráficas y las conexiones entre sus pines.
 *
 * Implementa Serializable según Clase 15 de POO.
 */
public class Circuito implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum TipoComponente {
        ENTRADA,
        COMPUERTA,
        FOCO
    }

    public static class ComponenteDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private final TipoComponente tipo;
        private final String id;
        private final String tipoCompuerta; // AND, OR, NOT, NAND, NOR, XOR, XNOR (para COMPUERTA)
        private final boolean valorInicial; // para ENTRADA
        private final int x;
        private final int y;

        public ComponenteDTO(TipoComponente tipo, String id, String tipoCompuerta, boolean valorInicial, int x, int y) {
            this.tipo = tipo;
            this.id = id;
            this.tipoCompuerta = tipoCompuerta;
            this.valorInicial = valorInicial;
            this.x = x;
            this.y = y;
        }

        public TipoComponente getTipo() {
            return tipo;
        }

        public String getId() {
            return id;
        }

        public String getTipoCompuerta() {
            return tipoCompuerta;
        }

        public boolean getValorInicial() {
            return valorInicial;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static class ConexionDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String fromCompId;
        private final String fromPinName;
        private final String toCompId;
        private final String toPinName;

        public ConexionDTO(String fromCompId, String fromPinName, String toCompId, String toPinName) {
            this.fromCompId = fromCompId;
            this.fromPinName = fromPinName;
            this.toCompId = toCompId;
            this.toPinName = toPinName;
        }

        public String getFromCompId() {
            return fromCompId;
        }

        public String getFromPinName() {
            return fromPinName;
        }

        public String getToCompId() {
            return toCompId;
        }

        public String getToPinName() {
            return toPinName;
        }
    }

    private String version = "1";
    private final List<ComponenteDTO> componentes = new ArrayList<>();
    private final List<ConexionDTO> conexiones = new ArrayList<>();

    public Circuito() {
    }

    public Circuito(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addEntrada(String id, boolean valorInicial, int x, int y) {
        componentes.add(new ComponenteDTO(TipoComponente.ENTRADA, id, null, valorInicial, x, y));
    }

    public void addCompuerta(String id, String tipoCompuerta, int x, int y) {
        componentes.add(new ComponenteDTO(TipoComponente.COMPUERTA, id, tipoCompuerta, false, x, y));
    }

    public void addFoco(String id, int x, int y) {
        componentes.add(new ComponenteDTO(TipoComponente.FOCO, id, null, false, x, y));
    }

    public void addConexion(String fromCompId, String fromPinName, String toCompId, String toPinName) {
        conexiones.add(new ConexionDTO(fromCompId, fromPinName, toCompId, toPinName));
    }

    public List<ComponenteDTO> getComponentes() {
        return Collections.unmodifiableList(componentes);
    }

    public List<ConexionDTO> getConexiones() {
        return Collections.unmodifiableList(conexiones);
    }
}
