package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class ListarLibrosUseCase(
    private val repository: LibroRepository
) {

    suspend operator fun invoke(): Result<List<Libro>> = resultadoDe {
        repository.listar()
    }
}
