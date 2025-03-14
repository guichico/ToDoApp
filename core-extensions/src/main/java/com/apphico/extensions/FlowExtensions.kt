package com.apphico.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip

fun Flow<Boolean>.ifTrue() = filter { boolean -> boolean }

fun <T> Flow<T>.startWith(value: T) = onStart { emit(value) }

fun <T1, T2> Flow<T1>.zip(
    flow2: Flow<T2>
) = zip(flow2) { t1, t2 -> Pair(t1, t2) }

fun <T1, T2> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>
) = combine(flow, flow2) { t1, t2 -> Pair(t1, t2) }

fun <T1, T2, T3> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>
) = combine(flow, flow2, flow3) { t1, t2, t3 -> Triple(t1, t2, t3) }