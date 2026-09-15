package pe.edu.upeu.bibliomobil.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EstadoVacio(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    esError: Boolean = false,
    textoAccion: String? = null,
    onAccion: (() -> Unit)? = null
) {
    val color = if (esError) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
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
        if (textoAccion != null && onAccion != null) {
            Button(onClick = onAccion) {
                Text(textoAccion)
            }
        }
    }
}
