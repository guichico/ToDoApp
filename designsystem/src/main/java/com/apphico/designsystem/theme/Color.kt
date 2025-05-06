package com.apphico.designsystem.theme

import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF1B1B1B)
val LightGray = Color(0XFFF2F2F2)
val ExtraLightGray = Color(0XFFF8F9FA)
val MediumGray = Color(0XFFADB5BD)
val DarkGray = Color(0XFF495057)
val ChiliRed = Color(0xFFE23D28)

val LightBlue = Color(0XFFF5F7FD)
val MediumBlue = Color(0XFFCEE5FF)
val RippleBlue = Color(0XFFCDD3E5)

val MainContainer = Color(0XFFF0F0F4)
val MainText = Color(0xFF495D92)
val SecondaryText = Color(0XFF556CAA)

val DisabledColor = Color(0xFFC0C0C0)

val ProgressBlue = Color(0xFF0070BB)
val ProgressTrack = Color(0XFFF2F2F2)
val ProgressColor = Color(0XFFA6A6A6)

val GroupLightGray = Color(0XFF99A6B2)
val GroupDarkGray = Color(0XFF424D57)
val GroupLightBlue = Color(0xFF89CFF0)
val GroupDarkBlue = Color(0XFF15729E)
val GroupLightGreen = Color(0XFF5FB991)
val GroupDarkGreen = Color(0XFF275942)
val GroupLightRed = Color(0XFFE3354C)
val GroupDarkRed = Color(0XFF9D1528)
val GroupPink = Color(0XFFFFCCD9)
val GroupLightBrown = Color(0XFFDEAB90)
val GroupDarkBrown = Color(0XFF7F5539)
val GroupYellow = Color(0XFFF9DC5C)
val GroupOrange = Color(0XFFFFBB4D)
val GroupLightViolet = Color(0XFFBF91EE)
val GroupDarkViolet = Color(0XFF59189A)

fun isColorDark(color: Int): Boolean {
    val darkness: Double =
        1 - (0.299 * android.graphics.Color.red(color) + 0.587 * android.graphics.Color.green(color) + 0.114 * android.graphics.Color.blue(color)) / 255
    return darkness >= 0.5
}