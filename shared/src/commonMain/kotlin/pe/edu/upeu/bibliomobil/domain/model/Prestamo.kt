package pe.edu.upeu.bibliomobil.domain.model

data class Prestamo(
    val id: Long,
    val lector: Lector,
    val detalles: List<DetallePrestamo>,
    val estado: EstadoPrestamo
)