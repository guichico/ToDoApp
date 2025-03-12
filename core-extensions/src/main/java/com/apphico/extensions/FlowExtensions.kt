package com.apphico.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart

fun Flow<Boolean>.ifTrue() = filter { true }

fun <T> Flow<T>.startWith(value: T) = onStart { emit(value) }