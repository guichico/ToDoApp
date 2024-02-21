package com.apphico.designsystem.achievements

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Achievement
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.fakeData.mockedAchievement
import com.apphico.designsystem.components.card.DefaultCard
import com.apphico.designsystem.components.checklist.CheckList
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.DarkGray
import com.apphico.designsystem.theme.LightBlue
import com.apphico.designsystem.theme.MediumBlue
import com.apphico.designsystem.theme.RippleBlue
import com.apphico.designsystem.theme.SecondaryText
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.extensions.formatShortDate

@Composable
fun AchievementCard(
    achievement: Achievement,
    onClick: () -> Unit
) {
    DefaultCard(
        onClick = onClick
    ) {
        val nameStyle = if (achievement.getProgress() >= 1f) MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough)
        else MaterialTheme.typography.titleMedium

        /*
        Brush.linearGradient(
            0f to Black.copy(alpha = 0.8f), achievement.getProgress() to White
        )
        */

        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            achievement.group?.color?.let {
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
                    .padding(
                        top = ToDoAppTheme.spacing.small,
                        end = ToDoAppTheme.spacing.medium,
                        bottom = ToDoAppTheme.spacing.small
                    ),
            ) {
                Text(
                    text = achievement.name,
                    style = nameStyle,
                    color = MaterialTheme.colorScheme.primary
                )
                achievement.endDate?.let {
                    Text(
                        text = "até ${it.formatShortDate()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                achievement.doneDate?.let {
                    Text(
                        text = "concluído em ${it.formatShortDate()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (achievement.measurementType is MeasurementType.TaskDone) {
                    CheckList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = (-2).dp)
                            .padding(top = ToDoAppTheme.spacing.extraSmall),
                        checkList = (achievement.measurementType as MeasurementType.TaskDone).checkList,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
                if (achievement.getProgress() < 1f) {
                    LinearProgressIndicator(
                        progress = { achievement.getProgress() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .padding(top = ToDoAppTheme.spacing.small),
                        color = MediumBlue,
                        trackColor = LightBlue,
                        strokeCap = StrokeCap.Round,
                    )
                } else {
                    Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.extraSmall))
                }
            }
        }
    }
}

class AchievementCardPreviewProvider : PreviewParameterProvider<Achievement> {
    override val values = sequenceOf(mockedAchievement)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AchievementCardPreview(
    @PreviewParameter(AchievementCardPreviewProvider::class) achievement: Achievement
) {
    ToDoAppTheme {
        AchievementCard(
            achievement = achievement,
            onClick = {}
        )
    }
}