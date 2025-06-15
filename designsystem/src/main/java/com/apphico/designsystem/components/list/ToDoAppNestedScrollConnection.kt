package com.apphico.designsystem.components.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity

class ToDoAppNestedScrollConnection(
    private val anchorViewHeightPx: Float = 0f,
    val anchorViewOffsetHeightPx: MutableFloatState,
    val isScrollInProgress: MutableState<Boolean>
) : NestedScrollConnection {

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        isScrollInProgress.value = false
        return super.onPostFling(consumed, available)
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        isScrollInProgress.value = true

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
    val isScrollInProgress = remember { mutableStateOf(false) }

    val anchorViewHeightPx = with(LocalDensity.current) { anchorViewHeight.roundToPx().toFloat() }
    val anchorViewOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    return remember { ToDoAppNestedScrollConnection(anchorViewHeightPx, anchorViewOffsetHeightPx, isScrollInProgress) }
}