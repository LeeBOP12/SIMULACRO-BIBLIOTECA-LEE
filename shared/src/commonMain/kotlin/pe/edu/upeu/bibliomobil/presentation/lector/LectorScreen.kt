package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

@Composable
fun LectorScreen(
    viewModel: LectorViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    LectorScreen(
        estado = estado,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onRegistrar = viewModel::registrar,
        onReintentar = { viewModel.cargarLectores() },
        modifier = modifier
    )
}

@Composable
fun LectorScreen(
    estado: LectorUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EncabezadoLectores(estado.fase)
        }

        item {
            FormularioLectorCard(
                formulario = estado.formulario,
                registrando = estado.registrando,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onTelefonoChange = onTelefonoChange,
                onRegistrar = onRegistrar
            )
        }

        estado.mensajeExito?.let { mensaje ->
            item {
                MensajeExitoLector(mensaje)
            }
        }

        when (val fase = estado.fase) {
            LectorUiState.Fase.Cargando -> item {
                EstadoCargaLectores()
            }

            LectorUiState.Fase.SinLectores -> item {
                EstadoVacioLectores(
                    titulo = "Sin lectores",
                    descripcion = "Registra el primer lector para iniciar la cartera."
                )
            }

            is LectorUiState.Fase.ConLectores -> items(
                items = fase.lectores,
                key = { it.id }
            ) { lector ->
                LectorCard(lector)
            }

            is LectorUiState.Fase.Error -> item {
                EstadoVacioLectores(
                    titulo = "No se pudo cargar la cartera de lectores",
                    descripcion = fase.mensaje,
                    esError = true,
                    onReintentar = onReintentar
                )
            }
        }
    }
}

@Composable
private fun EncabezadoLectores(fase: LectorUiState.Fase) {
    val total = when (fase) {
        LectorUiState.Fase.Cargando,
        LectorUiState.Fase.SinLectores,
        is LectorUiState.Fase.Error -> 0
        is LectorUiState.Fase.ConLectores -> fase.lectores.size
    }
    val conteo = if (total == 1) "1 lector" else "$total lectores"

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Cartera de lectores",
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
private fun FormularioLectorCard(
    formulario: FormularioLector,
    registrando: Boolean,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
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
                text = "Registrar lector",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            CampoLector(
                valor = formulario.nombre,
                etiqueta = "Nombre",
                error = formulario.nombreError,
                onValorChange = onNombreChange
            )
            CampoLector(
                valor = formulario.correo,
                etiqueta = "Correo",
                error = formulario.correoError,
                keyboardType = KeyboardType.Email,
                onValorChange = onCorreoChange
            )
            CampoLector(
                valor = formulario.telefono,
                etiqueta = "Teléfono",
                error = formulario.telefonoError,
                keyboardType = KeyboardType.Phone,
                onValorChange = onTelefonoChange
            )
            Button(
                onClick = onRegistrar,
                enabled = !registrando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (registrando) "Registrando..." else "Registrar")
            }
        }
    }
}

@Composable
private fun CampoLector(
    valor: String,
    etiqueta: String,
    error: String?,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(error)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun EstadoCargaLectores() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(12.dp))
        Text("Cargando lectores")
    }
}

@Composable
private fun MensajeExitoLector(mensaje: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = mensaje,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EstadoVacioLectores(
    titulo: String,
    descripcion: String,
    esError: Boolean = false,
    onReintentar: (() -> Unit)? = null
) {
    val color = if (esError) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
        if (onReintentar != null) {
            Button(onClick = onReintentar) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun LectorCard(lector: LectorUi) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = lector.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = lector.correo,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = lector.telefono,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
