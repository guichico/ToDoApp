package com.apphico.todoapp.group

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.apphico.core_model.Group
import com.apphico.core_repository.calendar.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class AddEditGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val group = MutableStateFlow(Group())

    // val isEditing = groupArg != null
    val isEditing = false

    fun hasChanges(): Boolean {
        return false
    }

    fun onNameChanged(name: String) {
        group.value = group.value.copy(name = name)
    }

    fun onColorChanged(color: Color) {
        group.value = group.value.copy(color = color.toArgb())
    }

    fun save() {

    }
}
