package com.apphico.designsystem.theme

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarViewDay
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.EditLocationAlt
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.KeyboardAlt
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Reorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.apphico.designsystem.theme.BaseToDoAppIcon.DrawableResourceIcon
import com.apphico.designsystem.theme.BaseToDoAppIcon.ImageVectorIcon

sealed class BaseToDoAppIcon {
    data class ImageVectorIcon(val imageVector: ImageVector) : BaseToDoAppIcon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : BaseToDoAppIcon()
}

object ToDoAppIcons {
    val icBack = ImageVectorIcon(Icons.AutoMirrored.Outlined.ArrowBack)

    val icMenuFocusUnselected = ImageVectorIcon(Icons.Outlined.Psychology)
    val icMenuCalendarUnselected = ImageVectorIcon(Icons.Outlined.CalendarMonth)
    val icMenuAchievementUnselected = ImageVectorIcon(Icons.Outlined.WorkspacePremium)

    val icMenuFocusSelected = ImageVectorIcon(Icons.Filled.Psychology)
    val icMenuCalendarSelected = ImageVectorIcon(Icons.Filled.CalendarMonth)
    val icMenuAchievementSelected = ImageVectorIcon(Icons.Filled.WorkspacePremium)

    val icCircle = ImageVectorIcon(Icons.Outlined.Circle)
    val icCheck = ImageVectorIcon(Icons.Outlined.Check)
    val icCheckCircle = ImageVectorIcon(Icons.Outlined.CheckCircleOutline)
    val icRadioButtonUnchecked = ImageVectorIcon(Icons.Outlined.RadioButtonUnchecked)
    val icRadioButtonChecked = ImageVectorIcon(Icons.Outlined.RadioButtonChecked)
    val icAdd = ImageVectorIcon(Icons.Outlined.Add)
    val icRemove = ImageVectorIcon(Icons.Outlined.Remove)
    val icEdit = ImageVectorIcon(Icons.Outlined.ModeEdit)
    val icReminder = ImageVectorIcon(Icons.Outlined.Alarm)
    val icLocation = ImageVectorIcon(Icons.Outlined.Place)
    val icCalendarViewDay = ImageVectorIcon(Icons.Outlined.CalendarViewDay)
    val icCalendarViewAgenda = ImageVectorIcon(Icons.Outlined.ViewAgenda)
    val icToday = ImageVectorIcon(Icons.Outlined.Today)
    val icFilter = ImageVectorIcon(Icons.Outlined.FilterList)
    val icArrowDown = ImageVectorIcon(Icons.Outlined.ArrowDropDown)
    val icArrowLeft = ImageVectorIcon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft)
    val icArrowRight = ImageVectorIcon(Icons.AutoMirrored.Outlined.KeyboardArrowRight)
    val icKeyboard = ImageVectorIcon(Icons.Outlined.KeyboardAlt)
    val icClock = ImageVectorIcon(Icons.Outlined.AccessTime)
    val icReorder = ImageVectorIcon(Icons.Outlined.Reorder)
    val icSearch = ImageVectorIcon(Icons.Outlined.Search)
    val icLocationOn = ImageVectorIcon(Icons.Filled.LocationOn)
    val icEditLocation = ImageVectorIcon(Icons.Outlined.EditLocationAlt)
    val icNoInternetConnection = ImageVectorIcon(Icons.Outlined.WifiOff)
    val icError = ImageVectorIcon(Icons.Outlined.Error)
}

@Composable
fun ToDoAppIcon(
    icon: BaseToDoAppIcon,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is ImageVectorIcon -> {
            Icon(
                modifier = modifier,
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                tint = tint
            )
        }

        is DrawableResourceIcon -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = icon.id),
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }
}
