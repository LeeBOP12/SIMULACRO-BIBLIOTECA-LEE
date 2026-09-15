package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class LectorRepositorioEnMemoria : LectorRepository {

    private val mutex = Mutex()
    private val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        delay(LATENCIA_MS)

        return mutex.withLock {
            val guardado = lector.copy(id = siguienteId++)
            lectores += guardado
            guardado
        }
    }

    override suspend fun listar(): List<Lector> {
        delay(LATENCIA_MS)

        return mutex.withLock {
            lectores.toList()
        }
    }

    private companion object {
        const val LATENCIA_MS = 400L
    }
}
