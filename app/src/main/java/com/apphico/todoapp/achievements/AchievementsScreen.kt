package com.apphico.todoapp.achievements

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.apphico.core_model.Achievement
import com.apphico.core_model.fakeData.mockedAchievements
import com.apphico.designsystem.achievements.AchievementCard
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.BottomBarNavigationItem
import com.apphico.todoapp.navigation.DEFAULT_TWEEN_ANIMATION
import com.apphico.todoapp.navigation.enterToStart
import com.apphico.todoapp.navigation.exitToEnd

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterAchievements() =
    when (initialState.destination.route) {
        BottomBarNavigationItem.CALENDAR.route,
        BottomBarNavigationItem.FOCUS.route -> enterToStart()

        else -> fadeIn(tween(DEFAULT_TWEEN_ANIMATION))
    }

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitAchievements() =
    when (targetState.destination.route) {
        BottomBarNavigationItem.CALENDAR.route,
        BottomBarNavigationItem.FOCUS.route -> exitToEnd()

        else -> fadeOut(tween(DEFAULT_TWEEN_ANIMATION))
    }

@Composable
fun AchievementsScreen(
    achievementsViewModel: AchievementsViewModel = hiltViewModel(),
    navigateToAddEditAchievement: (Achievement?) -> Unit
) {
    val achievements = remember { mutableStateOf(mockedAchievements) }

    AchievementsScreenContent(
        achievements = achievements,
        navigateToAddEditAchievement = navigateToAddEditAchievement
    )
}

@Composable
private fun AchievementsScreenContent(
    achievements: State<List<Achievement>>,
    navigateToAddEditAchievement: (Achievement?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(
                start = ToDoAppTheme.spacing.medium,
                top = ToDoAppTheme.spacing.medium,
                end = ToDoAppTheme.spacing.medium,
                bottom = 80.dp
            )
        ) {
            items(achievements.value) {
                AchievementCard(
                    achievement = it,
                    onClick = { navigateToAddEditAchievement(it) }
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ToDoAppTheme.spacing.medium),
            onClick = { navigateToAddEditAchievement(null) }
        ) {
            ToDoAppIcon(
                icon = ToDoAppIcons.icAdd,
                contentDescription = "add"
            )
        }
    }
}

class AchievementsScreenPreviewProvider : PreviewParameterProvider<List<Achievement>> {
    override val values = sequenceOf(mockedAchievements)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun AchievementsScreenPreview(
    @PreviewParameter(AchievementsScreenPreviewProvider::class) achievements: List<Achievement>
) {
    ToDoAppTheme {
        AchievementsScreenContent(
            achievements = remember { mutableStateOf(achievements) },
            navigateToAddEditAchievement = {}
        )
    }
}
