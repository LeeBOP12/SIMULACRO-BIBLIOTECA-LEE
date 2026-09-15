package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.ErroresDeLector
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun cargarLectores(mantenerMensaje: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fase = LectorUiState.Fase.Cargando,
                    mensajeExito = if (mantenerMensaje) it.mensajeExito else null
                )
            }

            listarLectores()
                .onSuccess { lectores ->
                    _uiState.update {
                        it.copy(
                            fase = if (lectores.isEmpty()) {
                                LectorUiState.Fase.SinLectores
                            } else {
                                LectorUiState.Fase.ConLectores(
                                    lectores.map { lector -> lector.aUi() }
                                )
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = LectorUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo cargar la cartera de lectores"
                            )
                        )
                    }
                }
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(nombre = valor, nombreError = null),
                mensajeExito = null
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(correo = valor, correoError = null),
                mensajeExito = null
            )
        }
    }

    fun onTelefonoChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(telefono = valor, telefonoError = null),
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

            registrarLector(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono
            ).onSuccess { lector ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioLector(),
                        registrando = false,
                        mensajeExito = "Lector \"${lector.nombre}\" registrado correctamente"
                    )
                }
                cargarLectores(mantenerMensaje = true)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = if (error is LectorInvalidoException) {
                            formulario.conErrores(error.errores)
                        } else {
                            formulario
                        },
                        fase = if (error is LectorInvalidoException) {
                            it.fase
                        } else {
                            LectorUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo cargar la cartera de lectores"
                            )
                        }
                    )
                }
            }
        }
    }

    private fun FormularioLector.conErrores(errores: ErroresDeLector): FormularioLector {
        return copy(
            nombreError = errores.nombre,
            correoError = errores.correo,
            telefonoError = errores.telefono
        )
    }
}
