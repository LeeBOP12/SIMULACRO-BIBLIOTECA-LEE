package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.presentation.component.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.component.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.component.ValidatedTextField

@Composable
fun LibroScreen(
    viewModel: LibroViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    LibroScreen(
        estado = estado,
        onTituloChange = viewModel::onTituloChange,
        onAutorChange = viewModel::onAutorChange,
        onAnioChange = viewModel::onAnioChange,
        onEjemplaresChange = viewModel::onEjemplaresChange,
        onRegistrar = viewModel::registrar,
        onReintentar = { viewModel.cargarLibros() },
        modifier = modifier
    )
}

@Composable
fun LibroScreen(
    estado: LibroUiState,
    onTituloChange: (String) -> Unit,
    onAutorChange: (String) -> Unit,
    onAnioChange: (String) -> Unit,
    onEjemplaresChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EncabezadoLibros(estado.fase)
        }

        item {
            FormularioLibroCard(
                formulario = estado.formulario,
                registrando = estado.registrando,
                onTituloChange = onTituloChange,
                onAutorChange = onAutorChange,
                onAnioChange = onAnioChange,
                onEjemplaresChange = onEjemplaresChange,
                onRegistrar = onRegistrar
            )
        }

        estado.mensajeExito?.let { mensaje ->
            item {
                MensajeExito(mensaje)
            }
        }

        when (val fase = estado.fase) {
            LibroUiState.Fase.Cargando -> item {
                EstadoCargaLibros()
            }

            LibroUiState.Fase.SinLibros -> item {
                EstadoVacio(
                    titulo = "Sin libros",
                    descripcion = "Registra el primer libro para iniciar el catálogo.",
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }

            is LibroUiState.Fase.ConLibros -> items(
                items = fase.libros,
                key = { it.id }
            ) { libro ->
                LibroCard(libro)
            }

            is LibroUiState.Fase.Error -> item {
                EstadoVacio(
                    titulo = "No se pudo cargar el catálogo",
                    descripcion = fase.mensaje,
                    esError = true,
                    textoAccion = "Reintentar",
                    onAccion = onReintentar,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun EncabezadoLibros(fase: LibroUiState.Fase) {
    val total = when (fase) {
        LibroUiState.Fase.Cargando,
        LibroUiState.Fase.SinLibros,
        is LibroUiState.Fase.Error -> 0
        is LibroUiState.Fase.ConLibros -> fase.libros.size
    }
    val conteo = if (total == 1) "1 libro" else "$total libros"

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Catálogo de libros",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = conteo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FormularioLibroCard(
    formulario: FormularioLibro,
    registrando: Boolean,
    onTituloChange: (String) -> Unit,
    onAutorChange: (String) -> Unit,
    onAnioChange: (String) -> Unit,
    onEjemplaresChange: (String) -> Unit,
    onRegistrar: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Registrar libro",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            ValidatedTextField(
                valor = formulario.titulo,
                etiqueta = "Título",
                error = formulario.tituloError,
                onValorChange = onTituloChange
            )
            ValidatedTextField(
                valor = formulario.autor,
                etiqueta = "Autor",
                error = formulario.autorError,
                onValorChange = onAutorChange
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ValidatedTextField(
                    valor = formulario.anio,
                    etiqueta = "Año",
                    error = formulario.anioError,
                    keyboardType = KeyboardType.Number,
                    onValorChange = onAnioChange,
                    modifier = Modifier.weight(1f)
                )
                ValidatedTextField(
                    valor = formulario.ejemplares,
                    etiqueta = "Ejemplares",
                    error = formulario.ejemplaresError,
                    keyboardType = KeyboardType.Number,
                    onValorChange = onEjemplaresChange,
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = onRegistrar,
                enabled = !registrando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (registrando) "Registrando…" else "Registrar")
            }
        }
    }
}

@Composable
private fun EstadoCargaLibros() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(12.dp))
        Text("Cargando catálogo")
    }
}


@Composable
private fun LibroCard(libro: LibroUi) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = libro.autor,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = libro.lineaSecundaria,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (libro.requiereReposicion) {
                AssistChip(
                    onClick = {},
                    label = { Text("Pocos ejemplares") }
                )
            }
        }
    }
}
