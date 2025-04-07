package com.apphico.designsystem.components.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun SnackBar(
    text: String,
    endPadding: Dp,
) {
    Snackbar(
        modifier = Modifier
            .padding(
                start = ToDoAppTheme.spacing.large,
                top = ToDoAppTheme.spacing.large,
                end = endPadding,
                bottom = ToDoAppTheme.spacing.large
            ),
        shape = CardDefaults.shape
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = ToDoAppTheme.spacing.extraSmall,
                    bottom = ToDoAppTheme.spacing.extraSmall
                ),
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun SnackBarPreview(
) {
    SnackBar(
        text = "Some message",
        endPadding = ToDoAppTheme.spacing.large
    )
}