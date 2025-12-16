package org.example.codes

import kotlin.jvm.java
import kotlin.text.toDoubleOrNull
import kotlin.text.toIntOrNull

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
            } catch (e: Exception) {
                println("Error: ${e.message}. Por favor, introduce un valor válido.")
            }
        }
    }
}
val funcionesExtra = FuncionesExtra()
