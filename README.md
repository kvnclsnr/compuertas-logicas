# Simulador de compuertas lógicas

Aplicación de escritorio desarrollada en Java para construir circuitos con compuertas lógicas, probar entradas y consultar las tablas de verdad resultantes. Incluye un motor lógico independiente de la interfaz y una aplicación gráfica construida con Swing.

## Funcionalidades

### Editor gráfico

- Presenta una paleta con las compuertas AND, OR, NOT, NAND, NOR, XOR y XNOR, además de entradas tipo interruptor.
- Permite arrastrar elementos de la paleta al lienzo y moverlos para organizar el circuito.
- Permite conectar salidas con entradas mediante cables. Al conectar una entrada que ya tiene un cable, la conexión anterior se sustituye.
- Permite desconectar cables y eliminar elementos del lienzo.
- Los interruptores cambian entre encendido y apagado al interactuar con ellos. El motor evalúa el circuito y actualiza la apariencia de las compuertas según su salida.
- Muestra las tablas de verdad de los circuitos presentes en el lienzo. El panel se actualiza al cambiar el circuito y ajusta las tablas al cambiar su tamaño.
- Incluye una barra superior con opciones de menú y exportación.

### Motor lógico

- `LogicInput` representa una entrada lógica identificada por un nombre y un valor booleano.
- `LogicSource` define el comportamiento compartido por las entradas y las compuertas, como obtener el valor, la expresión lógica y recorrer dependencias.
- `Gate` proporciona la base para las compuertas: expresión lógica, valor de salida y recorrido de entradas.
- `BinaryGate` reúne el manejo común de las compuertas con dos entradas.
- Las clases AND, OR, NOT, NAND, NOR, XOR y XNOR implementan sus respectivas operaciones.
- `GateFactory` crea compuertas según el tipo solicitado.
- `Circuit` administra las compuertas de un grupo conectado, evalúa sus salidas y genera tablas de verdad para el circuito o para una compuerta específica.

## Estructura del proyecto

El código fuente se encuentra bajo `src/com`. El paquete `engine` contiene la lógica independiente de Swing y el paquete `ui` organiza la interfaz por componentes y responsabilidades.

```text
src/com/
├── engine/
│   ├── LogicInput.java              # Entrada lógica con nombre y valor
│   ├── LogicSource.java             # Contrato común de entradas y compuertas
│   ├── circuit/
│   │   └── Circuit.java             # Administración y tablas de verdad
│   └── gates/
│       ├── Gate.java                # Base para las compuertas
│       ├── BinaryGate.java          # Base para compuertas de dos entradas
│       ├── GateFactory.java         # Creación de compuertas por tipo
│       ├── AndGate.java
│       ├── NandGate.java
│       ├── NorGate.java
│       ├── NotGate.java
│       ├── OrGate.java
│       ├── XnorGate.java
│       ├── XorGate.java
│       └── GateTest.java            # Demostración del motor en consola
└── ui/
    ├── App.java                     # Inicio de la aplicación Swing
    ├── circuit/
    │   ├── pin/Pin.java             # Pines de entrada y salida
    │   └── wire/
    │       ├── Wire.java            # Conexión entre dos pines
    │       ├── WireCurve.java       # Geometría y dibujo de un cable
    │       ├── WireList.java        # Colección y búsqueda de cables
    │       └── PendingWire.java     # Cable provisional durante el arrastre
    ├── components/
    │   ├── common/                  # Paneles y divisores reutilizables
    │   ├── gatePalette/             # Elementos arrastrables de la paleta
    │   ├── menu/                    # Botones, menús y opciones emergentes
    │   └── truthTable/              # Secciones, tablas y renderizadores
    ├── events/
    │   ├── drag/                    # Arrastre y colocación de elementos
    │   └── resize/                  # Ajustes al redimensionar paneles
    ├── icons/                       # Dibujo de compuertas e interruptores
    ├── layouts/
    │   ├── canvas/Canvas.java       # Lienzo y estado gráfico del circuito
    │   ├── main/                    # Contenido principal y barra superior
    │   ├── truthTable/              # Panel que presenta las tablas
    │   └── workspace/               # Paleta y distribución del espacio
    └── styles/                      # Colores y utilidades visuales
```

### Cómo se organiza la interfaz

`App` crea la ventana y combina la barra superior con el contenido principal. `Content` prepara el área central y `Workspace` organiza la paleta junto al área de trabajo.

`GatePalette` construye la lista de elementos disponibles. `GateItem` y los manejadores de `events/drag` hacen posible arrastrarlos al lienzo. `Canvas` mantiene los elementos y los cables, gestiona las conexiones entre pines y crea una instancia de `Circuit` por cada grupo conectado de compuertas. Cuando se agrega o quita una conexión o compuerta, reconstruye esos grupos y usa `Circuit.evaluate()` para evaluar cada circuito. También notifica los cambios a la tabla de verdad. Los tipos `Pin` y `Wire` representan las conexiones visuales y lógicas.

`LogicGate` comparte el tamaño, el dibujo y la gestión de pines de las compuertas gráficas; cada clase concreta dibuja su símbolo. `Switch` proporciona una entrada que se puede activar o desactivar. `GateIconFactory` centraliza la creación de los iconos correspondientes a los nombres de la paleta.

`TruthTable` observa los cambios del lienzo y presenta las tablas suministradas por el motor. Los componentes de `components/truthTable` construyen las secciones y celdas, mientras que los renderizadores controlan la presentación de encabezados y salidas.

## Requisitos

- JDK 26, según la configuración del proyecto.
- Apache Ant para compilar desde la terminal.
- NetBeans es opcional; el proyecto incluye configuración de Ant compatible con ese IDE.

## Compilar

Desde la carpeta raíz del proyecto:

```bash
ant compile
```

Las clases compiladas se guardan en `build/classes`.

## Ejecutar la aplicación

Después de compilar, inicia la interfaz con:

```bash
java -cp build/classes com.ui.App
```

En NetBeans, la clase principal es `com.ui.App`.

## Ejecutar la demostración del motor

`GateTest` construye un circuito de ejemplo con entradas, compuertas AND, NAND y OR, e imprime tablas de verdad en la consola:

```bash
java -cp build/classes com.engine.gates.GateTest
```

## Tablas de verdad desde el motor

`Circuit` ofrece métodos para mostrar las tablas de las compuertas agregadas al circuito y para generar la tabla de una compuerta concreta. Para construir cada tabla, el motor recorre las dependencias de la compuerta, encuentra las entradas lógicas utilizadas y calcula las salidas para sus combinaciones. También restaura los valores originales de las entradas al terminar.

## Estado del proyecto

El proyecto cuenta con un editor gráfico para colocar compuertas e interruptores, conectarlos y consultar las tablas de verdad. El motor lógico también puede usarse de manera independiente mediante sus clases y la demostración de consola. La configuración actual no define un conjunto de pruebas automatizadas.

## Autores

- Kevin Celis
- Shaday Arevalo
- Juan Ocampo
