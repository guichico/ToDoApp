package com.apphico.designsystem.components.checklist

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.core_model.CheckListItem
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark
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
                    onNameChanged = { name -> onCheckListItemChanged(checkListItem, checkListItem.copy(name = name)) },
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
private fun CheckListItemField(
    checkListItem: CheckListItem,
    parentDate: LocalDate?,
    onNameChanged: (String) -> Unit,
    onItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val isCheckListItemDone = checkListItem.isDone(parentDate)

    val nameStyle =
        if (isCheckListItemDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough) else MaterialTheme.typography.bodyLarge
    val textColor = if (isColorDark(MaterialTheme.colorScheme.primaryContainer.toArgb())) White else Black
    val animatedColor by animateColorAsState(if (!isCheckListItemDone) textColor else textColor.copy(alpha = 0.5f))

    var text by remember { mutableStateOf(checkListItem.name) }

    SmallTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
            onNameChanged(it)
        },
        textStyle = nameStyle,
        textColor = animatedColor,
        leadingIcon = {
            Row {
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icReorder,
                    onClick = {}
                )
                CircleCheckbox(
                    modifier = Modifier
                        .offset(x = (-8).dp)
                        .align(Alignment.CenterVertically),
                    checked = isCheckListItemDone,
                    onCheckedChanged = {
                        onCheckListItemDoneChanged(checkListItem, it)
                    },
                    tint = animatedColor
                )
            }
        },
        trailingIcon = {
            ToDoAppIconButton(
                icon = ToDoAppIcons.icRemove,
                onClick = { onItemRemoved(checkListItem) }
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )
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

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TaskCreateCheckListPreview() {
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