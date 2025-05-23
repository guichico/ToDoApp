package com.apphico.designsystem.components.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNumber
import com.apphico.extensions.toIntFormattedNumber

@Composable
fun IntTextField(
    modifier: Modifier = Modifier,
    initialValue: Float?,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (Float) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val pattern = remember { Regex("^\\d+$") }
    var valueText by remember { mutableStateOf(initialValue?.toInt()?.toString() ?: "") }

    NormalTextField(
        modifier = modifier,
        value = valueText,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        onValueChange = {
            if (it.isEmpty() || it.matches(pattern)) {
                valueText = it.getNumber()
                onValueChange(
                    it.toIntFormattedNumber()
                        .replace(",", "")
                        .replace(".", "")
                        .toFloat()
                )
            }
        },
        visualTransformation = IntVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions
    )
}

@PreviewLightDark
@Composable
private fun IntTextFieldPreview() {
    ToDoAppTheme {
        IntTextField(
            initialValue = 10.0f,
            placeholder = "int text field",
            onValueChange = {}
        )
    }
}