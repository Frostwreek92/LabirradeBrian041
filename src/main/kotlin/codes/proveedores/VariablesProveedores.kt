package org.example.codes.proveedores

class VariablesProveedores {
    val rutaJSONProveedores = "src/main/resources/LaBirradeBrian_proveedores.json"
    val menuCRUDProveedores = "\n" + """Menu CRUD Proveedores:
    1. Ver Información de los Proveedores
    2. Insertar un nuevo Proveedor
    3. Modificar Proveedor por ID introducido
    4. Eliminar Proveedor por ID introducido
    0. Volver
Introduce una opción entre 0 y 4 por favor: """.trimIndent()
    val menuEdicionProveedor = "\n" + """¿Qué desea modificar?
    1. Nombre
    0. Volver
Tu opción: """.trimIndent()
    var salirMenuCRUDProveedor = false
}
val variablesProveedores = VariablesProveedores()
