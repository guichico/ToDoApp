package com.apphico.todoapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NavigationBarViewModel @Inject constructor() : ViewModel() {

    val shouldShowBlueNavBar = MutableStateFlow(false)

    fun setShouldShowBlueNavBar(value: Boolean) {
        shouldShowBlueNavBar.value = value
    }
}