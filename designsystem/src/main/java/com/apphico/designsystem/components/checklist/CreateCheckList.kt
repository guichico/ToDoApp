package com.apphico.designsystem.components.checklist

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.core_model.CheckListItem
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CreateCheckList(
    scrollState: ScrollState,
    addNewItemTitle: String,
    checkList: State<List<CheckListItem>>,
    onCheckListChanged: (List<CheckListItem>) -> Unit
) {
    val mutableCheckListState = remember { mutableStateListOf<CheckListItem>().apply { addAll(checkList.value) } }

    Column {
        mutableCheckListState.toList()
            .forEachIndexed { index, checkListItem ->
                CheckListItemField(
                    mutableCheckListState = mutableCheckListState,
                    index = index,
                    checkListItem = checkListItem,
                    onCheckListChanged = onCheckListChanged
                )

                Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))
            }

        AddItemField(
            addNewItemTitle = addNewItemTitle,
            mutableCheckListState = mutableCheckListState,
            onCheckListChanged = onCheckListChanged,
            scrollState = scrollState
        )
    }
}

@Composable
private fun CheckListItemField(
    mutableCheckListState: SnapshotStateList<CheckListItem>,
    index: Int,
    checkListItem: CheckListItem,
    onCheckListChanged: (List<CheckListItem>) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val nameStyle =
        if (checkListItem.isDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough) else MaterialTheme.typography.bodyLarge

    SmallTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = mutableCheckListState[index].name,
        onValueChange = { value ->
            mutableCheckListState[index] = checkListItem.copy(name = value)
            onCheckListChanged(mutableCheckListState.toList())
        },
        textStyle = nameStyle,
        leadingIcon = {
            Row {
                IconButton(
                    onClick = {

                    }
                ) {
                    ToDoAppIcon(
                        icon = ToDoAppIcons.icReorder,
                        contentDescription = "checkbox"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .offset(x = (-8).dp),
                    onClick = {

                    }
                ) {
                    ToDoAppIcon(
                        icon = if (checkListItem.isDone) ToDoAppIcons.icCheckCircle else ToDoAppIcons.icCircle,
                        contentDescription = "checkbox"
                    )
                }
            }

        },
        trailingIcon = {
            IconButton(
                onClick = {
                    mutableCheckListState.remove(checkListItem)
                    onCheckListChanged(mutableCheckListState.toList())
                }
            ) {
                ToDoAppIcon(
                    icon = ToDoAppIcons.icRemove,
                    contentDescription = "remove"
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )
}

@Composable
private fun AddItemField(
    addNewItemTitle: String,
    mutableCheckListState: SnapshotStateList<CheckListItem>,
    onCheckListChanged: (List<CheckListItem>) -> Unit,
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
            IconButton(
                onClick = {
                    text = addItem(mutableCheckListState, text, onCheckListChanged, coroutineScope, scrollState, addItemHeight)
                }
            ) {
                ToDoAppIcon(
                    icon = ToDoAppIcons.icAdd,
                    contentDescription = "add"
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                text = addItem(mutableCheckListState, text, onCheckListChanged, coroutineScope, scrollState, addItemHeight)
            })
    )
}

private fun addItem(
    mutableCheckListState: SnapshotStateList<CheckListItem>,
    text: String,
    onCheckListChanged: (List<CheckListItem>) -> Unit,
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    addItemHeight: Float
): String {
    mutableCheckListState.add(CheckListItem(name = text))
    onCheckListChanged(mutableCheckListState.toList())

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
                        CheckListItem("take dog to a walk", true),
                        CheckListItem("pet the dog", false)
                    )
                )
            },
            onCheckListChanged = {}
        )
    }
}