package com.apphico.core_model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
enum class TaskStatus(@StringRes val title: Int) : Parcelable {
    ALL(R.string.status_all),
    DONE(R.string.status_finished),
    UNDONE(R.string.status_unfinished)
}