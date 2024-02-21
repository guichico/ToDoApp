package com.apphico.todoapp.calendar

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTasks
import com.apphico.designsystem.task.TaskCard
import com.apphico.designsystem.theme.LightBlue
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.BottomBarNavigationItem
import com.apphico.todoapp.navigation.DEFAULT_TWEEN_ANIMATION
import com.apphico.todoapp.navigation.enterToEnd
import com.apphico.todoapp.navigation.enterToStart
import com.apphico.todoapp.navigation.exitToEnd
import com.apphico.todoapp.navigation.exitToStart

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterCalendar() =
    when (initialState.destination.route) {
        BottomBarNavigationItem.FOCUS.route -> enterToStart()
        BottomBarNavigationItem.ACHIEVEMENT.route -> enterToEnd()
        else -> fadeIn(tween(DEFAULT_TWEEN_ANIMATION))
    }

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitCalendar() =
    when (targetState.destination.route) {
        BottomBarNavigationItem.FOCUS.route -> exitToEnd()
        BottomBarNavigationItem.ACHIEVEMENT.route -> exitToStart()
        else -> fadeOut(tween(DEFAULT_TWEEN_ANIMATION))
    }

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    navigateToAddEditTask: (Task?) -> Unit
) {
    val calendar = calendarViewModel.calendar.collectAsState()

    CalendarScreenContent(
        tasks = calendar,
        navigateToAddEditTask = navigateToAddEditTask
    )
}

@Composable
private fun CalendarScreenContent(
    tasks: State<List<Task>>,
    navigateToAddEditTask: (Task?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(
                start = ToDoAppTheme.spacing.medium,
                top = ToDoAppTheme.spacing.medium,
                end = ToDoAppTheme.spacing.medium,
                bottom = 80.dp
            )
        ) {
            items(tasks.value) {
                TaskCard(
                    task = it,
                    onClick = { navigateToAddEditTask(it) }
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ToDoAppTheme.spacing.medium),
            onClick = { navigateToAddEditTask(null) }
        ) {
            ToDoAppIcon(
                icon = ToDoAppIcons.icAdd,
                contentDescription = "add"
            )
        }
    }
}

class CalendarScreenPreviewProvider : PreviewParameterProvider<List<Task>> {
    override val values = sequenceOf(mockedTasks)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun CalendarScreenPreview(
    @PreviewParameter(CalendarScreenPreviewProvider::class) tasks: List<Task>
) {
    ToDoAppTheme {
        CalendarScreenContent(
            tasks = remember { mutableStateOf(tasks) },
            navigateToAddEditTask = {}
        )
    }
}
