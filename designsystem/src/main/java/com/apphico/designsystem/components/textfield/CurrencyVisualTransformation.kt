package com.apphico.designsystem.components.textfield

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.apphico.extensions.toFormattedNumber
import java.text.DecimalFormat

class CurrencyVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text

        if (inputText.isNotEmpty()) {
            val currencySymbol = DecimalFormat().decimalFormatSymbols.currencySymbol

            val formattedNumber = inputText.toFormattedNumber(true).takeUnless { inputText.isEmpty() || (inputText.toFloat() == 0f) } ?: ""

            val newText = AnnotatedString(
                text = "$currencySymbol $formattedNumber",
                spanStyles = text.spanStyles,
                paragraphStyles = text.paragraphStyles
            )

            val offsetMapping = FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length + (currencySymbol.length + 1)
            )

            return TransformedText(newText, offsetMapping)
        } else {
            val offsetMapping = FixedCursorOffsetMapping(
                contentLength = 0,
                formattedContentLength = 0
            )

            return TransformedText(AnnotatedString(text = ""), offsetMapping)
        }
    }

    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

}