package com.apphico.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.ProgressBlue
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.extensions.formatDateAndTime
import com.apphico.extensions.getNowDateTime
import java.text.DecimalFormat
import java.time.LocalDateTime

@Composable
fun ProgressCard(
    date: LocalDateTime,
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
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = date.formatDateAndTime(),
                        style = dateStyle
                    )
                    description?.let { description ->
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = ToDoAppTheme.spacing.extraSmall,
                                    end = ToDoAppTheme.spacing.large
                                ),
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Box {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    trackColor = ProgressBlue.copy(alpha = 0.5f),
                    color = ProgressBlue,
                    progress = progress
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = ToDoAppTheme.spacing.extraSmall)
                        .align(Alignment.Center),
                    text = "${DecimalFormat("##.00").format(progress * 100)}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = White
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PercentageProgressCardPreview() {
    ToDoAppTheme {
        ProgressCard(
            date = getNowDateTime(),
            progressText = "1.00/30.00",
            description = "Some description of my progress. A very very very very very very very very very very very very very very very very long description.",
            progress = 0.5f,
            onClick = {}
        )
    }
}