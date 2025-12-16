package org.example.codes.proveedores

import com.mongodb.client.model.Updates
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
        val idProveedor = funcionesExtra.leerDato("Introduce ID del Proveedor a modificar: ", Int::class.java)
        val doc = coleccionProveedores.find(Document("idProveedor", idProveedor)).first()
        if (doc == null) {
            println("No existe ningún proveedor con el id = $idProveedor")
            return
        }
        println("Proveedor seleccionado: ${doc.getString("nombreProveedor")}")
        val opcion = funcionesExtra.leerDato(variablesProveedores.menuEdicionProveedor, Int::class.java)
        if (opcion !in 0 .. 3) {
            println("Opción no válida")
            return
        }
        if (opcion == 0) {
            println("Actualización cancelada.")
            return
        }
        val update = when (opcion) {
            1 -> Updates.set("nombreProveedor", funcionesExtra.leerDato("Introduce nombre: ", String::class.java))
            else -> null
        }
        if (update != null) {
            val resultado = coleccionProveedores.updateOne(Document("idProveedor", idProveedor), update)
            if (resultado.modifiedCount > 0) {
                println("Proveedor con ID = $idProveedor actualizado con éxito.")
            } else {
                println("No se puede actualizar el proveedor.")
            }
        }
    }
    fun eliminarProveedor() {
        mostrarProveedores()
        val idProveedor = funcionesExtra.leerDato("Introduce ID del Proveedor a modificar: ", Int::class.java)
        val doc = coleccionProveedores.find(Document("idProveedor", idProveedor)).first()
        if (doc == null) {
            println("No existe ninguna tapa con ID = $idProveedor")
            return
        }
        val nombre = doc.getString("nombreProveedor")
        println("El nombre del proveedor seleccionada con ID $idProveedor es '$nombre'")
        val respuesta = funcionesExtra.leerDato("¿Quieres eliminarlo? (s/n): ", String::class.java).lowercase()
        if (respuesta != "s") {
            println("Eliminación cancelada")
            return
        }
        val resultado = coleccionProveedores.deleteOne(Document("idProveedor", idProveedor))
        if (resultado.deletedCount > 0) {
            println("Proveedor '$nombre' eliminado correctamente.")
        } else {
            println("No se puede eliminar el proveedor.")
        }
    }
}
val funcionesProveedores = FuncionesProveedores()
