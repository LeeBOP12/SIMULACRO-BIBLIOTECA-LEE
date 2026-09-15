package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null
) {
    val hayErrores: Boolean
        get() = titulo != null || autor != null || anio != null || ejemplares != null
}

class LibroInvalidoException(
    val errores: ErroresDeLibro
) : IllegalArgumentException("El libro contiene datos inválidos")

class RegistrarLibroUseCase(
    private val repository: LibroRepository
) {

    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> = resultadoDe {
        val tituloRecortado = titulo.trim()
        val autorRecortado = autor.trim()
        val anioRecortado = anio.trim()
        val ejemplaresRecortados = ejemplares.trim()

        val anioConvertido = anioRecortado.toIntOrNull()
        val ejemplaresConvertidos = ejemplaresRecortados.toIntOrNull()

        val errores = ErroresDeLibro(
            titulo = if (tituloRecortado.isBlank()) "El título es obligatorio" else null,
            autor = if (autorRecortado.isBlank()) "El autor es obligatorio" else null,
            anio = validarAnio(anioRecortado, anioConvertido),
            ejemplares = validarEjemplares(ejemplaresRecortados, ejemplaresConvertidos)
        )

        if (errores.hayErrores) {
            throw LibroInvalidoException(errores)
        }

        repository.registrar(
            Libro(
                id = 0L,
                titulo = tituloRecortado,
                autor = autorRecortado,
                anio = anioConvertido!!,
                ejemplares = ejemplaresConvertidos!!
            )
        )
    }

    private fun validarAnio(valor: String, anio: Int?): String? {
        if (valor.isBlank()) return "El año es obligatorio"
        if (anio == null) return "El año debe ser un número entero"
        if (anio !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO) {
            return "El año debe estar entre ${Libro.ANIO_MINIMO} y ${Libro.ANIO_MAXIMO}"
        }
        return null
    }

    private fun validarEjemplares(valor: String, ejemplares: Int?): String? {
        if (valor.isBlank()) return "Los ejemplares son obligatorios"
        if (ejemplares == null) return "Los ejemplares deben ser un número entero"
        if (ejemplares < 0) return "Los ejemplares no pueden ser negativos"
        return null
    }
}
