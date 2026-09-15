package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen

@Composable
fun InicioScreen(
    destinos: List<Screen>,
    onDestinoClick: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val accesosRapidos = destinos.filterNot { it == Screen.Inicio }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Bienvenido a BiblioMobil",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "RF-01 centraliza el acceso a los modulos principales de la biblioteca.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(accesosRapidos, key = { it.ruta }) { destino ->
            AccesoRapidoCard(
                destino = destino,
                onClick = { onDestinoClick(destino) }
            )
        }
    }
}

@Composable
private fun AccesoRapidoCard(
    destino: Screen,
    onClick: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = destino.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = descripcionDe(destino),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(onClick = onClick) {
                Text("Abrir")
            }
        }
    }
}

private fun descripcionDe(destino: Screen): String {
    return when (destino) {
        Screen.Libros -> "Consulta y registra libros disponibles."
        Screen.Lectores -> "Administra los lectores registrados."
        Screen.Prestamos -> "Revisa el modulo reservado para prestamos."
        Screen.Inicio -> "Inicio"
    }
}
