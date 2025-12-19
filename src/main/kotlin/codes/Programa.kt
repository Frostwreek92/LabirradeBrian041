package org.example.codes

import java.io.File
import org.bson.Document
import org.json.JSONArray
import org.bson.json.JsonWriterSettings
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.example.codes.cerveza.funcionesCervezas
import org.example.codes.cerveza.variablesCerveza
import org.example.codes.proveedores.funcionesProveedores
import org.example.codes.proveedores.variablesProveedores
import org.example.codes.registro.funcionesRegistro
import org.example.codes.registro.variablesRegistro
import org.example.codes.tapas.funcionesTapas
import org.example.codes.tapas.variablesTapa
import java.lang.Exception
import kotlin.collections.contains

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
        } catch (e: Exception) {
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
    } catch (e: Exception) {
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
    } catch (e: Exception) {
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
    } catch (e: Exception) {
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
    } catch (e: Exception) {
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
    } catch (e: Exception) {
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
    } catch (e: Exception) {
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
