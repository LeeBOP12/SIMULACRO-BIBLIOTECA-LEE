package pe.edu.upeu.bibliomobil.domain.model

data class Libro(
    val id: Long,
    val titulo: String,
    val autor: String,
    val anio: Int,
    val ejemplares: Int
) {
    init {
        require(titulo.isNotBlank()) { "El título es obligatorio" }
        require(autor.isNotBlank()) { "El autor es obligatorio" }
        require(anio in ANIO_MINIMO..ANIO_MAXIMO) {
            "El año debe estar entre $ANIO_MINIMO y $ANIO_MAXIMO"
        }
        require(ejemplares >= 0) { "Los ejemplares no pueden ser negativos" }
    }

    val requiereReposicion: Boolean
        get() = ejemplares < EJEMPLARES_MINIMOS

    companion object {
        const val EJEMPLARES_MINIMOS = 3
        const val ANIO_MINIMO = 1450
        const val ANIO_MAXIMO = 2026
    }
}