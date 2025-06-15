package com.apphico.designsystem.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun MainLazyList(
    listState: LazyListState,
    onAddClicked: () -> Unit,
    anchorViewHeight: State<Dp>,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
    content: LazyListScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        NestedScroll(
            anchorViewHeight = anchorViewHeight,
            isNestedViewExpanded = isNestedViewExpanded,
            onNestedViewClosed = onNestedViewClosed,
            nestedContent = nestedContent
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    start = ToDoAppTheme.spacing.medium,
                    top = ToDoAppTheme.spacing.medium,
                    end = ToDoAppTheme.spacing.medium,
                    bottom = 80.dp
                )
            ) {
                content()
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ToDoAppTheme.spacing.medium),
            containerColor = MaterialTheme.colorScheme.background,
            onClick = onAddClicked
        ) {
            ToDoAppIcon(
                icon = ToDoAppIcons.icAdd,
                contentDescription = "add",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MainLazyListPreview() {
    MainLazyList(
        listState = rememberLazyListState(),
        onAddClicked = {},
        anchorViewHeight = remember { mutableStateOf(342.dp) },
        isNestedViewExpanded = remember { mutableStateOf(false) },
        onNestedViewClosed = {},
        nestedContent = {}
    ) {
        val list = listOf("First", "Second", "Third")

        items(list) { item ->
            Text(item)
        }
    }
}