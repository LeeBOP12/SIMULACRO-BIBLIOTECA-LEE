package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/**
 * Define las operaciones de la cartera de lectores de la biblioteca.
 */
interface LectorRepository {

    /**
     * Registra un lector y devuelve el lector guardado.
     */
    suspend fun registrar(lector: Lector): Lector

    /**
     * Lista los lectores registrados en la cartera de lectores.
     */
    suspend fun listar(): List<Lector>
}