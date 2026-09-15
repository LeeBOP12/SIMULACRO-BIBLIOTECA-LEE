package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class ListarLectoresUseCase(
    private val repository: LectorRepository
) {

    suspend operator fun invoke(): Result<List<Lector>> = resultadoDe {
        repository.listar()
    }
}
