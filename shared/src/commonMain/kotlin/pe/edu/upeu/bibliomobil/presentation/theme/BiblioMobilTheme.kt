package pe.edu.upeu.bibliomobil.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF315C72),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD3EAF4),
    onPrimaryContainer = Color(0xFF0B3446),
    secondary = Color(0xFF6D5E2E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF6E5A8),
    onSecondaryContainer = Color(0xFF3A3006),
    tertiary = Color(0xFF8A4D3C),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDAD0),
    onTertiaryContainer = Color(0xFF3A0B03),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFAFCFD),
    onBackground = Color(0xFF191C1E),
    surface = Color(0xFFFAFCFD),
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFDDE3E7),
    onSurfaceVariant = Color(0xFF41484D),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF3F7FA),
    surfaceContainer = Color(0xFFEDF1F4),
    surfaceContainerHigh = Color(0xFFE7ECEF),
    surfaceContainerHighest = Color(0xFFE1E6E9),
    outline = Color(0xFF71787D),
    outlineVariant = Color(0xFFC1C7CC),
    inverseSurface = Color(0xFF2E3133),
    inverseOnSurface = Color(0xFFF0F1F3),
    inversePrimary = Color(0xFF9FCFE4)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9FCFE4),
    onPrimary = Color(0xFF003546),
    primaryContainer = Color(0xFF174B60),
    onPrimaryContainer = Color(0xFFC4E7F5),
    secondary = Color(0xFFD9C98B),
    onSecondary = Color(0xFF393005),
    secondaryContainer = Color(0xFF534618),
    onSecondaryContainer = Color(0xFFF6E5A8),
    tertiary = Color(0xFFFFB59F),
    onTertiary = Color(0xFF542113),
    tertiaryContainer = Color(0xFF703729),
    onTertiaryContainer = Color(0xFFFFDAD0),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF111416),
    onBackground = Color(0xFFE1E3E5),
    surface = Color(0xFF111416),
    onSurface = Color(0xFFE1E3E5),
    surfaceVariant = Color(0xFF41484D),
    onSurfaceVariant = Color(0xFFC1C7CC),
    surfaceContainerLowest = Color(0xFF0C0F10),
    surfaceContainerLow = Color(0xFF191C1E),
    surfaceContainer = Color(0xFF1D2022),
    surfaceContainerHigh = Color(0xFF282A2D),
    surfaceContainerHighest = Color(0xFF333538),
    outline = Color(0xFF8B9297),
    outlineVariant = Color(0xFF41484D),
    inverseSurface = Color(0xFFE1E3E5),
    inverseOnSurface = Color(0xFF2E3133),
    inversePrimary = Color(0xFF315C72)
)

@Composable
fun BiblioMobilTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
