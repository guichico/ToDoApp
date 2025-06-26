package com.apphico.core_model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
enum class Status(val intValue: Int, @param:StringRes val title: Int) : Parcelable {
    ALL(0, R.string.status_all),
    DONE(1, R.string.status_finished),
    UNDONE(2, R.string.status_unfinished)
}