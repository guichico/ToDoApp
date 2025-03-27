package com.apphico.designsystem.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun GroupIndicator(
    group: Group?
) {
    group?.color?.let {
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
}

@Preview
@Composable
private fun GroupIndicatorPreview() {
    ToDoAppTheme {
        GroupIndicator(
            group = mockedGroup
        )
    }
}