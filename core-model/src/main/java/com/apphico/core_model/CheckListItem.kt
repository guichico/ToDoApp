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
data class CheckListItem(
    val id: Long = 0,
    val name: String,
    val isDone: Boolean = false
) : Parcelable