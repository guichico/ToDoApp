package com.apphico.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = DarkerGray,
    primaryContainer = DarkSecondary,
    inversePrimary = Black,

    secondary = LightBlue,
    secondaryContainer = DarkerGray,

    tertiary = DarkerGray,
    tertiaryContainer = DarkerGray,

    background = DarkMain,
    onBackground = DarkGray,

    surface = DarkMain,
    inverseSurface = LightGray,
    surfaceVariant = DarkMain,
    surfaceTint = DarkMain
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = White,
    inversePrimary = White,

    secondary = MainText,
    secondaryContainer = MainContainer,

    tertiary = SecondaryText,
    tertiaryContainer = MediumBlue,

    background = White,
    onBackground = LightBlue,

    surface = White,
    inverseSurface = DarkGray,
    surfaceVariant = White,
    surfaceTint = White
)

object ToDoAppTheme {
    val spacing: Spacings
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic color is available on Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(
            LocalSpacing provides ToDoAppTheme.spacing,
            // LocalRippleConfiguration provides toDoAppRippleTheme(RippleBlue) // TODO Check it
        ) {
            content()
        }
    }
}