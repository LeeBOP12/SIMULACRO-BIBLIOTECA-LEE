package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import pe.edu.upeu.bibliomobil.presentation.component.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.navigation.DESTINOS
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            var pantallaActual by rememberSaveable(stateSaver = Screen.Saver) {
                mutableStateOf(Screen.Inicio)
            }
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Text(
                            text = "BiblioMobil",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        DESTINOS.forEach { destino ->
                            NavigationDrawerItem(
                                label = { Text(destino.titulo) },
                                selected = destino == pantallaActual,
                                onClick = {
                                    pantallaActual = destino
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(pantallaActual.titulo) },
                            navigationIcon = {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } }
                                ) {
                                    Text("☰")
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        when (pantallaActual) {
                            Screen.Inicio -> InicioScreen(
                                destinos = DESTINOS,
                                onDestinoClick = { pantallaActual = it }
                            )

                            Screen.Libros -> LibroScreen()
                            Screen.Lectores -> LectorScreen()
                            Screen.Prestamos -> EstadoVacio(
                                titulo = "Prestamos",
                                descripcion = "RF-05 queda preparado para registrar prestamos en la siguiente iteracion."
                            )
                        }
                    }
                }
            }
        }
    }
}
