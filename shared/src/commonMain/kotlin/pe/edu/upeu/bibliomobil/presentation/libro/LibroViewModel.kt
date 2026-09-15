package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.ErroresDeLibro
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros(mantenerMensaje: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fase = LibroUiState.Fase.Cargando,
                    mensajeExito = if (mantenerMensaje) it.mensajeExito else null
                )
            }

            listarLibros()
                .onSuccess { libros ->
                    _uiState.update {
                        it.copy(
                            fase = if (libros.isEmpty()) {
                                LibroUiState.Fase.SinLibros
                            } else {
                                LibroUiState.Fase.ConLibros(libros.map { libro -> libro.aUi() })
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = LibroUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo cargar el catálogo"
                            )
                        )
                    }
                }
        }
    }

    fun onTituloChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(titulo = valor, tituloError = null),
                mensajeExito = null
            )
        }
    }

    fun onAutorChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(autor = valor, autorError = null),
                mensajeExito = null
            )
        }
    }

    fun onAnioChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(anio = valor, anioError = null),
                mensajeExito = null
            )
        }
    }

    fun onEjemplaresChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(ejemplares = valor, ejemplaresError = null),
                mensajeExito = null
            )
        }
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        viewModelScope.launch {
            val formulario = _uiState.value.formulario

            _uiState.update {
                it.copy(
                    registrando = true,
                    formulario = formulario.sinErrores(),
                    mensajeExito = null
                )
            }

            registrarLibro(
                titulo = formulario.titulo,
                autor = formulario.autor,
                anio = formulario.anio,
                ejemplares = formulario.ejemplares
            ).onSuccess { libro ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioLibro(),
                        registrando = false,
                        mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente"
                    )
                }
                cargarLibros(mantenerMensaje = true)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = if (error is LibroInvalidoException) {
                            formulario.conErrores(error.errores)
                        } else {
                            formulario
                        },
                        fase = if (error is LibroInvalidoException) {
                            it.fase
                        } else {
                            LibroUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo cargar el catálogo"
                            )
                        }
                    )
                }
            }
        }
    }

    private fun FormularioLibro.conErrores(errores: ErroresDeLibro): FormularioLibro {
        return copy(
            tituloError = errores.titulo,
            autorError = errores.autor,
            anioError = errores.anio,
            ejemplaresError = errores.ejemplares
        )
    }
}
