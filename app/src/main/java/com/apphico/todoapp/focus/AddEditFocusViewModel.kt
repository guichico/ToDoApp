package com.apphico.todoapp.focus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.apphico.core_model.FocusMode
import com.apphico.core_repository.calendar.focus.FocusRepository
import com.apphico.todoapp.navigation.CustomNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditFocusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val focusRepository: FocusRepository
) : ViewModel() {

    private val focusMode = savedStateHandle.toRoute<AddEditFocusRoute>(
        typeMap = mapOf(typeOf<AddEditFocusParameters>() to CustomNavType(AddEditFocusParameters::class.java, AddEditFocusParameters.serializer()))
    ).addEditFocusParameters.focusMode

    val editingFocus = MutableStateFlow(focusMode ?: FocusMode())
    val isEditing = focusMode != null

    fun hasChanges(): Boolean {
        val focus = focusMode ?: FocusMode()
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