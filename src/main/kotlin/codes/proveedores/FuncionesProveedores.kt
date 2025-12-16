package org.example.codes.proveedores

import org.bson.Document
import org.example.codes.coleccionProveedores
import org.example.codes.funcionesExtra

class FuncionesProveedores {
    fun mostrarProveedores() {
        println("\n=== Listado Proveedores ===")
        coleccionProveedores.find().forEach { doc ->
            val id = doc.getInteger("idProveedor")
            val nombre = doc.getString("nombreProveedor")
            println("ID: $id " +
                    "- Nombre: $nombre")
        }
    }
    fun insertarProveedor() {
        val maxID = coleccionProveedores.find()
            .sort(Document("idProveedor", -1))
            .first()
            ?.getInteger("idProveedor") ?: 0
        val nombreProveedor = funcionesExtra.leerDato("Introduce nombre: ", String::class.java)
        val nuevoProveedor = Document()
            .append("idProveedor", maxID + 1)
            .append("nombreProveedor", nombreProveedor)
        coleccionProveedores.insertOne(nuevoProveedor)
        println("Proveedor '$nombreProveedor' añadido correctamente con ID ${maxID + 1}.")
    }
    fun actualizarProveedor() {
        mostrarProveedores()
    }
    fun eliminarProveedor() {
        mostrarProveedores()
    }
}
val funcionesProveedores = FuncionesProveedores()
