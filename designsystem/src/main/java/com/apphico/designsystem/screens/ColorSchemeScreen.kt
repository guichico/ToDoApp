package com.apphico.designsystem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.ToDoAppTheme

typealias ColorName = Pair<String, Color>

@Composable
fun ColorSchemeScreen() {
    val colors = listOf(
        ColorName("primary", MaterialTheme.colorScheme.primary),
        ColorName("primaryContainer", MaterialTheme.colorScheme.primaryContainer),
        ColorName("inversePrimary", MaterialTheme.colorScheme.inversePrimary),
        ColorName("onPrimary", MaterialTheme.colorScheme.onPrimary),
        ColorName("onPrimaryContainer", MaterialTheme.colorScheme.onPrimaryContainer),
        ColorName("secondary", MaterialTheme.colorScheme.secondary),
        ColorName("secondaryContainer", MaterialTheme.colorScheme.secondaryContainer),
        ColorName("onSecondary", MaterialTheme.colorScheme.onSecondary),
        ColorName("onSecondaryContainer", MaterialTheme.colorScheme.onSecondaryContainer),
        ColorName("tertiary", MaterialTheme.colorScheme.tertiary),
        ColorName("tertiaryContainer", MaterialTheme.colorScheme.tertiaryContainer),
        ColorName("onTertiary", MaterialTheme.colorScheme.onTertiary),
        ColorName("onTertiaryContainer", MaterialTheme.colorScheme.onTertiaryContainer),
        ColorName("background", MaterialTheme.colorScheme.background),
        ColorName("onBackground", MaterialTheme.colorScheme.onBackground),
        ColorName("surface", MaterialTheme.colorScheme.surface),
        ColorName("onSurface", MaterialTheme.colorScheme.onSurface),
        ColorName("surfaceVariant", MaterialTheme.colorScheme.surfaceVariant),
        ColorName("onSurfaceVariant", MaterialTheme.colorScheme.onSurfaceVariant),
        ColorName("surfaceTint", MaterialTheme.colorScheme.surfaceTint),
        ColorName("inverseSurface", MaterialTheme.colorScheme.inverseSurface),
        ColorName("inverseOnSurface", MaterialTheme.colorScheme.inverseOnSurface),
        ColorName("error", MaterialTheme.colorScheme.error),
        ColorName("onError", MaterialTheme.colorScheme.onError),
        ColorName("errorContainer", MaterialTheme.colorScheme.errorContainer),
        ColorName("onErrorContainer", MaterialTheme.colorScheme.onErrorContainer),
        ColorName("outline", MaterialTheme.colorScheme.outline),
        ColorName("outlineVariant", MaterialTheme.colorScheme.outlineVariant),
        ColorName("scrim", MaterialTheme.colorScheme.scrim)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(
            vertical = ToDoAppTheme.spacing.medium,
            horizontal = ToDoAppTheme.spacing.small
        )
    ) {
        items(colors) { (colorName, color) ->
            ColorRow(
                colorName = colorName,
                color = color
            )
        }
    }
}

@Composable
private fun ColorRow(
    colorName: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .padding(ToDoAppTheme.spacing.large)
            .fillMaxWidth()
    ) {
        Text(
            text = colorName
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = ToDoAppTheme.spacing.medium)
                .height(18.dp)
                .background(color = color)
        )
    }
}

@Preview
@Composable
private fun ColorSchemeScreenPreview(
) {
    ColorSchemeScreen()
}