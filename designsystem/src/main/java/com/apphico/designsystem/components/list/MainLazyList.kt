package com.apphico.designsystem.components.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.components.date.CalendarView
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun MainLazyList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    onAddClicked: () -> Unit,
    content: LazyListScope.() -> Unit
) {

    val toolbarHeight = 342.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    // our offset to collapse toolbar
    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    // now, let's create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }

    // TODO Put in Scaffold
    val isCalendarExpanded = remember { mutableStateOf(true) }

    LaunchedEffect(isCalendarExpanded.value) {
        toolbarOffsetHeightPx.floatValue = if (isCalendarExpanded.value) 0f else (toolbarHeight.value * -1)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            val offsetY by animateDpAsState(toolbarOffsetHeightPx.floatValue.roundToInt().dp)
            val padding by animateDpAsState(
                (toolbarHeight - (toolbarOffsetHeightPx.floatValue.dp * -1))
                    .takeIf { it.value > 0 && isCalendarExpanded.value } ?: 0.dp
            )

            CalendarView(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset(y = offsetY)
                    // TODO Check shadow in TopBar
                    .shadow(elevation = 8.dp, spotColor = Color.Transparent),
                isCalendarExpanded = isCalendarExpanded,
                selectedDate = remember { mutableStateOf(LocalDate.now()) },
                onSelectedDateChanged = { }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding),
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
        onAddClicked = {}
    ) {
        val list = listOf("First", "Second", "Third")

        items(list) { item ->
            Text(item)
        }
    }
}