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
import java.lang.Exception
import kotlin.collections.contains

//BD y colección con la que se trabajará
const val NOM_BD = "LaBirradeBrian"
const val NOM_COLECCION = "cervezas"

//variables globales definidas sin inicializar
lateinit var servidor: MongoServer
lateinit var cliente: MongoClient
lateinit var uri: String
lateinit var coleccionCervezas: MongoCollection<Document>

class Programa {
    fun iniciar() {
        conectarBD()
        importarBD(variablesCerveza.rutaJSON, coleccionCervezas)
        variables.titulo
        try {
            while (!variables.salirMenuInicial) {
                val opcion = funcionesExtra.leerDato(variables.menuInicial, Int::class.java)
                when (opcion) {
                    1 -> menuCRUDCervezas()
                    2 -> consultasAdicionalesCervezas()
                    0 -> variables.salirMenuInicial = funcionesExtra.finEleccion()
                    else -> println("Introduce una de las opciones")
                }
            }
        } catch (e: Exception) {
            println("Excepción: $e")
        }
        variables.finPrograma
        exportarBD(variablesCerveza.rutaJSON, coleccionCervezas)
        desconectarBD()
    }
}
val programa = Programa()

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

// Función para conectar a la BD
fun conectarBD() {
    servidor = MongoServer(MemoryBackend())
    val address = servidor.bind()
    uri = "mongodb://${address.hostName}:${address.port}"
    cliente = MongoClients.create(uri)
    coleccionCervezas = cliente.getDatabase(NOM_BD).getCollection(NOM_COLECCION)
    println("Servidor MongoDB en memoria iniciado en $uri")
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
