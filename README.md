# Simulador de compuertas lógicas

Aplicación de consola desarrollada en Java para modelar compuertas lógicas, conectarlas en circuitos y generar sus tablas de verdad

## Autores

- Kevin Celis
- Shaday Arevalo
- Juan Ocampo

## Funcionalidades

- Representa entradas lógicas con nombre y valor booleano
- Modela las operaciones AND, NAND, NOR, NOT, OR, XNOR y XOR
- Permite conectar entradas y compuertas para formar circuitos
- Muestra la tabla de verdad de cada compuerta agregada al circuito o de una compuerta específica
- Detecta automáticamente las entradas utilizadas y muestra las expresiones de las compuertas que forman la salida
- Permite consultar, evaluar y eliminar compuertas de un circuito

## Organización del proyecto

```text
src/
└── com/simulator/model/
    ├── LogicInput.java
    ├── LogicSource.java
    ├── banks/
    │   ├── GateBank.java
    │   └── GateBankTest.java
    ├── circuit/
    │   └── Circuit.java
    └── gates/
        ├── Gate.java
        ├── AndGate.java
        ├── NandGate.java
        ├── NorGate.java
        ├── NotGate.java
        ├── OrGate.java
        ├── XnorGate.java
        ├── XorGate.java
        └── GateTest.java
```

`LogicSource` define la interfaz común para entradas y compuertas, mientras que `Gate` implementa el comportamiento compartido por los distintos tipos de compuerta

`Circuit` administra las compuertas y genera las tablas de verdad. `GateBank` está obsoleto y ya no forma parte del flujo principal

## Requisitos

- JDK 26, según la versión configurada para compilar el proyecto
- Apache Ant para compilar
- NetBeans es opcional y permite abrir el proyecto con su configuración de Ant

## Compilar

Desde la carpeta raíz del proyecto, ejecuta:

```bash
ant compile
```

Las clases compiladas se guardan en `build/classes`

## Ejecutar la demostración

La clase `GateTest` contiene el ejemplo de uso del proyecto. Después de compilar, ejecútala con:

```bash
java -cp build/classes com.simulator.model.gates.GateTest
```

El ejemplo crea un circuito con compuertas AND, NAND y OR, imprime las tablas de las compuertas registradas y luego muestra la tabla de una compuerta específica

## Uso de las tablas de verdad

`printTruthTable()` muestra las tablas de todas las compuertas agregadas al circuito

`printTruthTable(gate)` muestra la tabla de la compuerta indicada, aunque no esté agregada individualmente al circuito

Las entradas se obtienen recorriendo la compuerta y sus dependencias, por lo que no es necesario pasar una lista de entradas

## Estado del proyecto

El simulador genera tablas de verdad para circuitos compuestos y compuertas individuales. `GateBankTest` está vacío y no hay pruebas automatizadas configuradas todavía
