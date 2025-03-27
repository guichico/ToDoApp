package com.apphico.designsystem.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun EmptyView(
    emptyMessage: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.large),
            text = emptyMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@PreviewLightDark
@Composable
private fun EmptyViewPreview(

) {
    ToDoAppTheme {
        EmptyView(
            emptyMessage = "Não há nenhuma tarefa cadastrada.\nClique no botão \"+\" para adicionar sua primeira tarefa."
        )
    }
}