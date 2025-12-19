package org.example.codes

class Variables {
    // Variables de inicio
    val titulo = "La Birra de Brian: La Caída"
    val menuInicial = "\n" + """Menu principal:
    1. Menu CRUD
    2. Consultas Adicionales
    0. Salir
Introduce una opción entre 0 y 2 por favor: """.trimIndent()
    val menuCRUD = "\n" + """Menú CRUD
    1. Cervezas
    2. Tapas
    3. Proveedores
    0. Volver
Introduce una de las opciones: """.trimIndent()
    val menuAdicionales = "\n" + """Menú consultas adicionales
    1. Cervezas
    2. Registro
    0. Volver
Introduce una de las opciones: """.trimIndent()
    var salirMenuInicial = false
    var salirCRUD = false
    var salirAdicionales = false
    val finPrograma = "\nFin de programa"
}
val variables = Variables()