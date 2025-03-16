package com.apphico.core_repository.calendar.room.entities

import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.Task

fun Group.toGroupDB(): GroupDB =
    GroupDB(
        groupId = this.id,
        name = this.name,
        color = this.color
    )

fun GroupDB.toGroup(): Group =
    Group(
        id = this.groupId,
        name = this.name,
        color = this.color
    )

fun CheckListItem.toCheckListItemDB(taskId: Long): CheckListItemDB =
    CheckListItemDB(
        checkListItemId = this.id,
        checkListTaskId = taskId,
        name = this.name,
        isDone = this.isDone
    )

fun CheckListItemDB.tCheckListItem(): CheckListItem =
    CheckListItem(
        id = this.checkListItemId,
        name = this.name,
        isDone = this.isDone
    )

fun Location.toLocationDB(taskId: Long): LocationDB =
    LocationDB(
        locationId = this.id,
        locationTaskId = taskId,
        latitude = this.coordinates.first,
        longitude = this.coordinates.second,
        address = this.address
    )

fun LocationDB.toLocation(): Location =
    Location(
        id = this.locationId,
        coordinates = Coordinates(this.latitude, this.longitude),
        address = this.address
    )

fun Task.toTaskDB(): TaskDB =
    TaskDB(
        taskId = this.id,
        name = this.name,
        description = this.description,
        taskGroupId = this.group?.id,
        startDate = this.startDate,
        startTime = this.startTime,
        endDate = this.endDate,
        endTime = this.endTime,
        daysOfWeek = this.daysOfWeek,
        reminder = this.reminder,
        isDone = this.isDone
    )

fun TaskRelations.toTask(): Task =
    Task(
        id = this.taskDB.taskId,
        name = this.taskDB.name,
        description = this.taskDB.description,
        group = this.groupDB?.toGroup(),
        checkList = this.checkList?.map { it.tCheckListItem() } ?: emptyList(),
        startDate = this.taskDB.startDate,
        startTime = this.taskDB.startTime,
        endDate = this.taskDB.endDate,
        endTime = this.taskDB.endTime,
        daysOfWeek = this.taskDB.daysOfWeek ?: emptyList(),
        reminder = this.taskDB.reminder,
        location = this.locationDB?.toLocation(),
        isDone = this.taskDB.isDone
    )