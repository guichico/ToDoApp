package com.apphico.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.components.textfield.GroupField
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun AddEditHeader(
    nameValue: State<String?>,
    namePlaceholder: String,
    onNameChange: (String) -> Unit,
    descriptionValue: State<String?>,
    descriptionPlaceholder: String,
    onDescriptionChange: (String) -> Unit,
    group: State<Group?>,
    groupPlaceholder: String,
    navigateToSelectGroup: () -> Unit,
    onGroupRemoved: () -> Unit
) {
    val focusRequester = FocusRequester()

    Column {
        NameTextField(nameValue, namePlaceholder, onNameChange, focusRequester)

        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

        DescriptionTextField(focusRequester, descriptionValue, descriptionPlaceholder, onDescriptionChange)

        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

        GroupField(
            modifier = Modifier.fillMaxWidth(),
            group = group,
            addNewGroupPlaceholder = groupPlaceholder,
            navigateToSelectGroup = navigateToSelectGroup,
            onGroupRemoved = onGroupRemoved
        )
    }
}

@Composable
private fun NameTextField(
    nameValue: State<String?>,
    namePlaceholder: String,
    onNameChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    NormalTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = nameValue.value ?: "",
        placeholder = namePlaceholder,
        onValueChange = onNameChange,
        textStyle = MaterialTheme.typography.titleMedium,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
    )
}

@Composable
private fun DescriptionTextField(
    focusRequester: FocusRequester,
    descriptionValue: State<String?>,
    descriptionPlaceholder: String,
    onDescriptionChange: (String) -> Unit
) {
    NormalTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = descriptionValue.value ?: "",
        placeholder = descriptionPlaceholder,
        onValueChange = onDescriptionChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
}

class AddEditHeaderPreviewProvider : PreviewParameterProvider<Group> {
    override val values = sequenceOf(mockedGroup)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AddEditHeaderPreview(
    @PreviewParameter(AddEditHeaderPreviewProvider::class) group: Group
) {
    AddEditHeader(
        nameValue = remember { mutableStateOf("") },
        namePlaceholder = "Add name",
        onNameChange = {},
        descriptionValue = remember { mutableStateOf("") },
        descriptionPlaceholder = "Add description",
        onDescriptionChange = {},
        group = remember { mutableStateOf(group) },
        groupPlaceholder = "Select group",
        navigateToSelectGroup = {},
        onGroupRemoved = {}
    )
}