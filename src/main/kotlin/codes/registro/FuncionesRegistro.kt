package org.example.codes.registro

import com.mongodb.client.model.Updates
import org.bson.Document
import org.example.codes.cerveza.funcionesCervezas
import org.example.codes.coleccionCervezas
import org.example.codes.coleccionRegistro
import org.example.codes.coleccionTapas
import org.example.codes.funcionesExtra
import org.example.codes.proveedores.funcionesProveedores
import org.example.codes.tapas.funcionesTapas

class FuncionesRegistro {
    fun mostrarRegistroDetallado() {
        val pipeline = listOf(
            // Cervezas
            Document("\$lookup", Document()
                .append("from", "cervezas")
                .append("localField", "idCerveza")
                .append("foreignField", "idCerveza")
                .append("as", "cerveza")
            ),
            Document("\$unwind", Document()
                .append("path", "\$cerveza")
                .append("preserveNullAndEmptyArrays", true)
            ),

            // Tapas
            Document("\$lookup", Document()
                .append("from", "tapas")
                .append("localField", "idTapa")
                .append("foreignField", "idTapa")
                .append("as", "tapa")
            ),
            Document("\$unwind", Document()
                .append("path", "\$tapa")
                .append("preserveNullAndEmptyArrays", true)
            ),

            // Proveedores
            Document("\$lookup", Document()
                .append("from", "proveedores")
                .append("localField", "idProveedor")
                .append("foreignField", "idProveedor")
                .append("as", "proveedor")
            ),
            Document("\$unwind", Document()
                .append("path", "\$proveedor")
                .append("preserveNullAndEmptyArrays", true)
            ),

            // Proyección final (como tu factura)
            Document("\$project", Document()
                .append("fecha", "\$fechaDia")
                .append("operacion", "\$sumaResta")
                .append("cerveza", "\$cerveza.nombre")
                .append("tapa", "\$tapa.nombreTapa")
                .append("cantidadCerveza", 1)
                .append("cantidadTapa", 1)
                .append("proveedor", "\$proveedor.nombreProveedor")
            ),

            Document("\$sort", Document("fecha", -1))
        )

        val registros = coleccionRegistro.aggregate(pipeline).toList()

        if (registros.isEmpty()) {
            println("No hay registros.")
            return
        }

        println("================================================================================")
        println(String.format(
            "%-12s %-8s %-15s %-15s %-10s %-15s",
            "Fecha", "Tipo", "Cerveza", "Tapa", "Cantidad", "Proveedor"
        ))
        println("--------------------------------------------------------------------------------")

        registros.forEach { r ->
            val fecha = r["fecha"] ?: "-"
            val operacion = r["operacion"] ?: "-"
            val cerveza = r["cerveza"] ?: "-"
            val tapa = r["tapa"] ?: "-"
            val cantidad = when {
                (r["cantidadCerveza"] as Int) > 0 -> r["cantidadCerveza"]
                else -> r["cantidadTapa"]
            }
            val proveedor = r["proveedor"] ?: "-"

            println(String.format(
                "%-12s %-8s %-15s %-15s %-10s %-15s",
                fecha, operacion, cerveza, tapa, cantidad, proveedor
            ))
        }

        println("================================================================================")
    }
    fun sumarCervezas() {
        funcionesCervezas.mostrarCervezas()
        funcionesCervezas.mostrarCervezas()
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la Cerveza: ", Int::class.java)
        val cantidad = funcionesExtra.leerDato("Introduce cantidad a sumar: ", Int::class.java)

        funcionesProveedores.mostrarProveedores()
        val idProveedor = funcionesExtra.leerDato("Introduce ID del Proveedor: ", Int::class.java)

        val cerveza = coleccionCervezas.find(Document("idCerveza", idCerveza)).first()
        if (cerveza == null) {
            println("La cerveza no existe")
            return
        }

        coleccionCervezas.updateOne(
            Document("idCerveza", idCerveza),
            Updates.inc("cantidad", cantidad)
        )

        val registro = Document()
            .append("idRegistro", nuevoIdRegistro())
            .append("idCerveza", idCerveza)
            .append("cantidadCerveza", cantidad)
            .append("idTapa", null)
            .append("cantidadTapa", 0)
            .append("idProveedor", idProveedor)
            .append("sumaResta", "SUMA")
            .append("fechaDia", fechaActual())

        coleccionRegistro.insertOne(registro)
        println("Stock de cerveza actualizado correctamente.")
    }
    fun restarCerveza() {
        funcionesCervezas.mostrarCervezas()
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la Cerveza: ", Int::class.java)
        val cantidadRestar = funcionesExtra.leerDato("Introduce cantidad a restar: ", Int::class.java)

        val cerveza = coleccionCervezas.find(Document("idCerveza", idCerveza)).first()
        if (cerveza == null) {
            println("La cerveza no existe.")
            return
        }

        val cantidadActual = cerveza.getInteger("cantidad")

        if (cantidadRestar > cantidadActual) {
            println("❌ No hay suficiente stock. Stock actual: $cantidadActual")
            return
        }

        // Restar (puede quedar en 0)
        coleccionCervezas.updateOne(
            Document("idCerveza", idCerveza),
            Updates.inc("cantidad", -cantidadRestar)
        )

        val registro = Document()
            .append("idRegistro", nuevoIdRegistro())
            .append("idCerveza", idCerveza)
            .append("cantidadCerveza", cantidadRestar)
            .append("idTapa", null)
            .append("cantidadTapa", 0)
            .append("idProveedor", null)
            .append("sumaResta", "RESTA")
            .append("fechaDia", fechaActual())

        coleccionRegistro.insertOne(registro)

        println("✔ Stock actualizado. Nuevo stock: ${cantidadActual - cantidadRestar}")
    }
    fun sumarTapa() {
        funcionesTapas.mostrarTapas()
        val idTapa = funcionesExtra.leerDato("Introduce ID de la Tapa: ", Int::class.java)
        val cantidad = funcionesExtra.leerDato("Introduce cantidad a sumar: ", Int::class.java)

        coleccionTapas.updateOne(
            Document("idTapa", idTapa),
            Updates.inc("cantidadTapa", cantidad)
        )

        val registro = Document()
            .append("idRegistro", nuevoIdRegistro())
            .append("idCerveza", null)
            .append("cantidadCerveza", 0)
            .append("idTapa", idTapa)
            .append("cantidadTapa", cantidad)
            .append("idProveedor", null)
            .append("sumaResta", "SUMA")
            .append("fechaDia", fechaActual())

        coleccionRegistro.insertOne(registro)
        println("Stock de tapas actualizado.")
    }
    fun restarTapa() {
        funcionesTapas.mostrarTapas()
        val idTapa = funcionesExtra.leerDato("Introduce ID de la Tapa: ", Int::class.java)
        val cantidadRestar = funcionesExtra.leerDato("Introduce cantidad a restar: ", Int::class.java)

        val tapa = coleccionTapas.find(Document("idTapa", idTapa)).first()
        if (tapa == null) {
            println("La tapa no existe.")
            return
        }

        val cantidadActual = tapa.getInteger("cantidadTapa")

        if (cantidadRestar > cantidadActual) {
            println("❌ No hay suficiente stock. Stock actual: $cantidadActual")
            return
        }

        coleccionTapas.updateOne(
            Document("idTapa", idTapa),
            Updates.inc("cantidadTapa", -cantidadRestar)
        )

        val registro = Document()
            .append("idRegistro", nuevoIdRegistro())
            .append("idCerveza", null)
            .append("cantidadCerveza", 0)
            .append("idTapa", idTapa)
            .append("cantidadTapa", cantidadRestar)
            .append("idProveedor", null)
            .append("sumaResta", "RESTA")
            .append("fechaDia", fechaActual())

        coleccionRegistro.insertOne(registro)

        println("✔ Stock de tapas actualizado. Nuevo stock: ${cantidadActual - cantidadRestar}")
    }
    fun sumarAmbos() {
        funcionesCervezas.mostrarCervezas()
        val idCerveza = funcionesExtra.leerDato("ID Cerveza: ", Int::class.java)
        val cantCerveza = funcionesExtra.leerDato("Cantidad cerveza a sumar: ", Int::class.java)

        funcionesTapas.mostrarTapas()
        val idTapa = funcionesExtra.leerDato("ID Tapa: ", Int::class.java)
        val cantTapa = funcionesExtra.leerDato("Cantidad tapa a sumar: ", Int::class.java)

        funcionesProveedores.mostrarProveedores()
        val idProveedor = funcionesExtra.leerDato("ID Proveedor: ", Int::class.java)

        // Actualizar stocks
        coleccionCervezas.updateOne(
            Document("idCerveza", idCerveza),
            Updates.inc("cantidad", cantCerveza)
        )

        coleccionTapas.updateOne(
            Document("idTapa", idTapa),
            Updates.inc("cantidadTapa", cantTapa)
        )

        // Registro
        val registro = Document()
            .append("idRegistro", nuevoIdRegistro())
            .append("idCerveza", idCerveza)
            .append("cantidadCerveza", cantCerveza)
            .append("idTapa", idTapa)
            .append("cantidadTapa", cantTapa)
            .append("idProveedor", idProveedor)
            .append("sumaResta", "SUMA")
            .append("fechaDia", fechaActual())

        coleccionRegistro.insertOne(registro)

        println("✔ Stock de cerveza y tapas incrementado correctamente.")
    }
    fun restarAmbos() {
        funcionesCervezas.mostrarCervezas()
        val idCerveza = funcionesExtra.leerDato("ID Cerveza: ", Int::class.java)
        val cantCerveza = funcionesExtra.leerDato("Cantidad cerveza a restar: ", Int::class.java)

        funcionesTapas.mostrarTapas()
        val idTapa = funcionesExtra.leerDato("ID Tapa: ", Int::class.java)
        val cantTapa = funcionesExtra.leerDato("Cantidad tapa a restar: ", Int::class.java)

        val cerveza = coleccionCervezas.find(Document("idCerveza", idCerveza)).first()
        val tapa = coleccionTapas.find(Document("idTapa", idTapa)).first()

        if (cerveza == null || tapa == null) {
            println("❌ Cerveza o tapa no existe.")
            return
        }

        val stockCerveza = cerveza.getInteger("cantidad")
        val stockTapa = tapa.getInteger("cantidadTapa")

        // VALIDACIÓN CLAVE
        if (cantCerveza > stockCerveza) {
            println("❌ No hay suficiente cerveza. Stock actual: $stockCerveza")
            return
        }

        if (cantTapa > stockTapa) {
            println("❌ No hay suficientes tapas. Stock actual: $stockTapa")
            return
        }

        // Restar ambos (ya validado)
        coleccionCervezas.updateOne(
            Document("idCerveza", idCerveza),
            Updates.inc("cantidad", -cantCerveza)
        )

        coleccionTapas.updateOne(
            Document("idTapa", idTapa),
            Updates.inc("cantidadTapa", -cantTapa)
        )

        val registro = Document()
            .append("idRegistro", nuevoIdRegistro())
            .append("idCerveza", idCerveza)
            .append("cantidadCerveza", cantCerveza)
            .append("idTapa", idTapa)
            .append("cantidadTapa", cantTapa)
            .append("idProveedor", null)
            .append("sumaResta", "RESTA")
            .append("fechaDia", fechaActual())

        coleccionRegistro.insertOne(registro)

        println("✔ Stock de cerveza y tapas reducido correctamente.")
    }
    fun mostrarStockTotal() {
        println("\n=== Stock de Cervezas ===")
        coleccionCervezas.find().forEach {
            println("${it.getString("nombre")} → ${it.getInteger("cantidad")}")
        }

        println("\n=== Stock de Tapas ===")
        coleccionTapas.find().forEach {
            println("${it.getString("nombreTapa")} → ${it.getInteger("cantidadTapa")}")
        }
    }

}
val funcionesRegistro = FuncionesRegistro()

private fun nuevoIdRegistro(): Int {
    return coleccionRegistro.find()
        .sort(Document("idRegistro", -1))
        .first()
        ?.getInteger("idRegistro") ?: 0 + 1
}
private fun fechaActual(): String {
    return java.time.LocalDate.now().toString()
}

