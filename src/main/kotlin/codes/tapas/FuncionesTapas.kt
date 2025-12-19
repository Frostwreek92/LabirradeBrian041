package org.example.codes.tapas

import com.mongodb.client.model.Updates
import org.example.codes.coleccionTapas
import org.bson.Document
import org.example.codes.funcionesExtra

class FuncionesTapas{
    fun mostrarTapas() {
        println("\n=== Listado Tapas ===")
        coleccionTapas.find().forEach { doc ->
            val id = doc.getInteger("idTapa")
            val nombre = doc.getString("nombreTapa")
            val cantidadTapa = doc.getInteger("cantidadTapa")
            val precioTapa = doc.get("precioTapa", Number::class.java).toDouble()
            println("ID: $id " +
                    "- Nombre: $nombre " +
                    "- Cantidad: $cantidadTapa " +
                    "- Precio: $precioTapa")
        }
    }
    fun insertarTapa() {
        val maxID = coleccionTapas.find()
            .sort(Document("idTapa", -1))
            .first()
            ?.getInteger("idTapa") ?: 0
        val nombreTapa = funcionesExtra.leerDato("Introduce nombre: ", String::class.java)
        val cantidadTapa = funcionesExtra.leerDato("Introduce cantidad: ", Int::class.java)
        val precioTapa = funcionesExtra.leerDato("Introduce precio: ", Double::class.java)
        val nuevaTapa = Document()
            .append("idTapa", maxID + 1)
            .append("nombreTapa", nombreTapa)
            .append("cantidadTapa", cantidadTapa)
            .append("precioTapa", precioTapa)
        coleccionTapas.insertOne(nuevaTapa)
        println("Tapa '$nombreTapa' añadida correctamente con ID ${maxID + 1}.")
    }
    fun actualizarTapa() {
        mostrarTapas()
        val idTapa = funcionesExtra.leerDato("Introduce ID de la tapa a modificar: ", Int::class.java)
        val doc = coleccionTapas.find(Document("idTapa", idTapa)).first()
        if (doc == null) {
            println("No existe ninguna tapa con el id = $idTapa")
            return
        }
        println("Tapa seleccionada: ${doc.getString("nombreTapa")}")
        val opcion = funcionesExtra.leerDato(variablesTapa.menuEdicionTapa, Int::class.java)
        if (opcion !in 0 .. 3) {
            println("Opción no válida")
            return
        }
        if (opcion == 0) {
            println("Actualización cancelada.")
            return
        }
        val update = when (opcion) {
            1 -> Updates.set("nombreTapa", funcionesExtra.leerDato("Introduce nombre: ", String::class.java))
            2 -> Updates.set("cantidadTapa", funcionesExtra.leerDato("Introduce cantidad: ", Int::class.java))
            3 -> Updates.set("precioTapa", funcionesExtra.leerDato("Introduce precio: ", Double::class.java))
            else -> null
        }
        if (update != null) {
            val resultado = coleccionTapas.updateOne(Document("idTapa", idTapa), update)
            if (resultado.modifiedCount > 0) {
                println("Tapa con ID = $idTapa actualizada con éxito.")
            } else {
                println("No se puede actualizar la tapa.")
            }
        }
    }
    fun eliminarTapa() {
        mostrarTapas()
        val idTapa = funcionesExtra.leerDato("Introduce ID de la tapa a eliminar: ", Int::class.java)
        val doc = coleccionTapas.find(Document("idTapa", idTapa)).first()
        if (doc == null) {
            println("No existe ninguna tapa con ID = $idTapa")
            return
        }
        val nombre = doc.getString("nombreTapa")
        println("El nombre de la tapa seleccionada con ID $idTapa es '$nombre'")
        val respuesta = funcionesExtra.leerDato("¿Quieres eliminarla? (s/n): ", String::class.java).lowercase()
        if (respuesta != "s") {
            println("Eliminación cancelada")
            return
        }
        val resultado = coleccionTapas.deleteOne(Document("idTapa", idTapa))
        if (resultado.deletedCount > 0) {
            println("Tapa '$nombre' eliminada correctamente.")
        } else {
            println("No se puede eliminar la tapa.")
        }
    }
}
val funcionesTapas = FuncionesTapas()
