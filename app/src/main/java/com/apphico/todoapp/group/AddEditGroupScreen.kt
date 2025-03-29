package com.apphico.todoapp.group

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.R
import com.apphico.designsystem.components.dialogs.showDiscardChangesDialogOnBackIfNeed
import com.apphico.designsystem.components.picker.ColorPicker
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun AddEditGroupScreen(
    addEditGroupViewModel: AddEditGroupViewModel = hiltViewModel(),
    snackBar: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val group = addEditGroupViewModel.editingGroup.collectAsState()
    val isEditing = addEditGroupViewModel.isEditing

    val showDiscardChangesDialogOnBackIfNeed = showDiscardChangesDialogOnBackIfNeed(
        hasChanges = addEditGroupViewModel::hasChanges,
        navigateBack = navigateBack
    )

    val groupSaveSuccess = stringResource(R.string.group_saved)
    val groupSaveError = stringResource(R.string.group_save_error)

    val groupDeleteSuccess = stringResource(R.string.group_deleted)
    val groupDeleteError = stringResource(R.string.group_delete_error)

    DeleteSaveTopBar(
        title = stringResource(R.string.add_group),
        isEditing = isEditing,
        onSaveClicked = {
            addEditGroupViewModel.save { isSuccess ->
                snackBar(if (isSuccess) groupSaveSuccess else groupSaveError)
                if (isSuccess) navigateBack()
            }
        },
        onDeleteClicked = {
            addEditGroupViewModel.delete { isSuccess ->
                snackBar(if (isSuccess) groupDeleteSuccess else groupDeleteError)
                if (isSuccess) navigateBack()
            }
        },
        navigateBack = {
            showDiscardChangesDialogOnBackIfNeed()
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
            Spacer(modifier = Modifier.height(48.dp))
            ColorPicker(
                selectedColor = color,
                onColorSelected = onColorChanged
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AddEditGroupScreenPreview() {
    ToDoAppTheme {
        AddEditGroupScreenContent(
            innerPadding = PaddingValues(),
            group = remember { mutableStateOf(mockedGroup) },
            onNameChanged = {},
            onColorChanged = {}
        )
    }
}