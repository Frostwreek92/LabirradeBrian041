package org.example.codes.tapas

class VariablesTapa {
    val rutaJSONTapas = "src/main/resources/LaBirradeBrian_tapas.json"
    val menuCRUDTapas = "\n" + """Menu CRUD Tapas:
    1. Ver Información de las Tapas
    2. Insertar una nueva Tapa
    3. Modificar Tapa por ID introducido
    4. Eliminar Tapa por ID introducido
    0. Volver
Introduce una opción entre 0 y 4 por favor: """.trimIndent()
    val menuEdicionTapa = "\n" + """¿Qué desea modificar?
    1. Nombre
    2. Cantidad
    3. Precio
    0. Volver
Tu opción: """.trimIndent()
    var salirMenuCRUDTapas = false
}
val variablesTapa = VariablesTapa()