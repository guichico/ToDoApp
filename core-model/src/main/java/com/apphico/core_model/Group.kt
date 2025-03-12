package com.apphico.core_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Group(
    val id: Long = 0,
    val name: String = "",
    val color: Int = Int.MIN_VALUE
) : Parcelable