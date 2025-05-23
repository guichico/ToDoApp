package com.apphico.extensions

fun <T, U> List<T>.isEqualToBy(other: List<T>, compareBy: (T) -> U) = map(compareBy).toSet() == other.map(compareBy).toSet()

fun <T> List<T>.add(newItem: T) = this.toMutableList().apply { add(newItem) }

fun <T> List<T>.remove(item: T) = this.toMutableList().apply { remove(item) }

fun <T> List<T>.update(oldItem: T, newItem: T) = this.toMutableList().apply {
    try {
        this[this.indexOf(oldItem)] = newItem
    } catch (_: Exception) {
    }
}

fun <T> List<T>.update(oldIndex: Int, newItem: T) = this.toMutableList().apply {
    try {
        this[oldIndex] = newItem
    } catch (_: Exception) {
    }
}

fun <T> List<T>.addOrRemove(item: T): List<T> {
    return this.toMutableList().apply {
        if (this.contains(item)) {
            remove(item)
        } else {
            add(item)
        }
    }
}