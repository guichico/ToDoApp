package com.apphico.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.apphico.core_model.Group

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF1B1B1B)
val LightGray = Color(0XFFF2F2F2)
val ExtraLightGray = Color(0XFFF8F9FA)
val MediumGray = Color(0XFFADB5BD)
val DarkGray = Color(0XFF495057)
val ChiliRed = Color(0xFFE23D28)

val LightBlue = Color(0xFFEEF0F6)
val MediumBlue = Color(0XFFCEE5FF)
val RippleBlue = Color(0XFFCDD3E5)

val MainContainer = Color(0XFFDDE2EE)
val MainText = Color(0xFF495D92)
val SecondaryText = Color(0XFF556CAA)

val DisabledColor = Color(0xFFC0C0C0)

val ProgressBlue = Color(0xFF0070BB)
val ProgressTrack = Color(0XFFF2F2F2)
val ProgressColor = Color(0XFFA6A6A6)

val GroupGray = Color(0XFFDEE2E6)
val GroupBlue = Color(0xFF89CFF0)
val GroupGreen = Color(0XFF3E8E6A)
val GroupRed = Color(0XFFE01E37)
val GroupPink = Color(0XFFFFB3C6)
val GroupLightBrown = Color(0XFFDEAB90)
val GroupDarkBrown = Color(0XFF7F5539)
val GroupYellow = Color(0XFFF9DC5C)
val GroupOrange = Color(0XFFFFBB4D)
val GroupViolet = Color(0XFF994FE3)

fun isColorDark(color: Int): Boolean {
    val darkness: Double =
        1 - (0.299 * android.graphics.Color.red(color) + 0.587 * android.graphics.Color.green(color) + 0.114 * android.graphics.Color.blue(color)) / 255
    return darkness >= 0.5
}

@Composable
fun Group?.getBgColor() = this?.color?.let { Color(it) } ?: MaterialTheme.colorScheme.onPrimary

@Composable
fun Group?.getTextColor() =
    this?.color?.let { if (isColorDark(Color(it).toArgb())) White else Black } ?: MaterialTheme.colorScheme.primary

@Composable
fun Group?.getStrokeColor() = this?.color?.let { Color.Transparent } ?: MaterialTheme.colorScheme.primary
