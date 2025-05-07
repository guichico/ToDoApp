package com.apphico.designsystem.components.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNumber
import com.apphico.extensions.toFormattedNumber
import com.apphico.extensions.toTextFieldFormat

@Composable
fun PercentageTextField(
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
    var valueText by remember { mutableStateOf(initialValue?.toTextFieldFormat() ?: "") }

    NormalTextField(
        modifier = modifier,
        value = valueText,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        onValueChange = {
            val percent = it.toFloatOrNull() ?: 0f

            if (percent <= 10000.0 && (it.isEmpty() || it.matches(pattern))) {
                valueText = it.getNumber()
                onValueChange(it.toFormattedNumber().replace(",", ".").toFloat())
            }
        },
        trailingIcon = {
            ToDoAppIcon(
                icon = ToDoAppIcons.icPercent,
                contentDescription = "percent",
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
            )
        },
        visualTransformation = DecimalVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions
    )
}

@PreviewLightDark
@Composable
private fun DecimalTextFieldPreview() {
    ToDoAppTheme {
        PercentageTextField(
            initialValue = 1f,
            placeholder = "percentage text field",
            onValueChange = {}
        )
    }
}