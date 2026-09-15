package pe.edu.upeu.bibliomobil.presentation.libro

data class LibroUiState(
    val fase: Fase = Fase.Cargando,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
) {

    sealed interface Fase {
        data object Cargando : Fase
        data object SinLibros : Fase
        data class ConLibros(val libros: List<LibroUi>) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

data class FormularioLibro(
    val titulo: String = "",
    val autor: String = "",
    val anio: String = "",
    val ejemplares: String = "",
    val tituloError: String? = null,
    val autorError: String? = null,
    val anioError: String? = null,
    val ejemplaresError: String? = null
) {

    fun sinErrores(): FormularioLibro {
        return copy(
            tituloError = null,
            autorError = null,
            anioError = null,
            ejemplaresError = null
        )
    }
}
