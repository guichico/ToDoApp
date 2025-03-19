package com.apphico.core_model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class CheckBoxItem : Parcelable {
    @get:StringRes
    abstract val title: Int
}