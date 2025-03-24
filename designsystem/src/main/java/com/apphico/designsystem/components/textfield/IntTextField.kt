package com.apphico.designsystem.components.textfield

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNumber
import com.apphico.extensions.toIntFormattedNumber
import com.apphico.extensions.toTextFieldFormat

@Composable
fun IntTextField(
    modifier: Modifier = Modifier,
    initialValue: Float?,
    placeholder: String? = null,
    onValueChange: (Float) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val pattern = remember { Regex("^\\d+\$") }
    var valueText by remember { mutableStateOf(if (initialValue != 0f) initialValue?.toTextFieldFormat() ?: "" else "") }

    NormalTextField(
        modifier = modifier,
        value = valueText,
        placeholder = placeholder,
        onValueChange = {
            if (it.isEmpty() || it.matches(pattern)) {
                valueText = it.getNumber()
                onValueChange(
                    it.toIntFormattedNumber()
                        .replace(",", "")
                        .replace(".", "")
                        .toFloat())
            }
        },
        visualTransformation = IntVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun IntTextFieldPreview(
) {
    ToDoAppTheme {
        IntTextField(
            initialValue = 10f,
            placeholder = "int text field",
            onValueChange = {}
        )
    }
}