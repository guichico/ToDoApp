package com.apphico.core_model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Stable
@Immutable
@Parcelize
@Serializable
data class Reminder(
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0
) : Parcelable
