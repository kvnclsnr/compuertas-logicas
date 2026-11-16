# Simulador de compuertas lógicas

Aplicación de escritorio en Java para construir circuitos combinacionales, probarlos visualmente y consultar sus tablas de verdad. La interfaz está desarrollada con Swing y la lógica del circuito está separada en un motor que también puede utilizarse desde consola.

## Funcionalidades

### Editor de circuitos

- La paleta ofrece AND, OR, NOT, NAND, NOR, XOR, XNOR e `INPUT` (un interruptor).
- Arrastra un elemento de la paleta al lienzo para crear una instancia. Cada instancia tiene un identificador generado, como `AND-0` o `IN-0`; aparece como tooltip al pasar el cursor sobre ella.
- Arrastra los componentes dentro del lienzo para moverlos. Si los sueltas fuera de sus límites, vuelven a quedar dentro.
- Para eliminar un componente, arrástralo desde el lienzo y suéltalo sobre la paleta. Al eliminarlo también se quitan sus cables.
- Haz clic izquierdo en un pin y arrastra hasta otro pin para crear una conexión. Solo se conectan salidas con entradas; una entrada admite un solo cable, por lo que una conexión nueva reemplaza la existente.
- Para mover una conexión ya hecha, inicia el arrastre desde su pin de entrada: el cable se desprende y puedes conectarlo a otra entrada.
- Haz clic derecho en un pin para desconectarlo. En una salida se eliminan sus cables; en una entrada, solo el cable que llega a ella.
- Haz clic en un interruptor para alternar su estado. Las compuertas y los cables reflejan visualmente si la señal está activa.

### Tablas de verdad

- El panel lateral presenta una tabla por cada grupo de compuertas conectadas; al conectar o desconectar compuertas, los grupos se recalculan.
- Las columnas incluyen las entradas del circuito y las expresiones de las compuertas. Los valores se muestran como `0` y `1`.
- Las entradas de compuerta que no estén conectadas se tratan como variables independientes al generar la tabla. Sus nombres se forman con el ID de la compuerta y el pin, por ejemplo `AND-0.A`.
- La tabla considera todas las combinaciones de las entradas y restaura sus valores originales al terminar el cálculo. El motor limita la generación a 10 entradas por tabla.
- Cambiar el estado actual de un interruptor actualiza la evaluación visual; la tabla de verdad sigue describiendo todas las combinaciones posibles.
- Las tablas pueden desplazarse horizontalmente cuando no caben en el panel y el panel lateral permite desplazamiento vertical.

### Barra superior

La interfaz muestra menús para Archivo, Editar, Ver y Ayuda, además de un botón Exportar con opciones JSON y Tabla de verdad. Por ahora, estos elementos presentan sus opciones, pero no tienen acciones funcionales asociadas.

## Arquitectura

El código se encuentra en `src/com`. `engine` implementa la lógica independiente de Swing y `ui` contiene la interfaz gráfica.

### Motor lógico (`com.engine`)

- `LogicSource` define la interfaz común para entradas y compuertas: valor, expresión y recorrido de dependencias.
- `LogicInput` representa una entrada con nombre y valor booleano.
- `Gate` ofrece la base para las compuertas, con nombre, cálculo de expresión y recorrido de entradas.
- `BinaryGate` reúne el manejo de las dos entradas de AND, OR, NAND, NOR, XOR y XNOR. `NotGate` gestiona una entrada.
- `GateFactory` crea compuertas del motor a partir de un tipo.
- `Circuit` calcula las tablas y expresiones de sus compuertas; `CircuitManager` mantiene los grupos de compuertas conectadas y los reorganiza cuando cambia el circuito.
- `TruthTablePrinter` presenta tablas en consola. `GateTest` crea un circuito de demostración para usar con esa impresora.

Para una tabla, `Circuit` recorre las dependencias, reúne las entradas únicas, evalúa cada combinación y produce una matriz: la primera fila contiene los encabezados y las siguientes contienen los valores. Los pines sin conectar reciben entradas temporales para ese cálculo y luego se restauran las conexiones originales.

### Interfaz (`com.ui`)

- `App` crea la ventana; `TopBar` y `Content` organizan su barra superior y el área principal.
- `Workspace` dispone la paleta, el lienzo y el panel lateral. `WorkspaceSplit` y `DividerStartPositionHandler` controlan el divisor redimensionable.
- `GatePalette` crea la lista de componentes. `GateItem` y los manejadores de `events/drag` se ocupan del arrastre y de añadir componentes al lienzo.
- `Canvas` mantiene los componentes, los cables y el estado visual; conecta los pines con sus fuentes lógicas y notifica cambios a la vista de tablas.
- `LogicGate` y sus subclases dibujan las compuertas. `Switch` implementa el interruptor de entrada y `GateIconFactory` crea los iconos de la paleta.
- `Pin`, `Wire`, `WireCurve` y `PendingWire` representan los pines, las conexiones y el cable provisional durante el arrastre.
- `TruthTableView` escucha los cambios del lienzo. `CircuitList`, `CircuitSection`, `CircuitTable` y sus renderizadores presentan las tablas.
- `Palette` centraliza los colores y `Styles` agrupa utilidades de formato Swing.

```text
src/com/
├── engine/
│   ├── LogicInput.java
│   ├── LogicSource.java
│   ├── circuit/
│   │   ├── Circuit.java
│   │   ├── CircuitManager.java
│   │   └── TruthTablePrinter.java
│   └── gates/
│       ├── Gate.java
│       ├── BinaryGate.java
│       ├── GateFactory.java
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
    ├── circuit/
    │   ├── pin/Pin.java
    │   └── wire/
    │       ├── PendingWire.java
    │       ├── Wire.java
    │       ├── WireCurve.java
    │       └── WireList.java
    ├── components/
    │   ├── common/
    │   ├── gatePalette/
    │   ├── menu/
    │   └── truthTable/
    ├── events/
    │   ├── circuit/
    │   ├── drag/
    │   └── resize/
    ├── icons/
    ├── layouts/
    │   ├── canvas/
    │   ├── main/
    │   ├── sidePanel/
    │   └── workspace/
    └── styles/
```

## Requisitos

- JDK 26 o compatible con el nivel de lenguaje configurado en `nbproject/project.properties`.
- Apache Ant para compilar desde la terminal.
- NetBeans es opcional; el proyecto incluye configuración de Ant generada para ese IDE.

## Compilar y ejecutar

Desde la raíz del proyecto:

```bash
ant compile
java -cp build/classes com.ui.App
```

La clase principal también está configurada como `com.ui.App` en el proyecto de NetBeans.

## Ejecutar la demostración del motor

Después de compilar, ejecuta:

```bash
java -cp build/classes com.engine.gates.GateTest
```

La demostración imprime en consola las tablas de verdad de un circuito con entradas, AND, NAND y OR. `GateTest` es un ejemplo ejecutable, no una suite de pruebas automatizadas.

## Estructura auxiliar

- `build.xml` y `nbproject/` contienen la configuración del proyecto y las tareas de compilación de Ant/NetBeans.
- `manifest.mf` contiene el manifiesto de la aplicación.
- `README.md` documenta el uso y la organización del proyecto.

## Autores

- Kevin Celis
- Shaday Arevalo
- Juan Ocampo
