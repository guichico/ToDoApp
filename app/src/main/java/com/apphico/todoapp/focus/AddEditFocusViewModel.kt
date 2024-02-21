package com.apphico.todoapp.focus

import androidx.lifecycle.ViewModel
import com.apphico.core_model.FocusMode
import com.apphico.core_repository.calendar.focus.FocusRepository
import com.apphico.todoapp.di.AddEditFocusScreenArgModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class AddEditFocusViewModel @Inject constructor(
    @AddEditFocusScreenArgModule.FocusData private val focusArg: FocusMode?,
    private val focusRepository: FocusRepository
) : ViewModel() {

    val editingFocus = MutableStateFlow(focusArg ?: FocusMode())

    val isEditing = focusArg != null

    fun hasChanges(): Boolean {
        val focus = focusArg ?: FocusMode()
        val editingFocus = editingFocus.value

        // TODO Implement others
        return when {
            editingFocus.name != focus.name -> true
            editingFocus.description != focus.description -> true
            editingFocus.timer != focus.timer -> true
            editingFocus.interval != focus.interval -> true
            else -> false
        }
    }
}