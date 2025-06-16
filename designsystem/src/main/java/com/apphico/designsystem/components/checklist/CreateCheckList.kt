package com.apphico.designsystem.components.checklist

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.core_model.CheckListItem
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateCheckList(
    scrollState: ScrollState,
    addNewItemTitle: String,
    checkList: State<List<CheckListItem>>,
    parentDate: State<LocalDate?>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit
) {
    FlowColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        checkList.value
            .forEach { checkListItem ->
                CheckListItemField(
                    checkListItem = checkListItem,
                    parentDate = parentDate.value,
                    onNameChanged = { name ->
                        onCheckListItemChanged(checkListItem, checkListItem.copy(name = name))
                    },
                    onItemRemoved = { onCheckListItemItemRemoved(it) },
                    onCheckListItemDoneChanged = { checkListItem, isDone -> onCheckListItemDoneChanged(checkListItem, parentDate.value, isDone) }
                )

                Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))
            }

        AddItemField(
            addNewItemTitle = addNewItemTitle,
            onItemAdded = { onCheckListItemItemAdded(it) },
            scrollState = scrollState
        )
    }
}

@Composable
private fun AddItemField(
    addNewItemTitle: String,
    onItemAdded: (CheckListItem) -> Unit,
    scrollState: ScrollState
) {
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = FocusRequester()

    val addItemHeight = with(LocalDensity.current) { 56.dp.toPx() }

    var text by remember { mutableStateOf("") }

    SmallTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = text,
        placeholder = addNewItemTitle,
        onValueChange = { text = it },
        trailingIcon = {
            ToDoAppIconButton(
                icon = ToDoAppIcons.icAdd,
                onClick = {
                    text = addItem(onItemAdded, text, coroutineScope, scrollState, addItemHeight)
                }
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                text = addItem(onItemAdded, text, coroutineScope, scrollState, addItemHeight)
            }
        )
    )
}

private fun addItem(
    onItemAdded: (CheckListItem) -> Unit,
    text: String,
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    addItemHeight: Float
): String {
    onItemAdded(CheckListItem(name = text))

    coroutineScope.launch {
        scrollState.animateScrollBy(addItemHeight)
    }

    return ""
}

@PreviewLightDark
@Composable
private fun CreateCheckListPreview() {
    ToDoAppTheme {
        CreateCheckList(
            scrollState = rememberScrollState(),
            addNewItemTitle = "Add new item",
            checkList = remember {
                mutableStateOf(
                    listOf(
                        CheckListItem(0L, "take dog to a walk", true),
                        CheckListItem(0L, "pet the dog", false)
                    )
                )
            },
            parentDate = remember { mutableStateOf(getNowDate()) },
            onCheckListItemChanged = { _, _ -> },
            onCheckListItemItemAdded = {},
            onCheckListItemItemRemoved = {},
            onCheckListItemDoneChanged = { _, _, _ -> }
        )
    }
}