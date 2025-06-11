package com.apphico.designsystem.components.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

class ToDoAppNestedScrollConnection(
    private val anchorViewHeightPx: Float = 0f,
    val anchorViewOffsetHeightPx: MutableFloatState
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        val newOffset = anchorViewOffsetHeightPx.floatValue + delta
        anchorViewOffsetHeightPx.floatValue = newOffset.coerceIn(-anchorViewHeightPx, 0f)
        return Offset.Zero
    }
}

@Composable
fun rememberNestedScrollConnection(
    anchorViewHeight: Dp
): ToDoAppNestedScrollConnection {
    val anchorViewHeightPx = with(LocalDensity.current) { anchorViewHeight.roundToPx().toFloat() }
    val anchorViewOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    return remember { ToDoAppNestedScrollConnection(anchorViewHeightPx, anchorViewOffsetHeightPx) }
}