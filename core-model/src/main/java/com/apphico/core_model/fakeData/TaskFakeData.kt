package com.apphico.core_model.fakeData

import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Task
import java.time.LocalDateTime
import java.time.LocalTime

val mockedTask = Task(
    name = "Eat some fruit",
    isDone = true,
    startDate = LocalDateTime.now(),
    endDate = LocalDateTime.now(),
    checkList = listOf(
        CheckListItem("take dog to a walk", true),
        CheckListItem("pet the dog", false)
    ),
    daysOfWeek = listOf(2, 3, 4),
    reminder = LocalTime.of(13, 15),
    group = mockedGroup
)

val mockedTasks = listOf(
    mockedTask,
    Task(
        name = "Taking the dog for a walk",
        isDone = false
    )
)