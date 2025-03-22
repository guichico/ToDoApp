package com.apphico.core_model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Stable
@Immutable
@Parcelize
@Serializable
data class CheckListItem(
    val id: Long = 0,
    val name: String,
    val hasDone: Boolean? = null,
    val doneDates: String? = ""
) : Parcelable {
    fun isDone(parentDate: LocalDate?): Boolean = doneDates?.contains(parentDate.toString()) == true || (parentDate == null && hasDone == true)
}