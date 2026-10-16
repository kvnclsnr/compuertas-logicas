# Simulador de compuertas lógicas

Proyecto universitario de la materia **Programación Orientada a Objetos (POO)**. Lo desarrollamos poco a poco para modelar compuertas lógicas y organizar sus componentes aplicando conceptos de programación orientada a objetos

## Autores

- Kevin Celis
- Shaday Arevalo
- Juan Ocampo

## Descripción

El proyecto está escrito en Java y modela entradas lógicas, distintos tipos de compuertas y un banco para administrarlas. Las compuertas heredan de la clase abstracta `Gate` e implementan su operación lógica. Cada compuerta puede mostrar su tabla de verdad

Actualmente incluye:

- `LogicInput`: representa una entrada lógica con nombre y valor booleano
- `Gate`: clase abstracta base para las compuertas
- `AndGate`, `NandGate`, `OrGate`, `NorGate`, `NotGate`, `XorGate` y `XnorGate`
- `GateBank`: permite agregar, buscar, eliminar y evaluar las compuertas que contiene
- `Circuit`: estructura inicial del modelo de circuitos, todavía en desarrollo
- `GateDemo`: muestra las tablas de verdad de las compuertas

El proyecto continúa en construcción; algunas clases y funcionalidades, como el modelo de circuitos y las pruebas del banco, todavía están incompletas

## Estructura del proyecto

```text
src/
└── com/simulator/model/
    ├── LogicInput.java
    ├── banks/
    │   └── GateBank.java
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

## Requisitos

- JDK compatible con la versión configurada en el proyecto (`Java 26`)
- Apache Ant para compilar o ejecutar desde la terminal
- También puedes abrir el proyecto en NetBeans usando sus archivos de configuración de Ant

## Compilar

Desde la carpeta raíz del proyecto, ejecuta:

```bash
ant compile
```

Los archivos compilados se generan en `build/classes`

## Ejecutar

La clase principal configurada muestra las tablas de verdad de las siete compuertas:

```bash
ant run
```

También se puede ejecutar `com.simulator.model.gates.GateTest` desde el IDE

## Pruebas y próximos pasos

`GateBankTest` es una clase inicial para las pruebas del banco, pero todavía no contiene pruebas automatizadas. Entre los próximos pasos están completar el modelo de circuitos y agregar pruebas para las compuertas y el banco
