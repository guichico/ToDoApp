package com.apphico.designsystem.components.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// TODO Make it more dynamically
private const val MIN_CALENDAR_VIEW_HEIGHT = 296
private const val CALENDAR_VIEW_ROW_HEIGHT = 50

@Composable
fun NestedScroll(
    anchorViewHeight: State<Dp>,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val nestedScrollConnection = rememberNestedScrollConnection(anchorViewHeight.value)
    var anchorViewOffsetHeightPx = nestedScrollConnection.anchorViewOffsetHeightPx

    val offsetY by animateDpAsState(anchorViewOffsetHeightPx.floatValue.roundToInt().dp)
    val padding by animateDpAsState(
        (anchorViewHeight.value - (anchorViewOffsetHeightPx.floatValue.dp * -1))
            .takeIf { it.value > 0 && isNestedViewExpanded.value } ?: 0.dp
    )

    LaunchedEffect(isNestedViewExpanded.value) {
        nestedScrollConnection.anchorViewOffsetHeightPx.floatValue = if (isNestedViewExpanded.value) 0f else (anchorViewHeight.value.value * -1)
    }

    LaunchedEffect(nestedScrollConnection.isScrollInProgress.value) {
        val scrollPercent = (offsetY * -1) / anchorViewHeight.value
        val isScrollInProgress = nestedScrollConnection.isScrollInProgress.value

        if (!isScrollInProgress) {
            if (scrollPercent > 0.3) {
                onNestedViewClosed()
            } else {
                anchorViewOffsetHeightPx.floatValue = 0f
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        val modifier = Modifier
            .offset(y = offsetY)
            .heightIn(min = MIN_CALENDAR_VIEW_HEIGHT.dp, max = (anchorViewHeight.value + CALENDAR_VIEW_ROW_HEIGHT.dp))
            .shadow(elevation = 8.dp, spotColor = Color.Transparent)

        nestedContent(modifier)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding)
        ) {
            content()
        }
    }
}