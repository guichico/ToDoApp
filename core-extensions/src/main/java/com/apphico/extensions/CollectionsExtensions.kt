package com.apphico.extensions

fun <T, U> List<T>.isEqualToBy(other: List<T>, compareBy: (T) -> U) = map(compareBy).toSet() == other.map(compareBy).toSet()

fun <T> List<T>.add(newItem: T) = this.toMutableList().apply { add(newItem) }

fun <T> List<T>.remove(item: T) = this.toMutableList().apply { remove(item) }

fun <T> List<T>.update(oldItem: T, newItem: T) = this.toMutableList().apply { this[this.indexOf(oldItem)] = newItem }