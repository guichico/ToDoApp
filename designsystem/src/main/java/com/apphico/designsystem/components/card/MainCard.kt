package com.apphico.designsystem.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.group.GroupIndicator
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun MainCard(
    modifier: Modifier = Modifier,
    isDone: Boolean,
    group: Group?,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    DefaultCard(
        modifier = modifier,
        enabled = !isDone,
        onClick = onClick
    ) {
        GroupIndicator(
            group = group
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(
                    start = ToDoAppTheme.spacing.extraSmall,
                    top = ToDoAppTheme.spacing.small,
                    end = ToDoAppTheme.spacing.medium,
                    bottom = ToDoAppTheme.spacing.small
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun MainCardPreview() {
    ToDoAppTheme {
        MainCard(
            isDone = false,
            group = mockedGroup,
            onClick = {}
        ) {}
    }
}