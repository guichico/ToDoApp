package com.apphico.designsystem.components.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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

@Composable
fun NestedScroll(
    anchorViewHeight: Dp,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val nestedScrollConnection = rememberNestedScrollConnection(anchorViewHeight)
    var anchorViewOffsetHeightPx = nestedScrollConnection.anchorViewOffsetHeightPx

    val offsetY by animateDpAsState(anchorViewOffsetHeightPx.floatValue.roundToInt().dp)
    val padding by animateDpAsState(
        (anchorViewHeight - (anchorViewOffsetHeightPx.floatValue.dp * -1))
            .takeIf { it.value > 0 && isNestedViewExpanded.value } ?: 0.dp
    )

    LaunchedEffect(isNestedViewExpanded.value) {
        nestedScrollConnection.anchorViewOffsetHeightPx.floatValue = if (isNestedViewExpanded.value) 0f else (anchorViewHeight.value * -1)
    }

    LaunchedEffect(nestedScrollConnection.isScrollInProgress.value) {
        val scrollPercent = (offsetY * -1) / anchorViewHeight
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
            .height(anchorViewHeight)
            .offset(y = offsetY)
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