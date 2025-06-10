package com.apphico.designsystem.components.list

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

@Composable
fun rememberNestedScroll(): ToDoAppNestedScroll {
    // TODO Implement saver
    return remember { ToDoAppNestedScroll() }
}

@Stable
class ToDoAppNestedScroll : NestedScrollConnection {

    private val isScrollInProgressState = mutableStateOf(false)
    val isScrollInProgress: Boolean
        get() = isScrollInProgressState.value

    private val scrollOffsetState = mutableFloatStateOf(0f)
    val scrollOffset: Float
        get() = scrollOffsetState.floatValue

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        scrollOffsetState.floatValue += consumed.y
        isScrollInProgressState.value = true
        return Offset.Zero
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        isScrollInProgressState.value = false
        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        scrollOffsetState.floatValue = 0f
        return super.onPostFling(consumed, available)
    }
}