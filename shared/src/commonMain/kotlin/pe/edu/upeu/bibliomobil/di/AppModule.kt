package pe.edu.upeu.bibliomobil.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.core.context.startKoin
import pe.edu.upeu.bibliomobil.data.repository.LectorRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel

val dataModule = module {
    single<LibroRepository> { LibroRepositorioEnMemoria() }
    single<LectorRepository> { LectorRepositorioEnMemoria() }
}

val domainModule = module {
    factory { RegistrarLibroUseCase(get()) }
    factory { ListarLibrosUseCase(get()) }
    factory { RegistrarLectorUseCase(get()) }
    factory { ListarLectoresUseCase(get()) }
}

val presentationModule = module {
    viewModel { LibroViewModel(get(), get()) }
    viewModel { LectorViewModel(get(), get()) }
}

expect val platformModule: Module

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(
            platformModule,
            dataModule,
            domainModule,
            presentationModule
        )
    }
}
