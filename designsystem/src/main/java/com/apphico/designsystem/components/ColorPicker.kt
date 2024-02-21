package com.apphico.designsystem.components

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.GroupBlue
import com.apphico.designsystem.theme.GroupDarkBrown
import com.apphico.designsystem.theme.GroupGray
import com.apphico.designsystem.theme.GroupGreen
import com.apphico.designsystem.theme.GroupLightBrown
import com.apphico.designsystem.theme.GroupOrange
import com.apphico.designsystem.theme.GroupPink
import com.apphico.designsystem.theme.GroupRed
import com.apphico.designsystem.theme.GroupViolet
import com.apphico.designsystem.theme.GroupYellow
import com.apphico.designsystem.theme.ToDoAppTheme

private val colors = listOf(
    GroupBlue, GroupGreen, GroupRed, GroupViolet, GroupPink,
    GroupDarkBrown, GroupLightBrown, GroupOrange, GroupYellow, GroupGray
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

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ColorPickerPreview(
) {
    ToDoAppTheme {
        ColorPicker(
            selectedColor = remember { mutableStateOf(GroupGreen) },
            onColorSelected = {}
        )
    }
}