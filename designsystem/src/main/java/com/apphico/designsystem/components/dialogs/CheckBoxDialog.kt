package com.apphico.designsystem.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.core_model.CheckBoxItem
import com.apphico.core_model.RecurringTask
import com.apphico.designsystem.components.buttons.AlertButton
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun CheckBoxDialog(
    title: String? = null,
    values: List<CheckBoxItem>,
    selectedItem: CheckBoxItem?,
    onItemSelected: (CheckBoxItem) -> Unit,
    dismissButtonText: String? = null,
    onDismissRequest: () -> Unit = emptyLambda,
    confirmButtonText: String? = null,
    onConfirmClicked: () -> Unit = emptyLambda
) {
    Column {
        DefaultDialog(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ToDoAppTheme.spacing.medium)
            ) {
                title?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = ToDoAppTheme.spacing.small,
                                horizontal = ToDoAppTheme.spacing.large
                            ),
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                values.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemSelected(it) }
                    ) {
                        ToDoAppIcon(
                            modifier = Modifier
                                .padding(
                                    start = ToDoAppTheme.spacing.large,
                                    top = ToDoAppTheme.spacing.large,
                                    bottom = ToDoAppTheme.spacing.large
                                )
                                .align(Alignment.CenterVertically),
                            icon = if (selectedItem == it) ToDoAppIcons.icRadioButtonChecked else ToDoAppIcons.icRadioButtonUnchecked,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier
                                .padding(ToDoAppTheme.spacing.large)
                                .align(Alignment.CenterVertically),
                            text = stringResource(it.title),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    dismissButtonText?.let {
                        AlertButton(
                            text = it,
                            onClick = onDismissRequest
                        )
                    }
                    confirmButtonText?.let {
                        AlertButton(
                            text = it,
                            onClick = onConfirmClicked
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun SaveRecurringTaskDialogPreview() {
    ToDoAppTheme {
        CheckBoxDialog(
            title = "Save task",
            values = listOf(RecurringTask.ThisTask, RecurringTask.Future, RecurringTask.All),
            selectedItem = RecurringTask.ThisTask,
            onItemSelected = {},
            dismissButtonText = "Cancel",
            onDismissRequest = {},
            confirmButtonText = "Save",
            onConfirmClicked = {}
        )
    }
}