package org.example.codes.cerveza

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import org.bson.Document
import org.example.codes.coleccionCervezas
import org.example.codes.funcionesExtra

class FuncionesCervezas {
    fun mostrarCervezas() {
        println("\n=== Listado Cervezas ===")
        coleccionCervezas.find().forEach { doc ->
            val id = doc.getInteger("idCerveza")
            val nombre = doc.getString("nombre")
            val graduacion = doc.get("graduacion", Number::class.java).toDouble()
            val tipo = doc.getString("tipo")
            val color = doc.getString("color")
            val origen = doc.getString("origen")
            val puntuacion = doc.get("puntuacion", Number::class.java).toDouble()
            val precio = doc.get("precio", Number::class.java).toDouble()
            val cantidad = doc.getInteger("cantidad")
            println("ID: $id " +
                    "- Nombre: $nombre " +
                    "- Graduación: $graduacion " +
                    "- Tipo: $tipo " +
                    "- Color: $color " +
                    "- Origen: $origen " +
                    "- Puntuación: $puntuacion " +
                    "- Precio: $precio$ " +
                    "- Cantidad: $cantidad")
        }
    }
    fun insertarCerveza() {
        val maxId = coleccionCervezas.find()
            .sort(Document("idCerveza", -1))
            .first()
            ?.getInteger("idCerveza") ?: 0  // Si no hay documentos, empieza desde 0
        val nombre = funcionesExtra.leerDato("Introduce nombre: ", String::class.java)
        val graduacion = funcionesExtra.leerDato("Introduce graducaión: ", Double::class.java)
        val tipo = funcionesExtra.leerDato("Introduce el tipo: ", String::class.java)
        val color = funcionesExtra.leerDato("Introduce el color: ", String::class.java)
        val origen = funcionesExtra.leerDato("Introduce el origen: ", String::class.java)
        val puntuacion = funcionesExtra.leerDato("Introduce la puntuación: ", Double::class.java)
        val precio = funcionesExtra.leerDato("Introduce precio: ", Double::class.java)
        val cantidad = funcionesExtra.leerDato("Introduce cantidad: ", Double::class.java)
        val nuevaCerveza = Document()
            .append("idCerveza", maxId + 1)
            .append("nombre", nombre)
            .append("graduacion", graduacion)
            .append("tipo", tipo)
            .append("color", color)
            .append("origen", origen)
            .append("puntuacion", puntuacion)
            .append("precio", precio)
            .append("cantidad", cantidad)
        coleccionCervezas.insertOne(nuevaCerveza)
        println("Cerveza '$nombre' añadida correctamente con ID ${maxId + 1}.")
    }
    fun actualizarCerveza() {
        mostrarCervezas()
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la cerveza a modificar: ", Int::class.java)
        // Buscar el documento
        val doc = coleccionCervezas.find(Document("idCerveza", idCerveza)).first()
        if (doc == null) {
            println("No existe ninguna cerveza con ID = $idCerveza")
            return
        }
        println("Cerveza seleccionada: ${doc.getString("nombre")}")
        val opcion = funcionesExtra.leerDato(variablesCerveza.menuEdicionCerveza, Int::class.java)
        if (opcion !in 0..8) {
            println("Opción no válida.")
            return
        }
        if (opcion == 0) {
            println("Actualización cancelada.")
            return
        }
        val update = when (opcion) {
            1 -> Updates.set("nombre", funcionesExtra.leerDato("Nuevo nombre: ", String::class.java))
            2 -> Updates.set("graduacion", funcionesExtra.leerDato("Nueva graduación: ", Double::class.java))
            3 -> Updates.set("tipo", funcionesExtra.leerDato("Nuevo tipo: ", String::class.java))
            4 -> Updates.set("color", funcionesExtra.leerDato("Nuevo color: ", String::class.java))
            5 -> Updates.set("origen", funcionesExtra.leerDato("Nuevo origen: ", String::class.java))
            6 -> Updates.set("puntuacion", funcionesExtra.leerDato("Nueva graduación: ", Double::class.java))
            7 -> Updates.set("precio", funcionesExtra.leerDato("Nueva precio: ", Double::class.java))
            8 -> Updates.set("cantidad", funcionesExtra.leerDato("Nueva cantidad: ", Int::class.java))
            else -> null
        }
        if (update != null) {
            val resultado = coleccionCervezas.updateOne(Document("idCerveza", idCerveza), update)
            if (resultado.modifiedCount > 0) {
                println("Cerveza con ID = $idCerveza actualizada con éxito.")
            } else {
                println("No se pudo actualizar la cerveza.")
            }
        }
    }
    fun eliminarCerveza() {
        mostrarCervezas()
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la cerveza a eliminar: ", Int::class.java)
        val doc = coleccionCervezas.find(Document("idCerveza", idCerveza)).first()
        if (doc == null) {
            println("No existe ninguna cerveza con ID = $idCerveza")
            return
        }
        val nombre = doc.getString("nombre")
        print("El nombre de la cerveza con ID $idCerveza es '$nombre'.")
        val respuesta = funcionesExtra.leerDato("¿Quieres eliminarla? (s/n): ", String::class.java).lowercase()
        if (respuesta != "s") {
            println("Eliminación cancelada.")
            return
        }
        val resultado = coleccionCervezas.deleteOne(Document("idCerveza", idCerveza))
        if (resultado.deletedCount > 0) {
            println("Cerveza '$nombre' eliminada correctamente.")
        } else {
            println("No se pudo eliminar la cerveza.")
        }
    }
    fun consultarConFiltros() {
        println("\n=== Cervezas con graduación > 5 ===")
        val cervezasFuertes = coleccionCervezas.find(Filters.gt("graduacion", 5.0))
        for (doc in cervezasFuertes) {
            println("${doc.getString("nombre")} - Graduación: ${doc.get("graduacion", Number::class.java).toDouble()}")
        }
        println("\n=== Cervezas tipo Lager ===")
        val cervezasLager = coleccionCervezas.find(Filters.eq("tipo", "Lager"))
        for (doc in cervezasLager) {
            println(doc.getString("nombre"))
        }
    }
    fun consultarConProyeccion() {
        println("\n=== Cervezas con solo nombre y graduación ===")
        val proyeccion = coleccionCervezas.find()
            .projection(Projections.include("nombre", "graduacion"))
        for (doc in proyeccion) {
            println("Nombre: ${doc.getString("nombre")} - Graduación: ${doc.get("graduacion", Number::class.java).toDouble()}")
        }
    }
    fun calcularPromedioGraduacion() {
        val promedio = coleccionCervezas.aggregate(
            listOf(Aggregates.group(null, Accumulators.avg("promedio", $$"$graduacion")))
        ).first()
        println("\nPromedio de graduación de todas las cervezas: ${promedio?.getDouble("promedio")}")
    }
    fun promedioPuntuacionPorTipo() {
        println("\n=== Promedio de puntuación por tipo de cerveza ===")
        val promedioPorTipo = coleccionCervezas.aggregate(
            listOf(Aggregates.group($$"$tipo", Accumulators.avg("promedioPuntuacion", $$"$puntuacion")))
        )
        for (doc in promedioPorTipo) {
            println("Tipo: ${doc.getString("_id")} - Promedio: ${doc.getDouble("promedioPuntuacion")}")
        }
    }
    fun top3Cervezas() {
        println("\n=== Top 3 cervezas por puntuación ===")
        val top3 = coleccionCervezas.aggregate(
            listOf(
                Aggregates.sort(Sorts.descending("puntuacion")),
                Aggregates.limit(3)
            )
        )
        for (doc in top3) {
            println("${doc.getString("nombre")} - Puntuación: ${doc.get("puntuacion", Number::class.java).toDouble()}")
        }
    }
}
val funcionesCervezas = FuncionesCervezas()
