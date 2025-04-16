package com.apphico.designsystem.achievements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.fakeData.mockedAchievement
import com.apphico.designsystem.R
import com.apphico.designsystem.components.card.MainCard
import com.apphico.designsystem.components.checklist.CheckList
import com.apphico.designsystem.components.text.LineThroughText
import com.apphico.designsystem.theme.LightBlue
import com.apphico.designsystem.theme.MediumBlue
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatShortDate
import java.time.LocalDate

@Composable
fun AchievementCard(
    achievement: Achievement,
    onClick: () -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit
) {
    val isDone = achievement.getProgress() >= 1f

    MainCard(
        isDone = isDone,
        group = achievement.group,
        onClick = onClick
    ) {
        LineThroughText(
            text = achievement.name,
            isLineThrough = isDone
        )
        DatesColumn(
            achievement = achievement
        )
        if (achievement.measurementType is MeasurementType.TaskDone) {
            CheckList(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-2).dp)
                    .padding(top = ToDoAppTheme.spacing.extraSmall),
                checkList = (achievement.measurementType as MeasurementType.TaskDone).checkList,
                parentDate = achievement.endDate,
                textColor = MaterialTheme.colorScheme.primary,
                onCheckListItemDoneChanged = { checkListItem, isDone ->
                    onCheckListItemDoneChanged(checkListItem, achievement.endDate, isDone)
                }
            )
        }
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .padding(top = ToDoAppTheme.spacing.small),
            progress = { achievement.getProgress() },
            color = MediumBlue,
            trackColor = LightBlue,
            strokeCap = StrokeCap.Round,
            gapSize = (-15).dp,
            drawStopIndicator = {}
        )
    }
}

@Composable
private fun DatesColumn(
    achievement: Achievement
) {
    achievement.endDate?.let {
        Text(
            text = "${stringResource(R.string.until)} ${it.formatShortDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    achievement.doneDate?.let {
        Text(
            text = "${stringResource(R.string.completed_in)} ${it.formatShortDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

class AchievementCardPreviewProvider : PreviewParameterProvider<Achievement> {
    override val values = sequenceOf(mockedAchievement)
}

@PreviewLightDark
@Composable
private fun AchievementCardPreview(
    @PreviewParameter(AchievementCardPreviewProvider::class) achievement: Achievement
) {
    ToDoAppTheme {
        AchievementCard(
            achievement = achievement,
            onClick = {},
            onCheckListItemDoneChanged = { _, _, _ -> }
        )
    }
}