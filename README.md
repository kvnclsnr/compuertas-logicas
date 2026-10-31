# Simulador de compuertas lógicas

Proyecto en Java para modelar compuertas lógicas, conectarlas en circuitos y generar tablas de verdad. El motor lógico funciona en consola y el desarrollo de la interfaz gráfica con Java Swing está en progreso.

Actualmente, la ventana Swing presenta una galería visual de las compuertas disponibles. La creación y simulación interactiva de circuitos desde la interfaz todavía está pendiente.

## Autores

- Kevin Celis
- Shaday Arevalo
- Juan Ocampo

## Funcionalidades

### Motor lógico

- Representa entradas con nombre y valor booleano.
- Modela las operaciones AND, NAND, NOR, NOT, OR, XNOR y XOR.
- Permite conectar entradas y compuertas para formar circuitos compuestos.
- Evalúa compuertas según los valores actuales de sus entradas.
- Genera tablas de verdad para las compuertas de un circuito o para una compuerta específica.
- Detecta las entradas utilizadas y ordena las compuertas según sus dependencias al generar una tabla.
- Muestra las expresiones lógicas correspondientes a las compuertas.
- Permite consultar, evaluar y eliminar compuertas de un circuito mediante la API de `Circuit`.

### Interfaz gráfica en progreso

- Muestra las siete compuertas en una ventana Swing, cada una identificada con su nombre.
- Dibuja los iconos con `Graphics2D` y `Path2D`, con trazos suavizados.
- Permite definir el tamaño de los iconos mediante el constructor de cada componente.
- Por ahora, la interfaz es una galería estática: aún no permite armar circuitos ni mostrar sus resultados.

## Organización del proyecto

```text
src/
└── com/
    ├── engine/
    │   ├── LogicInput.java
    │   ├── LogicSource.java
    │   ├── banks/
    │   │   ├── GateBank.java          # Obsoleto
    │   │   └── GateBankTest.java      # Vacío y obsoleto
    │   ├── circuit/
    │   │   └── Circuit.java
    │   └── gates/
    │       ├── Gate.java
    │       ├── AndGate.java
    │       ├── NandGate.java
    │       ├── NorGate.java
    │       ├── NotGate.java
    │       ├── OrGate.java
    │       ├── XnorGate.java
    │       ├── XorGate.java
    │       └── GateTest.java
    └── ui/
        ├── App.java
        └── gates/
            ├── LogicGateIcon.java
            ├── AndGate.java
            ├── NandGate.java
            ├── NorGate.java
            ├── NotGate.java
            ├── OrGate.java
            ├── XnorGate.java
            └── XorGate.java
```

`LogicSource` define la interfaz común de las entradas y las compuertas. `Gate` proporciona la base para evaluar compuertas y construir sus expresiones. `Circuit` administra las compuertas conectadas y genera las tablas de verdad. `GateBank` está obsoleto y no forma parte del flujo principal.

La interfaz Swing se encuentra en `com.ui`. `LogicGateIcon` comparte la lógica de dibujo y escalado; cada clase de compuerta define su propio trazado.

## Requisitos

- JDK 26, de acuerdo con la configuración del proyecto.
- Apache Ant para compilar desde la terminal.
- NetBeans es opcional; el proyecto incluye configuración de Ant para abrirlo desde ese IDE.

## Compilar

Desde la carpeta raíz del proyecto:

```bash
ant compile
```

Las clases compiladas se guardan en `build/classes`.

## Ejecutar la interfaz gráfica

Después de compilar, ejecuta:

```bash
java -cp build/classes com.ui.App
```

En NetBeans, la clase principal configurada para ejecutar el proyecto es `com.ui.App`.

## Ejecutar la demostración del motor

La clase `GateTest` construye un circuito de ejemplo con entradas, compuertas AND, NAND y OR, e imprime tablas de verdad en la consola. Para ejecutarla:

```bash
java -cp build/classes com.engine.gates.GateTest
```

## Tablas de verdad

- `printTruthTable()` muestra las tablas de las compuertas agregadas al circuito.
- `printTruthTable(gate)` muestra la tabla de la compuerta indicada, aunque no esté agregada individualmente al circuito.

Las entradas se obtienen recorriendo la compuerta solicitada y sus dependencias. El circuito restaura los valores originales de las entradas después de generar la tabla.

## Estado del proyecto

El motor permite evaluar circuitos compuestos y generar tablas de verdad en consola. La interfaz gráfica está en desarrollo y actualmente muestra los iconos de las siete compuertas; la edición visual y simulación de circuitos desde la ventana están pendientes. No hay un conjunto de pruebas automatizadas configurado.
