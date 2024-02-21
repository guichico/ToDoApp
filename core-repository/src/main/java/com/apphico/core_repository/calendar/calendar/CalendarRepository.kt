package com.apphico.core_repository.calendar.calendar

import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Task
import java.time.LocalDateTime
import java.time.LocalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CalendarRepository {

    fun getCalendar(): Flow<List<Task>>
}

class CalendarRepositoryImpl : CalendarRepository {

    override fun getCalendar(): Flow<List<Task>> =
        flow {
            emit(
                listOf(
                    Task(
                        name = "Comer fruta",
                        isDone = true,
                        group = Group(id = 0, name = "Escola", color = -6729757)
                    ),
                    Task(
                        name = "Levar cachorro passear",
                        isDone = false,
                        reminder = LocalTime.of(8, 0),
                        group = Group(id = 0, name = "Escola", color = -17587)
                    ),
                    Task(
                        name = "Estudar",
                        isDone = false,
                        checkList = listOf(
                            CheckListItem(
                                name = "matemática",
                                isDone = false
                            ),
                            CheckListItem(
                                name = "português",
                                isDone = true
                            )
                        ),
                        startDate = LocalDateTime.now()
                            .withHour(13)
                            .withMinute(0),
                        endDate = LocalDateTime.now()
                            .withHour(14)
                            .withMinute(30),
                        reminder = LocalTime.of(13, 15)
                    ),
                    Task(
                        name = "Almoçar saudável",
                        isDone = true
                    ),
                    Task(
                        name = "Consulta com dermatologista",
                        isDone = true,
                        startDate = LocalDateTime.now()
                            .withHour(15)
                            .withMinute(0),
                        endDate = LocalDateTime.now()
                            .withHour(15)
                            .withMinute(30),
                        reminder = LocalTime.of(14, 30),
                        group = Group(id = 0, name = "Escola", color = -402340)
                    ),
                    Task(
                        name = "Buscar filho na escola",
                        isDone = true,
                        endDate = LocalDateTime.now()
                            .withHour(17)
                            .withMinute(0),
                        group = Group(id = 0, name = "Escola", color = -19514)
                    ),
                    Task(
                        name = "Fazer exercícios",
                        isDone = true,
                        group = Group(id = 0, name = "Escola", color = -2089417)
                    ),
                    Task(
                        name = "Ler por pelo menos 20 minutos",
                        isDone = false,
                        group = Group(id = 0, name = "Escola", color = -12677526)
                    ),
                    Task(
                        name = "Lavar a louça",
                        isDone = false,
                        group = Group(id = 0, name = "Escola", color = -7745552)
                    ),
                    Task(
                        name = "Jantar saudável",
                        isDone = false,
                        group = Group(id = 0, name = "Escola", color = -2170138)
                    ),
                    Task(
                        name = "Tarefa com o título muito mas muito longo mesmoooo!",
                        isDone = true,
                        startDate = LocalDateTime.now()
                            .withHour(15)
                            .withMinute(0),
                        endDate = LocalDateTime.now()
                            .withHour(15)
                            .withMinute(30),
                        reminder = LocalTime.of(14, 30),
                        group = Group(id = 0, name = "Escola", color = -402340),
                        checkList = listOf(
                            CheckListItem(
                                name = "Tarefa de casa",
                                isDone = true
                            ),
                            CheckListItem(
                                name = "Tarefa de trabalho",
                                isDone = false
                            )
                        ),
                    ),
                )
            )
        }
}

/*
GroupGray: -2170138
GroupBlue: -7745552
GroupGreen: -12677526
GroupRed: -2089417
GroupPink: -19514
GroupLightBrown.: -2184304
GroupDarkBrown: -8432327
GroupYellow: -402340
GroupOrange: -17587
GroupViolet: -6729757
*/