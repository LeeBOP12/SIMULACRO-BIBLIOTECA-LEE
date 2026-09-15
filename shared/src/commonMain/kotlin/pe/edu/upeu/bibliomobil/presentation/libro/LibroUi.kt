package pe.edu.upeu.bibliomobil.presentation.libro

import pe.edu.upeu.bibliomobil.domain.model.Libro

data class LibroUi(
    val id: Long,
    val titulo: String,
    val autor: String,
    val lineaSecundaria: String,
    val requiereReposicion: Boolean
)

fun Libro.aUi(): LibroUi {
    val etiquetaEjemplares = if (ejemplares == 1) "ejemplar" else "ejemplares"

    return LibroUi(
        id = id,
        titulo = titulo,
        autor = autor,
        lineaSecundaria = "$anio · $ejemplares $etiquetaEjemplares",
        requiereReposicion = requiereReposicion
    )
}
