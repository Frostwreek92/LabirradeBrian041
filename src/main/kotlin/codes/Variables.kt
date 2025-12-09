package org.example.codes

class Variables {
    // Variables de inicio
    val titulo = "La Birra de Brian: La Caída"
    val rutaJSON = "src/main/resources/LaBirradeBrian_cervezas.json"
    val menuInicial = "\n" + """Menu principal:
    1. CRUD
    2. Consultas Adicionales
    0. Salir
Introduce una opción entre 0 y 4 por favor: """.trimIndent()
    val menuCRUD = "\n" + """Menu CRUD:
    1. Ver Información de las Cervezas
    2. Insertar una nueva Cerveza
    3. Modificar Cerveza por ID introducido
    4. Eliminar Cerveza por ID introducido
    0. Volver
Introduce una opción entre 0 y 4 por favor:  """.trimIndent()
    val menuConsultasAdicionales = "\n" + """Menu Consultas Adicionales
    1. Consultas con Filtros
    2. Consulta con Proyecciones
    3. Calcular Promedio Graduación
    4. Promedio Puntuación por Tipo
    5. Top 3 Cervezas
    0. Volver
Introduce una opción entre 0 y 5 por favor:  """.trimIndent()
    var salirMenuInicial = false
    var salirMenuCRUD = false
    var salirConsultasAdicionales = false
    val finPrograma = "\nFin de programa"
    val menuEdicionCerveza = "\n" + """¿Qué deseas modificar?
    1. Nombre
    2. Graduación
    3. Tipo
    4. Color
    5. Origen
    6. Puntuación
    0. Cancelar
Tu opción: """.trimIndent()
}
val variables = Variables()