package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**
 * Define las operaciones del catálogo de libros de la biblioteca.
 */
interface LibroRepository {

    /**
     * Registra un libro en el catálogo y devuelve el libro guardado.
     */
    suspend fun registrar(libro: Libro): Libro

    /**
     * Lista los libros registrados en el catálogo.
     */
    suspend fun listar(): List<Libro>
}