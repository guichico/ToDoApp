package com.apphico.designsystem.components.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.designsystem.components.date.CalendarView
import com.apphico.designsystem.views.FilterView
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun NestedScroll(
    anchorViewHeight: Dp,
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
    content: @Composable () -> Unit
) {
    val nestedScrollConnection = rememberNestedScrollConnection(anchorViewHeight)
    val anchorViewOffsetHeightPx = nestedScrollConnection.anchorViewOffsetHeightPx.floatValue

    val offsetY by animateDpAsState(anchorViewOffsetHeightPx.roundToInt().dp)
    val padding by animateDpAsState(
        (anchorViewHeight - (anchorViewOffsetHeightPx.dp * -1))
            .takeIf { it.value > 0 && isCalendarExpanded.value } ?: 0.dp
    )

    LaunchedEffect(isCalendarExpanded.value) {
        nestedScrollConnection.anchorViewOffsetHeightPx.floatValue = if (isCalendarExpanded.value) 0f else (anchorViewHeight.value * -1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        val modifier = Modifier
            .height(anchorViewHeight)
            .offset(y = offsetY)
            .shadow(elevation = 8.dp, spotColor = Color.Transparent)

        CalendarView(
            modifier = modifier,
            isCalendarExpanded = isCalendarExpanded,
            selectedDate = selectedDate,
            onSelectedDateChanged = onSelectedDateChanged
        )

        FilterView(
            modifier = modifier,
            isFilterExpanded = isFilterExpanded,
            selectedStatus = selectedStatus,
            onStatusChanged = onStatusChanged,
            groups = groups,
            selectedGroups = selectedGroups,
            onGroupSelected = onGroupSelected,
            onSearchClicked = onSearchClicked
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding)
        ) {
            content()
        }
    }
}