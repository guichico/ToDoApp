package com.apphico.designsystem.components.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun NormalTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String? = null,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    errorMessage: String = "",
    onClick: () -> Unit = emptyLambda,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardDefaults.shape)
                .clickable(onClick = onClick),
            enabled = onClick == emptyLambda,
            value = value,
            isError = isError,
            onValueChange = onValueChange,
            textStyle = textStyle,
            placeholder = {
                placeholder?.let {
                    Text(
                        text = it,
                        style = textStyle,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    )
                }
            },
            shape = CardDefaults.shape,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = MaterialTheme.colorScheme.secondary,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                disabledIndicatorColor = MaterialTheme.colorScheme.secondary,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.secondary
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions.copy(capitalization = KeyboardCapitalization.Sentences),
            keyboardActions = keyboardActions,
        )
        if (isError) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = ToDoAppTheme.spacing.extraSmall,
                        horizontal = ToDoAppTheme.spacing.small
                    ),
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun NormalTextFieldPreview() {
    ToDoAppTheme {
        NormalTextField(
            value = "Normal text field",
            placeholder = "Normal text field",
            isError = true,
            errorMessage = "Text is mandatory",
            onValueChange = {}
        )
    }
}