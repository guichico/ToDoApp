package com.apphico.todoapp.group

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.Group
import com.apphico.core_repository.calendar.group.GroupRepository
import com.apphico.todoapp.navigation.CustomNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditGroupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val group = savedStateHandle.toRoute<AddEditGroupRoute>(
        typeMap = mapOf(
            typeOf<AddEditGroupParameters>() to CustomNavType(
                AddEditGroupParameters::class.java,
                AddEditGroupParameters.serializer()
            )
        )
    ).addEditGroupParameters.group

    val editingGroup = MutableStateFlow(group ?: Group())
    val isEditing = group != null

    fun hasChanges(): Boolean {
        return false
    }

    fun onNameChanged(name: String) {
        editingGroup.value = editingGroup.value.copy(name = name)
    }

    fun onColorChanged(color: Color) {
        editingGroup.value = editingGroup.value.copy(color = color.toArgb())
    }

    fun save(onResult: (Boolean) -> Unit) {
        editingGroup.value.let { group ->
            if (group.name.isEmpty()) {
                return
            }
        }

        var group = editingGroup.value

        viewModelScope
            .launch {
                onResult(
                    if (isEditing) {
                        groupRepository.updateGroup(group)
                    } else {
                        groupRepository.insertGroup(group)
                    }
                )
            }
    }

    fun delete(onResult: (Boolean) -> Unit) {
        var group = editingGroup.value

        viewModelScope.launch {
            onResult(groupRepository.deleteGroup(group))
        }
    }
}
