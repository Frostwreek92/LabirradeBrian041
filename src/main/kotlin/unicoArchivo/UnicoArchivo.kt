package org.example.unicoArchivo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import org.bson.Document
import org.bson.json.JsonWriterSettings
import org.json.JSONArray
import java.io.File
import java.lang.Exception

fun main() {
    programa.iniciar()
}

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
    7. Precio
    8. Cantidad
    0. Cancelar
Tu opción: """.trimIndent()
}
val variablesCerveza = VariablesCerveza()
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

//BD y colección con la que se trabajará
const val NOM_BD = "LaBirradeBrian"
const val NOM_COLECCIONCERVEZAS = "cervezas"
const val NOM_COLECCIONTAPAS = "tapas"
const val NOM_COLECCIONPROVEEDORES = "proveedores"
const val NOM_COLECCIONREGISTRO = "registro"

//variables globales definidas sin inicializar
lateinit var servidor: MongoServer
lateinit var cliente: MongoClient
lateinit var uri: String
lateinit var coleccionCervezas: MongoCollection<Document>
lateinit var coleccionTapas: MongoCollection<Document>
lateinit var coleccionProveedores: MongoCollection<Document>
lateinit var coleccionRegistro: MongoCollection<Document>

class Programa {
    fun iniciar() {
        conectarBD()
        importar()
        variables.titulo
        try {
            while (!variables.salirMenuInicial) {
                val opcion = funcionesExtra.leerDato(variables.menuInicial, Int::class.java)
                when (opcion) {
                    1 -> menuCRUD()
                    2 -> consultasAdicionales()
                    0 -> variables.salirMenuInicial = funcionesExtra.finEleccion()
                    else -> println("Introduce una de las opciones")
                }
            }
        } catch (e: java.lang.Exception) {
            println("Excepción: $e")
        }
        variables.finPrograma
        exportar()
        desconectarBD()
    }
}
val programa = Programa()

fun menuCRUD() {
    variables.salirCRUD = false
    try {
        while (!variables.salirCRUD) {
            val opcion = funcionesExtra.leerDato(variables.menuCRUD, Int::class.java)
            when (opcion) {
                1 -> menuCRUDCervezas()
                2 -> menuCRUDTapas()
                3 -> menuCRUDProveedores()
                0 -> variables.salirCRUD = funcionesExtra.finEleccion()
            }
        }
    } catch (e: java.lang.Exception) {
        println("Excepción: $e")
    }
}
fun menuCRUDCervezas() {
    variablesCerveza.salirMenuCRUDCervezas = false
    try {
        while (!variablesCerveza.salirMenuCRUDCervezas) {
            val opcion = funcionesExtra.leerDato(variablesCerveza.menuCRUDCervezas, Int::class.java)
            when (opcion) {
                1 -> funcionesCervezas.mostrarCervezas()
                2 -> funcionesCervezas.insertarCerveza()
                3 -> funcionesCervezas.actualizarCerveza()
                4 -> funcionesCervezas.eliminarCerveza()
                0 -> variablesCerveza.salirMenuCRUDCervezas = funcionesExtra.finEleccion()
            }
        }
    } catch (e: java.lang.Exception) {
        println("Excepción: $e")
    }
}
fun menuCRUDTapas() {
    variablesTapa.salirMenuCRUDTapas = false
    try {
        while (!variablesTapa.salirMenuCRUDTapas) {
            val opcion = funcionesExtra.leerDato(variablesTapa.menuCRUDTapas, Int::class.java)
            when (opcion) {
                1 -> funcionesTapas.mostrarTapas()
                2 -> funcionesTapas.insertarTapa()
                3 -> funcionesTapas.actualizarTapa()
                4 -> funcionesTapas.eliminarTapa()
                0 -> variablesTapa.salirMenuCRUDTapas = funcionesExtra.finEleccion()
            }
        }
    } catch (e: java.lang.Exception) {
        println("Excepcion: $e")
    }
}
fun menuCRUDProveedores() {
    variablesProveedores.salirMenuCRUDProveedor = false
    try {
        while (!variablesProveedores.salirMenuCRUDProveedor) {
            val opcion = funcionesExtra.leerDato(variablesProveedores.menuCRUDProveedores, Int::class.java)
            when (opcion) {
                1 -> funcionesProveedores.mostrarProveedores()
                2 -> funcionesProveedores.insertarProveedor()
                3 -> funcionesProveedores.actualizarProveedor()
                4 -> funcionesProveedores.eliminarProveedor()
                0 -> variablesProveedores.salirMenuCRUDProveedor = funcionesExtra.finEleccion()
            }
        }
    } catch (e: java.lang.Exception) {
        println("Excepcion: $e")
    }
}

fun consultasAdicionales() {
    variables.salirAdicionales = false
    try {
        while (!variables.salirAdicionales) {
            val opcion = funcionesExtra.leerDato(variables.menuAdicionales, Int::class.java)
            when (opcion) {
                1 -> consultasAdicionalesCervezas()
                2 -> consultasRegistro()
                0 -> variables.salirAdicionales = funcionesExtra.finEleccion()
            }
        }
    } catch (e: java.lang.Exception) {
        println("Excepcion: $e")
    }
}
fun consultasAdicionalesCervezas() {
    variablesCerveza.salirConsultasAdicionalesCervezas = false
    try {
        while (!variablesCerveza.salirConsultasAdicionalesCervezas) {
            val opcion = funcionesExtra.leerDato(variablesCerveza.menuConsultasAdicionalesCervezas, Int::class.java)
            when (opcion) {
                1 -> funcionesCervezas.consultarConFiltros()
                2 -> funcionesCervezas.consultarConProyeccion()
                3 -> funcionesCervezas.calcularPromedioGraduacion()
                4 -> funcionesCervezas.promedioPuntuacionPorTipo()
                5 -> funcionesCervezas.top3Cervezas()
                0 -> variablesCerveza.salirConsultasAdicionalesCervezas = funcionesExtra.finEleccion()
            }
        }
    } catch (e: java.lang.Exception) {
        println("Excepción: $e")
    }
}
fun consultasRegistro() {
    variablesRegistro.salirRegistro = false
    try {
        while (!variablesRegistro.salirRegistro) {
            val opcion = funcionesExtra.leerDato(variablesRegistro.menuRegistro, Int::class.java)
            when (opcion) {
                1 -> funcionesRegistro.mostrarRegistroDetallado()
                2 -> funcionesRegistro.sumarCervezas()
                3 -> funcionesRegistro.restarCerveza()
                4 -> funcionesRegistro.sumarTapa()
                5 -> funcionesRegistro.restarTapa()
                6 -> funcionesRegistro.sumarAmbos()
                7 -> funcionesRegistro.restarAmbos()
                8 -> funcionesRegistro.mostrarStockTotal()
                0 -> variablesRegistro.salirRegistro = funcionesExtra.finEleccion()
            }
        }
    } catch (e: Exception) {
        println("Excepción: $e")
    }
}

// Función para conectar a la BD
fun conectarBD() {
    servidor = MongoServer(MemoryBackend())
    val address = servidor.bind()
    uri = "mongodb://${address.hostName}:${address.port}"
    cliente = MongoClients.create(uri)

    val db = cliente.getDatabase(NOM_BD)
    coleccionCervezas = db.getCollection(NOM_COLECCIONCERVEZAS)
    coleccionTapas = db.getCollection(NOM_COLECCIONTAPAS)
    coleccionProveedores = db.getCollection(NOM_COLECCIONPROVEEDORES)
    coleccionRegistro = db.getCollection(NOM_COLECCIONREGISTRO)

    println("Servidor MongoDB iniciado en $uri")
}
// Importar BD
fun importarBD(rutaJSON: String, coleccion: MongoCollection<Document>) {
    println("Iniciando importación de datos desde JSON...")
    val jsonFile = File(rutaJSON)
    if (!jsonFile.exists()) {
        println("No se encontró el archivo JSON a importar")
        return
    }
    // Leer JSON del archivo
    val jsonText = try {
        jsonFile.readText()
    } catch (e: kotlin.Exception) {
        println("Error leyendo el archivo JSON: ${e.message}")
        return
    }
    val array = try {
        JSONArray(jsonText)
    } catch (e: kotlin.Exception) {
        println("Error al parsear JSON: ${e.message}")
        return
    }
    // Convertir JSON a Document y eliminar _id si existe
    val documentos = mutableListOf<Document>()
    for (i in 0 until array.length()) {
        val doc = Document.parse(array.getJSONObject(i).toString())
        doc.remove("_id")  // <-- eliminar _id para que MongoDB genere uno nuevo
        documentos.add(doc)
    }
    if (documentos.isEmpty()) {
        println("El archivo JSON está vacío")
        return
    }
    val db = cliente.getDatabase(NOM_BD)
    val nombreColeccion =coleccion.namespace.collectionName
    // Borrar colección si existe
    if (db.listCollectionNames().contains(nombreColeccion)) {
        db.getCollection(nombreColeccion).drop()
        println("Colección '$nombreColeccion' eliminada antes de importar.")
    }
    // Insertar documentos
    try {
        coleccion.insertMany(documentos)
        println("Importación completada: ${documentos.size} documentos de $nombreColeccion.")
    } catch (e: kotlin.Exception) {
        println("Error importando documentos: ${e.message}")
    }
}
// Exportar BD
fun exportarBD(rutaJSON: String, coleccion: MongoCollection<Document>) {
    val settings = JsonWriterSettings.builder().indent(true).build()
    val file = File(rutaJSON)
    file.printWriter().use { out ->
        out.println("[")
        val cursor = coleccion.find().iterator()
        var first = true
        while (cursor.hasNext()) {
            if (!first) out.println(",")
            val doc = cursor.next()
            out.print(doc.toJson(settings))
            first = false
        }
        out.println("]")
        cursor.close()
    }
    println("Exportación de ${coleccion.namespace.collectionName} completada")
}
// Función para desconectar a la BD
fun desconectarBD() {
    cliente.close()
    servidor.shutdown()
    println("Servidor MongoDB en memoria finalizado")
}

fun importar() {
    importarBD(variablesCerveza.rutaJSONCervezas, coleccionCervezas)
    importarBD(variablesTapa.rutaJSONTapas, coleccionTapas)
    importarBD(variablesProveedores.rutaJSONProveedores, coleccionProveedores)
    importarBD(variablesRegistro.rutaJSONRegistro, coleccionRegistro)
}
fun exportar() {
    exportarBD(variablesCerveza.rutaJSONCervezas, coleccionCervezas)
    exportarBD(variablesTapa.rutaJSONTapas, coleccionTapas)
    exportarBD(variablesProveedores.rutaJSONProveedores, coleccionProveedores)
    exportarBD(variablesRegistro.rutaJSONRegistro, coleccionRegistro)
}

class FuncionesExtra {
    // Fin para cualquier when
    fun finEleccion(): Boolean {
        return true
    }
    /* Función para ahorrar líneas que se parece a Python al pedir datos
    * En la que puedes escribir un mensaje a mostrar*/
    private fun input(mensaje: String): String? {
        print(mensaje)
        return readlnOrNull()
    }
    // Función genérica para pedir datos seguros
    /* Formato de escritura:
    * "Mensaje entre comillas que mostrará", Int.String.Double::class.java, valor por defecto*/
    @Suppress("Unchecked_cast") // Elimina los warnings de las T
    fun <T> leerDato(mensaje: String, tipo: Class<T>, valorPorDefecto: T? = null): T {
        while (true) {
            val inputUsuario = input(mensaje)
            try {
                return when (tipo) {
                    String::class.java -> (inputUsuario ?: valorPorDefecto ?: "") as T
                    Int::class.java -> (inputUsuario?.toIntOrNull() ?: throw kotlin.Exception("No es un número entero")) as T
                    Double::class.java -> (inputUsuario?.toDoubleOrNull() ?: throw kotlin.Exception("No es un número válido")) as T
                    else -> throw kotlin.Exception("Tipo no soportado")
                }
            } catch (e: kotlin.Exception) {
                println("Error: ${e.message}. Por favor, introduce un valor válido.")
            }
        }
    }
}
val funcionesExtra = FuncionesExtra()
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

        println("=======================================================================================================")
        println(String.format(
            "%-20s %-8s %-15s %-15s %-10s %-15s",
            "Fecha", "Tipo", "Cerveza", "Tapa", "Cantidad", "Proveedor"
        ))
        println("-------------------------------------------------------------------------------------------------------")

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
                "%-20s %-8s %-15s %-15s %-10s %-15s",
                fecha, operacion, cerveza, tapa, cantidad, proveedor
            ))
        }

        println("=======================================================================================================")
    }
    fun sumarCervezas() {
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
        ?.getInteger("idRegistro") ?: (0 + 1)
}
private fun fechaActual(): String {
    return java.time.LocalDate.now().toString()
}
