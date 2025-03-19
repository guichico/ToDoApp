package com.apphico.todoapp.location

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.apphico.todoapp.navigation.CustomNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class SelectLocationOnMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val coordinates = savedStateHandle.toRoute<SelectLocationOnMapRoute>(
        typeMap = mapOf(
            typeOf<SelectLocationOnMapParameters>() to CustomNavType(
                SelectLocationOnMapParameters::class.java,
                SelectLocationOnMapParameters.serializer()
            )
        )
    ).selectLocationOnMapParameters.coordinates
}