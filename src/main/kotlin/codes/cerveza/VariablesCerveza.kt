package org.example.codes.cerveza

class VariablesCerveza {
    val rutaJSONCervezas = "src/main/resources/LaBirradeBrian_cervezas.json"
    val menuCRUDCervezas = "\n" + """Menu CRUD Cervezas:
    1. Ver Información de las Cervezas
    2. Insertar una nueva Cerveza
    3. Modificar Cerveza por ID introducido
    4. Eliminar Cerveza por ID introducido
    0. Volver
Introduce una opción entre 0 y 4 por favor:  """.trimIndent()
    val menuConsultasAdicionalesCervezas = "\n" + """Menu Consultas Adicionales Cervezas
    1. Consultas con Filtros
    2. Consulta con Proyecciones
    3. Calcular Promedio Graduación
    4. Promedio Puntuación por Tipo
    5. Top 3 Cervezas
    0. Volver
Introduce una opción entre 0 y 5 por favor:  """.trimIndent()
    var salirMenuCRUDCervezas = false
    var salirConsultasAdicionalesCervezas = false
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
val variablesCerveza = VariablesCerveza()