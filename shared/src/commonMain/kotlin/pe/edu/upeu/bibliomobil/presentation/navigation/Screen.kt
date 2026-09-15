package pe.edu.upeu.bibliomobil.presentation.navigation

import androidx.compose.runtime.saveable.Saver

sealed class Screen(
    val ruta: String,
    val titulo: String
) {
    data object Inicio : Screen("inicio", "Inicio")
    data object Libros : Screen("libros", "Libros")
    data object Lectores : Screen("lectores", "Lectores")
    data object Prestamos : Screen("prestamos", "Prestamos")

    companion object {
        val Saver: Saver<Screen, String> = Saver(
            save = { it.ruta },
            restore = { ruta -> fromRuta(ruta) }
        )

        fun fromRuta(ruta: String): Screen {
            return when (ruta) {
                Libros.ruta -> Libros
                Lectores.ruta -> Lectores
                Prestamos.ruta -> Prestamos
                else -> Inicio
            }
        }
    }
}

val DESTINOS = listOf(
    Screen.Inicio,
    Screen.Libros,
    Screen.Lectores,
    Screen.Prestamos
)
