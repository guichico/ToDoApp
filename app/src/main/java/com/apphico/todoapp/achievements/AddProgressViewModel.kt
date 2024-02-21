package com.apphico.todoapp.achievements

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditProgressViewModel @Inject constructor(

) : ViewModel() {

    // val isEditing = progressArg != null
    val isEditing = false

    fun hasChanges(): Boolean {
        return false
    }
}