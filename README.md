# La Birra de Brian: La Caída

Proyecto de aplicación **de consola en Kotlin** para la gestión de un bar ficticio llamado **La Birra de Brian**. Permite administrar **cervezas, tapas, proveedores y stock**, utilizando una **base de datos MongoDB en memoria**, con importación y exportación de datos en formato **JSON**.

---

## Características principales

* Menús interactivos por consola
* CRUD completo de:

    * Cervezas
    * Tapas
    * Proveedores
* Gestión de stock con registro de movimientos
* Consultas avanzadas sobre cervezas
* Base de datos MongoDB **en memoria** (no requiere servidor externo)
* Importación y exportación automática de datos JSON

---

## Tecnologías utilizadas

* **Kotlin**
* **MongoDB en memoria** (`de.bwaldvogel.mongo`)
* **MongoDB Java Driver**
* **JSON** para persistencia de datos
* **Gradle** (recomendado)

---

## Estructura del proyecto

```
main
│
├── kotlin
│   │
│   ├── main.kt
│   │
│   └── codes
│       ├── Programa.kt
│       ├── Variables.kt
│       ├── FuncionesExtra.kt
│       │
│       ├── cerveza
│       │   ├── VariablesCerveza.kt
│       │   └── FuncionesCervezas.kt
│       │
│       ├── tapas
│       │   ├── VariablesTapa.kt
│       │   └── FuncionesTapas.kt
│       │
│       ├── proveedores
│       │   ├── VariablesProveedores.kt
│       │   └── FuncionesProveedores.kt
│       │
│       └── registro
│           ├── VariablesRegistro.kt
│           └── FuncionesRegistro.kt
│
└── resources
    ├── LaBirradeBrian_cervezas
    ├── LaBirradeBrian_proveedores
    ├── LaBirradeBrian_registro
    └── LaBirradeBrian_tapas
```

---

## Ejecución del programa

1. Clona el repositorio
2. Abre el proyecto en **IntelliJ IDEA** u otro IDE compatible con Kotlin
3. Ejecuta la función `main()` ubicada en `main.kt`

El programa iniciará automáticamente el servidor MongoDB en memoria y cargará los datos desde los archivos JSON.

---

## Archivos JSON

Los datos se cargan y se guardan automáticamente en:

```
src/main/resources/
├── LaBirradeBrian_cervezas.json
├── LaBirradeBrian_tapas.json
├── LaBirradeBrian_proveedores.json
└── LaBirradeBrian_registro.json
```

Al cerrar el programa:

* Los datos se **exportan** automáticamente
* La base de datos en memoria se **elimina**

---

## Funcionalidades por módulo

### Cervezas

* Ver listado
* Insertar
* Modificar
* Eliminar
* Consultas avanzadas:

    * Filtros por graduación y tipo
    * Proyecciones
    * Promedios
    * Top 3 por puntuación

### Tapas

* Ver listado
* Insertar
* Modificar
* Eliminar

### Proveedores

* Ver listado
* Insertar
* Modificar
* Eliminar

### Registro y Stock

* Registro detallado de movimientos
* Suma y resta de stock
* Control de stock total
* Validaciones de cantidades

---

## Validaciones incluidas

* Control de stock insuficiente
* IDs inexistentes
* Tipos de datos incorrectos
* Confirmaciones antes de eliminar

---

## Notas importantes

* No es necesario instalar MongoDB
* La base de datos es **temporal**
* Ideal para prácticas de:

    * Kotlin
    * CRUD
    * MongoDB
    * Aggregations

---

## Autor

Álvaro García Mon.
