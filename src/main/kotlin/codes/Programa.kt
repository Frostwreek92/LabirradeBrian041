package org.example.codes

import java.lang.Exception

class Programa {
    fun iniciar() {
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
    }
}
val programa = Programa()

fun menuCRUD() {
    variables.salirMenuCRUD = false
    try {
        while (!variables.salirMenuCRUD) {
            val opcion = funcionesExtra.leerDato(variables.menuCRUD, Int::class.java)
            when (opcion) {
                1 -> funcionesCervezas.mostrarCervezas()
                2 -> funcionesCervezas.insertarCerveza()
                3 -> funcionesCervezas.actualizarCerveza()
                4 -> funcionesCervezas.eliminarCerveza()
                0 -> variables.salirMenuCRUD = funcionesExtra.finEleccion()
            }
        }
    } catch (e: Exception) {
        println("Excepción: $e")
    }
}

fun consultasAdicionales() {
    variables.salirConsultasAdicionales = false
    try {
        while (!variables.salirConsultasAdicionales) {
            val opcion = funcionesExtra.leerDato(variables.menuConsultasAdicionales, Int::class.java)
            when (opcion) {
                1 -> funcionesCervezas.consultarConFiltros()
                2 -> funcionesCervezas.consultarConProyeccion()
                3 -> funcionesCervezas.calcularPromedioGraduacion()
                4 -> funcionesCervezas.promedioPuntuacionPorTipo()
                5 -> funcionesCervezas.top3Cervezas()
                0 -> variables.salirConsultasAdicionales = funcionesExtra.finEleccion()
            }
        }
    } catch (e: Exception) {
        println("Excepción: $e")
    }
}
