package com.apphico.todoapp.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

abstract class SavedStateHandleViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel()
