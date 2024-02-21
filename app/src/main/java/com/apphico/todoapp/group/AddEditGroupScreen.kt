package com.apphico.todoapp.group

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.R
import com.apphico.designsystem.components.ColorPicker
import com.apphico.designsystem.components.dialogs.DiscardChangesDialog
import com.apphico.designsystem.components.dialogs.navigateBackConfirm
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.Screen

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterAddEditGroup() =
    when (initialState.destination.route) {
        Screen.SelectGroup.route -> slideInVertically(initialOffsetY = { it })
        else -> fadeIn()
    }


fun AnimatedContentTransitionScope<NavBackStackEntry>.exitAddEditGroup() =
    when (targetState.destination.route) {
        Screen.SelectGroup.route -> slideOutVertically(targetOffsetY = { it })
        else -> fadeOut()
    }

fun NavController.navigateToAddEditGroup() {
    this.navigate(Screen.AddEditGroup.route)
}

@Composable
fun AddEditGroupScreen(
    addEditGroupViewModel: AddEditGroupViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val group = addEditGroupViewModel.group.collectAsState()
    val isEditing = addEditGroupViewModel.isEditing

    val isAlertDialogOpen = remember { mutableStateOf(false) }
    val hasChanges = remember { derivedStateOf { addEditGroupViewModel.hasChanges() } }

    DiscardChangesDialog(
        isAlertDialogOpen = isAlertDialogOpen,
        hasChanges = hasChanges,
        navigateBack = navigateBack
    )

    DeleteSaveTopBar(
        title = stringResource(R.string.add_group),
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
        AddEditGroupScreenContent(
            innerPadding = innerPadding,
            group = group,
            onNameChanged = addEditGroupViewModel::onNameChanged,
            onColorChanged = addEditGroupViewModel::onColorChanged
        )
    }
}

@Composable
private fun AddEditGroupScreenContent(
    innerPadding: PaddingValues,
    group: State<Group>,
    onNameChanged: (String) -> Unit,
    onColorChanged: (Color) -> Unit
) {
    val name = remember { derivedStateOf { group.value.name } }
    val color = remember { derivedStateOf { Color(group.value.color) } }

    Box(
        modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = ToDoAppTheme.spacing.extraLarge,
                    horizontal = ToDoAppTheme.spacing.large
                )
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NormalTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = name.value,
                placeholder = stringResource(id = R.string.name),
                onValueChange = onNameChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.extraExtraLarge))
            ColorPicker(
                selectedColor = color,
                onColorSelected = onColorChanged
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun AddEditGroupScreenPreview(
) {
    ToDoAppTheme {
        AddEditGroupScreenContent(
            innerPadding = PaddingValues(),
            group = remember { mutableStateOf(mockedGroup) },
            onNameChanged = {},
            onColorChanged = {}
        )
    }
}