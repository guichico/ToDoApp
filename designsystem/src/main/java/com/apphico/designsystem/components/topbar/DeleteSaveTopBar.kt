package com.apphico.designsystem.components.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteSaveTopBar(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onCopyClicked: () -> Unit = emptyLambda,
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
                        if (onCopyClicked != emptyLambda) {
                            ToDoAppIconButton(
                                icon = ToDoAppIcons.icCopy,
                                onClick = onCopyClicked
                            )
                        }
                        ToDoAppIconButton(
                            icon = ToDoAppIcons.icDelete,
                            onClick = onDeleteClicked
                        )
                    }
                    ToDoAppIconButton(
                        icon = if (isEditing) ToDoAppIcons.icUpdate else ToDoAppIcons.icSave,
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
@PreviewLightDark
@Composable
private fun DeleteSaveTopBarPreview(
) {
    ToDoAppTheme {
        DeleteSaveTopBar(
            title = "Some screen",
            isEditing = false,
            onSaveClicked = {},
            onDeleteClicked = {},
            onCopyClicked = {},
            navigateBack = {},
            content = {}
        )
    }
}