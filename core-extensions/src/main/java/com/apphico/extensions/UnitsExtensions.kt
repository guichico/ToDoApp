package com.apphico.extensions

import java.text.DecimalFormat

private val decimalFormat by lazy { DecimalFormat("#,##0.00") }

private const val numberOfDecimals: Int = 2
private val symbols = DecimalFormat().decimalFormatSymbols

fun Float.format(): String = decimalFormat.format(this)

fun Float.toTextFieldFormat(): String = this.toString().replace(".", "0")

fun String.getNumber(): String =
    when {
        this.startsWith("0") -> ""
        else -> this
    }

fun String.toFormattedNumber(userThousandSeparator: Boolean = false): String {
    val thousandsSeparator = if (userThousandSeparator) symbols.groupingSeparator else ""
    val decimalSeparator = symbols.decimalSeparator
    val zero = symbols.zeroDigit

    val intPart = this
        .dropLast(numberOfDecimals)
        .reversed()
        .chunked(3)
        .joinToString(thousandsSeparator.toString())
        .reversed()
        .ifEmpty {
            zero.toString()
        }

    val fractionPart = this.takeLast(numberOfDecimals).let {
        if (it.length != numberOfDecimals) {
            List(numberOfDecimals - it.length) {
                zero
            }.joinToString("") + it
        } else {
            it
        }
    }

    return intPart + decimalSeparator + fractionPart
}

fun String.toIntFormattedNumber(): String {
    val thousandsSeparator = symbols.groupingSeparator
    val zero = symbols.zeroDigit

    val intPart = this
        .reversed()
        .chunked(3)
        .joinToString(thousandsSeparator.toString())
        .reversed()
        .ifEmpty {
            zero.toString()
        }

    return intPart
}