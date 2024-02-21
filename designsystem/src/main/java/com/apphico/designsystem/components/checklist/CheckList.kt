package com.apphico.designsystem.components.checklist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.core_model.CheckListItem
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun CheckList(
    modifier: Modifier = Modifier,
    checkList: List<CheckListItem>,
    textColor: Color
) {
    Column(
        modifier = modifier
    ) {
        checkList.forEach { checkListItem ->
            TaskCheckListItem(
                checkListItem = checkListItem,
                textColor = textColor
            )
        }
    }
}

@Composable
private fun TaskCheckListItem(
    checkListItem: CheckListItem,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .padding(vertical = ToDoAppTheme.spacing.extraSmall)
    ) {
        val nameStyle = if (checkListItem.isDone) MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
        else MaterialTheme.typography.bodyMedium

        CircleCheckbox(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            checked = checkListItem.isDone,
            onCheckedChange = {},
            tint = textColor
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = ToDoAppTheme.spacing.extraSmall),
            text = checkListItem.name,
            style = nameStyle,
            color = textColor
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TaskCheckListPreview() {
    ToDoAppTheme {
        CheckList(
            checkList = listOf(
                CheckListItem(
                    name = "matemática",
                    isDone = false
                ),
                CheckListItem(
                    name = "português",
                    isDone = true
                )
            ),
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}