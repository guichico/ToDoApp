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
import com.apphico.extensions.toFormattedNumber
import com.apphico.extensions.toTextFieldFormat

@Composable
fun CurrencyTextField(
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
                onValueChange(it.toFormattedNumber().replace(",", ".").toFloat())
            }
        },
        visualTransformation = CurrencyVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Decimal),
        keyboardActions = keyboardActions
    )
}

@PreviewLightDark
@Composable
private fun CurrencyTextFieldPreview(
) {
    ToDoAppTheme {
        CurrencyTextField(
            initialValue = 10f,
            placeholder = "decimal text field",
            onValueChange = {}
        )
    }
}