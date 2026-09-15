package pe.edu.upeu.bibliomobil.domain.model

data class DetallePrestamo(
    val libro: Libro,
    val dias: Int
) {
    init {
        require(dias in 1..DIAS_MAXIMOS) {
            "El préstamo debe durar entre 1 y 15 días"
        }
    }

    fun multaPorRetraso(diasRetraso: Int): Double {
        return diasRetraso * MULTA_DIARIA
    }

    companion object {
        const val DIAS_MAXIMOS = 15
        const val MULTA_DIARIA = 1.50
    }
}