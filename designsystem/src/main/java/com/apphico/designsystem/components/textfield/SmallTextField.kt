package com.apphico.designsystem.components.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String? = null,
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit = emptyLambda,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = if (isColorDark(MaterialTheme.colorScheme.primaryContainer.toArgb())) White else Black,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        modifier = modifier
            .heightIn(min = 42.dp)
            .clip(CardDefaults.shape)
            .clickable(onClick = onClick),
        enabled = onClick == emptyLambda,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(color = textColor),
        keyboardOptions = keyboardOptions.copy(capitalization = KeyboardCapitalization.Sentences),
        keyboardActions = keyboardActions,
    ) { innerTextField ->
        val horizontalPadding = if (leadingIcon != null) ToDoAppTheme.spacing.extraSmall else ToDoAppTheme.spacing.large

        TextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            placeholder = {
                placeholder?.let {
                    Text(
                        text = it,
                        style = textStyle,
                        color = textColor.copy(alpha = 0.8f)
                    )
                }
            },
            shape = CardDefaults.shape,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true,
            enabled = true,
            interactionSource = remember { MutableInteractionSource() },
            contentPadding = PaddingValues(
                vertical = ToDoAppTheme.spacing.small,
                horizontal = horizontalPadding
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = textColor,
                unfocusedLeadingIconColor = textColor,
                focusedTrailingIconColor = textColor,
                unfocusedTrailingIconColor = textColor,
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                cursorColor = textColor
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun SmallTextFieldPreview() {
    ToDoAppTheme {
        SmallTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = "Testing text",
            onValueChange = {}
        )
    }
}