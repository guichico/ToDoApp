package com.apphico.todoapp.focus

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.apphico.core_model.FocusMode
import com.apphico.core_model.fakeData.mockedFocus
import com.apphico.designsystem.R
import com.apphico.designsystem.animatedElevation
import com.apphico.designsystem.components.dialogs.DiscardChangesDialog
import com.apphico.designsystem.components.dialogs.navigateBackConfirm
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.Screen
import com.apphico.todoapp.navigation.navigateWithArgs

internal const val FOCUS_ARG = "task"

fun NavController.navigateAddEditFocus(focusMode: FocusMode?) {
    navigateWithArgs(
        route = Screen.AddEditFocus.route,
        args = bundleOf(FOCUS_ARG to focusMode),
        navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
    )
}

fun NavController.navigateBackToAddEditFocus(
    focusMode: FocusMode
) {
    navigateWithArgs(
        route = Screen.AddEditFocus.route,
        args = bundleOf(FOCUS_ARG to focusMode),
        navOptions = NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(Screen.AddEditFocus.route, true).build()
    )
}

@Composable
fun AddEditFocusScreen(
    addEditFocusViewModel: AddEditFocusViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val editingFocus = addEditFocusViewModel.editingFocus.collectAsState()
    val isEditing = addEditFocusViewModel.isEditing

    val isAlertDialogOpen = remember { mutableStateOf(false) }
    val hasChanges = remember { derivedStateOf { addEditFocusViewModel.hasChanges() } }

    DiscardChangesDialog(
        isAlertDialogOpen = isAlertDialogOpen,
        hasChanges = hasChanges,
        navigateBack = navigateBack
    )

    val scrollState = rememberScrollState()
    val showElevation = remember {
        derivedStateOf { scrollState.isScrollInProgress || scrollState.value != 0 }
    }

    DeleteSaveTopBar(
        modifier = Modifier
            .fillMaxWidth()
            .animatedElevation(
                conditionState = showElevation,
                shadowElevation = 10f
            )
            .zIndex(1f),
        title = stringResource(R.string.add_new_task),
        isEditing = isEditing,
        onSaveClicked = {},
        onDeleteClicked = {},
        navigateBack = {
            navigateBackConfirm(
                isAlertDialogOpen = isAlertDialogOpen,
                hasChanges = hasChanges,
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        AddEditFocusScreenContent(
            innerPadding = innerPadding,
            scrollState = scrollState,
            focus = editingFocus,
        )
    }
}

@Composable
private fun AddEditFocusScreenContent(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    focus: State<FocusMode>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = ToDoAppTheme.spacing.extraLarge,
                    horizontal = ToDoAppTheme.spacing.large
                )
                .imePadding()
        ) {


        }
    }
}

class AddEditFocusScreenPreviewProvider : PreviewParameterProvider<FocusMode> {
    override val values = sequenceOf(mockedFocus)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun AddEditFocusScreenPreview(
    @PreviewParameter(AddEditFocusScreenPreviewProvider::class) focusMode: FocusMode
) {
    ToDoAppTheme {
        AddEditFocusScreenContent(
            innerPadding = PaddingValues(),
            scrollState = ScrollState(0),
            focus = remember { mutableStateOf(focusMode) },
        )
    }
}
