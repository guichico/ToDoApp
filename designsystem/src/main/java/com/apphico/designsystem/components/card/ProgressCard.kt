package com.apphico.designsystem.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.components.progress.ProgressBar
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatLongDate
import com.apphico.extensions.formatShortTime
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ProgressCard(
    date: LocalDate?,
    time: LocalTime?,
    progressText: String? = null,
    description: String?,
    progress: Float,
    onClick: () -> Unit,
) {
    DefaultSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ToDoAppTheme.spacing.small),
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = ToDoAppTheme.spacing.large)
            ) {
                val dateStyle = progressText?.let { MaterialTheme.typography.bodySmall } ?: MaterialTheme.typography.titleMedium

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = ToDoAppTheme.spacing.medium),
                ) {
                    progressText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (date != null || time != null) {
                        Text(
                            text = "${date?.formatLongDate() ?: ""} ${time?.formatShortTime() ?: ""}",
                            style = dateStyle,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    description?.let { description ->
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = ToDoAppTheme.spacing.extraSmall,
                                    end = ToDoAppTheme.spacing.large
                                ),
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            ProgressBar(
                progress = { progress }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PercentageProgressCardPreview() {
    ToDoAppTheme {
        ProgressCard(
            date = getNowDate(),
            time = getNowTime(),
            progressText = "1.00/30.00",
            description = "Some description of my progress. A very very very very very very very very very very very very very very very very long description.",
            progress = 0.5f,
            onClick = {}
        )
    }
}