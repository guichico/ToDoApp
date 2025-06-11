package com.apphico.designsystem.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import java.time.LocalDate

@Composable
fun MainLazyList(
    listState: LazyListState,
    onAddClicked: () -> Unit,
    isCalendarExpanded: State<Boolean>,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        NestedScroll(
            anchorViewHeight = 342.dp,
            isCalendarExpanded = isCalendarExpanded,
            selectedDate = selectedDate,
            onSelectedDateChanged = onSelectedDateChanged,
            isFilterExpanded = isFilterExpanded,
            selectedStatus = selectedStatus,
            onStatusChanged = onStatusChanged,
            groups = groups,
            selectedGroups = selectedGroups,
            onGroupSelected = onGroupSelected,
            onSearchClicked = onSearchClicked
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
    /*
    MainLazyList(
        listState = rememberLazyListState(),
        onAddClicked = {}
    ) {
        val list = listOf("First", "Second", "Third")

        items(list) { item ->
            Text(item)
        }
    }
     */
}