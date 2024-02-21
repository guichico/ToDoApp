package com.apphico.designsystem.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apphico.core_model.FocusMode
import com.apphico.designsystem.components.card.DefaultCard
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatHours

@Composable
fun FocusCard(
    focus: FocusMode,
    onClick: () -> Unit
) {
    DefaultCard(
        onClick = onClick
    ) {
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            focus.group?.color?.let {
                Box(
                    modifier = Modifier
                        .padding(ToDoAppTheme.spacing.small),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(8.dp)
                            .background(color = Color(it), shape = CircleShape)
                    )
                }
            } ?: Spacer(modifier = Modifier.width(ToDoAppTheme.spacing.medium))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .padding(
                        top = ToDoAppTheme.spacing.small,
                        end = ToDoAppTheme.spacing.medium,
                        bottom = ToDoAppTheme.spacing.small
                    )
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = focus.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = ToDoAppTheme.spacing.extraSmall)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text = "Tempo de foco",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier,
                            text = focus.timer.formatHours(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text = "Intervalo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier,
                            text = focus.interval.formatHours(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}