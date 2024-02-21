package com.apphico.designsystem.components.topbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.SmallButton
import com.apphico.designsystem.theme.ToDoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteSaveTopBar(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    navigateBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .consumeWindowInsets(WindowInsets.systemBars),
        topBar = {
            ToDoAppTopBar(
                modifier = modifier,
                navigateBack = navigateBack,
                title = if (!isEditing) title else null,
                scrollBehavior = scrollBehavior,
                actions = {
                    if (isEditing) {
                        SmallButton(
                            modifier = Modifier
                                .padding(end = ToDoAppTheme.spacing.medium),
                            text = stringResource(R.string.delete),
                            onClick = onDeleteClicked
                        )
                    }
                    SmallButton(
                        modifier = Modifier
                            .padding(end = ToDoAppTheme.spacing.medium),
                        text = stringResource(R.string.save),
                        onClick = onSaveClicked
                    )
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DeleteSaveTopBarPreview(
) {
    ToDoAppTheme {
        DeleteSaveTopBar(
            title = "Some screen",
            isEditing = false,
            onSaveClicked = {},
            onDeleteClicked = {},
            navigateBack = {},
            content = {}
        )
    }
}