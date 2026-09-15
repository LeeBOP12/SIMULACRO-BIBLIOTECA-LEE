package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(
    val errores: ErroresDeLector
) : IllegalArgumentException("El lector contiene datos inválidos")

class RegistrarLectorUseCase(
    private val repository: LectorRepository
) {

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String
    ): Result<Lector> = resultadoDe {
        val nombreRecortado = nombre.trim()
        val correoRecortado = correo.trim()
        val telefonoRecortado = telefono.trim()
        val telefonoNormalizado = telefonoRecortado.ifBlank { null }

        val errores = ErroresDeLector(
            nombre = if (nombreRecortado.isBlank()) "El nombre es obligatorio" else null,
            correo = validarCorreo(correoRecortado),
            telefono = validarTelefono(telefonoNormalizado)
        )

        if (errores.hayErrores) {
            throw LectorInvalidoException(errores)
        }

        repository.registrar(
            Lector(
                id = 0L,
                nombre = nombreRecortado,
                correo = correoRecortado,
                telefono = telefonoNormalizado
            )
        )
    }

    private fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) return "El correo es obligatorio"
        if (!correo.matches(CORREO_REGEX)) return "El correo no tiene un formato válido"
        return null
    }

    private fun validarTelefono(telefono: String?): String? {
        if (telefono == null) return null
        if (!telefono.matches(TELEFONO_REGEX)) return "El teléfono debe tener entre 6 y 9 dígitos"
        return null
    }

    private companion object {
        val CORREO_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        val TELEFONO_REGEX = Regex("^\\d{6,9}$")
    }
}
