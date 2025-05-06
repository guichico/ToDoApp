package com.apphico.todoapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun ToDoAppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, spotColor = Color.Transparent)
    ) {
        NavigationBar(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(80.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            topLevelRoutes.forEach { topLevelRoute ->
                val isSelected = currentDestination?.hasRoute(topLevelRoute.route::class) == true

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
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.inverseSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.inverseSurface,
                        indicatorColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BottomBarPreview() {
    ToDoAppTheme {
        ToDoAppBottomBar(
            navController = rememberNavController()
        )
    }
}
