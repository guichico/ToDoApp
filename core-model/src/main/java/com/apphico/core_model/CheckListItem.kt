package com.apphico.core_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckListItem(
    val name: String,
    val isDone: Boolean = false
) : Parcelable