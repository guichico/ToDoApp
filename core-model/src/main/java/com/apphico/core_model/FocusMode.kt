package com.apphico.core_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class FocusMode(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val timer: Long = 0L,
    val interval: Long = 0L,
    val group: Group? = null,
) : Parcelable