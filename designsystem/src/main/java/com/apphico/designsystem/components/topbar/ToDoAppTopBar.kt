package com.apphico.designsystem.components.topbar

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoAppTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = emptyLambda,
    title: String? = null,
    onTitleClicked: () -> Unit = emptyLambda,
    subTitle: String? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val textColor = if (isSystemInDarkTheme()) White else Black

    TopAppBar(
        modifier = modifier,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = onTitleClicked != emptyLambda,
                        onClick = onTitleClicked
                    )
            ) {
                title?.let { title ->
                    subTitle?.let { subTitle ->
                        HeaderWithSubtitle(
                            title = title,
                            subTitle = subTitle,
                            textColor = textColor
                        )
                    } ?: run {
                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp, end = 4.dp, bottom = 4.dp),
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(
                White.toArgb()
                // MaterialTheme.colorScheme.primary.copy(alpha = 0.085f).compositeOver(MaterialTheme.colorScheme.surface.copy()).toArgb()
            )
        ),
        navigationIcon = if (navigateBack != emptyLambda) {
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ToDoAppIconButton(
                        icon = ToDoAppIcons.icBack,
                        tint = textColor,
                        onClick = navigateBack
                    )
                }
            }
        } else {
            {}
        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun HeaderWithSubtitle(
    title: String,
    subTitle: String,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .padding(
                top = ToDoAppTheme.spacing.medium,
                end = ToDoAppTheme.spacing.extraSmall
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = textColor
        )
        ToDoAppIcon(
            modifier = Modifier
                .padding(horizontal = ToDoAppTheme.spacing.small)
                .align(Alignment.CenterVertically),
            icon = ToDoAppIcons.icArrowDown,
            contentDescription = "calendar"
        )
    }
    Text(
        modifier = Modifier
            .padding(end = ToDoAppTheme.spacing.extraSmall),
        text = subTitle,
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
        color = MaterialTheme.colorScheme.secondary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ToDoAppTopBarPreview() {
    ToDoAppTopBar(
        title = "Top bar",
        subTitle = "subtitle",
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    )
}
