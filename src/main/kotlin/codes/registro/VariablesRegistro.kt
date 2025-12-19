package org.example.codes.registro

class VariablesRegistro {
    val rutaJSONRegistro = "src/main/resources/LaBirradeBrian_registro.json"
    val menuRegistro = "\n" + """Menú Registro:
        1. Mostrar el Registro
        2. Añadir Stock Cervezas
        3. Retirar Stock Cervezas
        4. Añadir Stock Tapas
        5. Restar Stock Tapas
        6. Sumar Stock Completo
        7. Restar Stock Completo
        8. Mostrar Stock Total
        0. Volver
Selecciona una opción: """.trimIndent()
    var salirRegistro = false
}
val variablesRegistro = VariablesRegistro()
