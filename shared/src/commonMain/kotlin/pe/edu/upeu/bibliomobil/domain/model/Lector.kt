package pe.edu.upeu.bibliomobil.domain.model

data class Lector(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String?
) {
    init {
        require(nombre.isNotBlank()) { "El nombre es obligatorio" }
        require(correo.isNotBlank()) { "El correo es obligatorio" }
        require(telefono == null || telefono.isNotBlank()) {
            "El teléfono no puede estar vacío"
        }
    }
}