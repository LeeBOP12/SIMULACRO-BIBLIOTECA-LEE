package pe.edu.upeu.bibliomobil.domain.usecase

import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> resultadoDe(operacion: suspend () -> T): Result<T> {
    return try {
        Result.success(operacion())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Result.failure(error)
    }
}
