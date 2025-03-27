package com.apphico.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.GroupDarkBlue
import com.apphico.designsystem.theme.GroupDarkBrown
import com.apphico.designsystem.theme.GroupDarkGray
import com.apphico.designsystem.theme.GroupDarkGreen
import com.apphico.designsystem.theme.GroupDarkRed
import com.apphico.designsystem.theme.GroupDarkViolet
import com.apphico.designsystem.theme.GroupLightBlue
import com.apphico.designsystem.theme.GroupLightBrown
import com.apphico.designsystem.theme.GroupLightGray
import com.apphico.designsystem.theme.GroupLightGreen
import com.apphico.designsystem.theme.GroupLightRed
import com.apphico.designsystem.theme.GroupLightViolet
import com.apphico.designsystem.theme.GroupOrange
import com.apphico.designsystem.theme.GroupPink
import com.apphico.designsystem.theme.GroupYellow
import com.apphico.designsystem.theme.ToDoAppTheme

private val colors = listOf(
    GroupPink,
    GroupLightRed,
    GroupDarkRed,
    GroupLightViolet,
    GroupDarkViolet,
    GroupLightBlue,
    GroupDarkBlue,
    GroupLightGreen,
    GroupDarkGreen,
    GroupYellow,
    GroupOrange,
    GroupLightBrown,
    GroupDarkBrown,
    GroupLightGray,
    GroupDarkGray
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    selectedColor: State<Color?>,
    onColorSelected: (Color) -> Unit
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.medium),
        maxItemsInEachRow = 5
    ) {
        colors.forEach { color ->
            var colorModifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(CircleShape)

            if (color == selectedColor.value) {
                colorModifier = colorModifier.border(
                    shape = CircleShape,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Row(
                modifier = colorModifier
                    .clickable { onColorSelected(color) }
                    .padding(ToDoAppTheme.spacing.medium)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .background(color, CircleShape)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ColorPickerPreview(
) {
    ToDoAppTheme {
        ColorPicker(
            selectedColor = remember { mutableStateOf(GroupLightGreen) },
            onColorSelected = {}
        )
    }
}