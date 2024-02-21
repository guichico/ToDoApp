package com.apphico.todoapp.group

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.R
import com.apphico.designsystem.components.card.DefaultCard
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.Screen

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterSelectGroup() =
    when (initialState.destination.route) {
        Screen.AddEditTask.route,
        Screen.AddEditAchievement.route -> slideInVertically(initialOffsetY = { it })
        else -> fadeIn()
    }


fun AnimatedContentTransitionScope<NavBackStackEntry>.exitSelectGroup() =
    when (targetState.destination.route) {
        Screen.AddEditTask.route,
        Screen.AddEditAchievement.route -> slideOutVertically(targetOffsetY = { it })
        else -> fadeOut()
    }

fun NavController.navigateToSelectGroup() {
    this.navigate(Screen.SelectGroup.route)
}

/*

internal const val GROUP_ARG = "group"

fun NavController.navigateToSelectGroup(
    task: Task,
    group: Group?
) {
    navigateWithArgs(
        route = Screen.SelectGroup.route,
        args = bundleOf(
            TASK_ARG to task,
            GROUP_ARG to group
        ),
        navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
    )
}

fun NavController.navigateBackToSelectGroup(
    task: Task
) {
    navigateWithArgs(
        route = Screen.SelectGroup.route,
        args = bundleOf(
            TASK_ARG to task
        ),
        navOptions = NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(Screen.AddEditLocation.route, true).build()
    )
}
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroupScreen(
    selectGroupViewModel: SelectGroupViewModel = hiltViewModel(),
    navigateToAddEditGroup: () -> Unit,
    navigateBack: () -> Unit
) {
    val groups = selectGroupViewModel.groups.collectAsState()

    Scaffold(
        modifier = Modifier
            .consumeWindowInsets(WindowInsets.systemBars),
        topBar = {
            ToDoAppTopBar(
                navigateBack = navigateBack,
                title = stringResource(R.string.select_group),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            )
        }
    ) { innerPadding ->
        SelectGroupScreenContent(
            innerPadding = innerPadding,
            groups = groups,
            navigateToAddEditGroup = navigateToAddEditGroup
        )
    }
}

@Composable
fun SelectGroupScreenContent(
    innerPadding: PaddingValues,
    groups: State<List<Group>>,
    navigateToAddEditGroup: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding())
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
            items(groups.value) { group ->
                GroupRow(group)
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ToDoAppTheme.spacing.medium),
            onClick = navigateToAddEditGroup
        ) {
            ToDoAppIcon(
                icon = ToDoAppIcons.icAdd,
                contentDescription = "add"
            )
        }
    }
}

@Composable
fun GroupRow(
    group: Group
) {
    DefaultCard(
        onClick = { /*TODO*/ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .padding(start = ToDoAppTheme.spacing.extraLarge)
                    .aspectRatio(1f)
                    .background(Color(group.color), CircleShape)
                    .align(Alignment.CenterVertically)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(
                        vertical = ToDoAppTheme.spacing.extraLarge,
                        horizontal = ToDoAppTheme.spacing.large
                    ),
                text = group.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                modifier = Modifier
                    .padding(end = ToDoAppTheme.spacing.extraSmall)
                    .align(Alignment.CenterVertically),
                onClick = { /*TODO*/ }
            ) {
                ToDoAppIcon(
                    icon = ToDoAppIcons.icEdit,
                    contentDescription = "edit",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

class SelectGroupScreenPreviewProvider : PreviewParameterProvider<List<Group>> {
    override val values = sequenceOf(listOf(mockedGroup))
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun SelectGroupScreenPreview(
    @PreviewParameter(SelectGroupScreenPreviewProvider::class) groups: List<Group>
) {
    ToDoAppTheme {
        SelectGroupScreenContent(
            innerPadding = PaddingValues(),
            groups = remember { mutableStateOf(groups) },
            navigateToAddEditGroup = {}
        )
    }
}