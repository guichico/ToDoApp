package com.apphico.todoapp.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apphico.designsystem.theme.LightBlue
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun ToDoAppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(80.dp)
            .shadow(elevation = 8.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        topLevelRoutes.forEach { topLevelRoute ->
            val isSelected = currentRoute == topLevelRoute.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = topLevelRoute.name),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                icon = {
                    ToDoAppIcon(
                        icon = if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.unselectedIcon,
                        contentDescription = null
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.inverseSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.inverseSurface,
                    indicatorColor = LightBlue
                )
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun BottomBarPreview() {
    ToDoAppTheme {
        ToDoAppBottomBar(
            navController = rememberNavController()
        )
    }
}
