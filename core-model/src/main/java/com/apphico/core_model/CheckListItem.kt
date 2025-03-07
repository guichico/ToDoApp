package com.apphico.core_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckListItem(
    val id: Long = 0,
    val name: String,
    val isDone: Boolean = false
) : Parcelable